package com.github.fabiopinho.ecommerce.controller;

import com.github.fabiopinho.ecommerce.domain.event.Event;
import com.github.fabiopinho.ecommerce.domain.event.EventDetailsDTO;
import com.github.fabiopinho.ecommerce.domain.event.EventRequestDTO;
import com.github.fabiopinho.ecommerce.domain.event.EventResponseDTO;
import com.github.fabiopinho.ecommerce.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * Cria um evento
     *
     * dica: utilizar o https://www.epochconverter.com/ e passar no campo Date
     *      *    - pegando o Timestamp in milliseconds: 1719782413000
     *
     * */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(@RequestParam("title") String title,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam("date") Long date,
                                        @RequestParam("city") String city,
                                        @RequestParam("state") String state,
                                        @RequestParam("remote") Boolean remote,
                                        @RequestParam("eventUrl") String eventUrl,
                                        @RequestParam(value = "image", required = false) MultipartFile image){
        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, state, remote, eventUrl);
        Event newEvent = this.eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }


    /**
     * O sistema deve retornar apenas os eventos que nao aconteceram.
     *
     * dica: utilizar o https://www.epochconverter.com/ e passar no campo Date
     *    - pegando o Timestamp in milliseconds: 1719782413000
     *
     * */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){

        List<EventResponseDTO> allEvents = eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);

    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(required = false) String city,
                                                               @RequestParam(required = false) String uf,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date startDate,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date endDate){

        List<EventResponseDTO> events = eventService.getFilteredEvents(page, size, city, uf, title, startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId){

        EventDetailsDTO eventDetails = eventService.getEventDetails(eventId);

        return ResponseEntity.ok(eventDetails);
    }
}
