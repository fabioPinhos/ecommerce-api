package com.github.fabiopinho.ecommerce.service;

import com.amazonaws.services.s3.AmazonS3;
import com.github.fabiopinho.ecommerce.domain.coupon.Coupon;
import com.github.fabiopinho.ecommerce.domain.event.Event;
import com.github.fabiopinho.ecommerce.domain.event.EventDetailsDTO;
import com.github.fabiopinho.ecommerce.domain.event.EventRequestDTO;
import com.github.fabiopinho.ecommerce.domain.event.EventResponseDTO;
import com.github.fabiopinho.ecommerce.repositories.CouponRepository;
import com.github.fabiopinho.ecommerce.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private EventRepository repository;

    @Autowired
    AddressService addressService;

    @Autowired
    CouponRepository couponRepository;


    public Event createEvent(EventRequestDTO dto){
        String imgUrl = null;

        if(dto.image() != null){
//            imgUrl = this.uploadImg(dto.image());
            imgUrl = "";
        }

        Event newEvent = new Event();
        newEvent.setTitle(dto.title());
        newEvent.setEventUrl(dto.eventUrl());
        newEvent.setDate(new Date(dto.date()));
        newEvent.setDescription(dto.description());
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(dto.remote());

        repository.save(newEvent);

        if (!dto.remote()){
            addressService.createAddress(dto, newEvent);
        }

        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartToFile(multipartFile);

            s3client.putObject(bucketName, filename, file);

            file.delete();

            return s3client.getUrl(bucketName, filename).toString();
        }catch (Exception e){
            System.out.println("erro ao subir arquivo :::" + e.getMessage());
            return "";
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public List <EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = repository.findUpcomingEvents(new Date(), pageable);
        return eventsPage.map(events -> new EventResponseDTO(events.getId(),
                events.getTitle(),
                events.getDescription(),
                events.getDate(),
                events.getAddress() != null ? events.getAddress().getCity() : "",
                events.getAddress() != null ? events.getAddress().getUf() : "",
                events.getRemote(),
                events.getEventUrl(),
                events.getImgUrl())).stream().toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, String title, Date startDate, Date endDate) {

        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date();
        endDate = endDate != null ? endDate : addTenYears(new Date());

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = repository.findFilteredEvents(city, uf, startDate, endDate, pageable);

        return eventsPage.map(events -> new EventResponseDTO(
                events.getId(),
                events.getTitle(),
                events.getDescription(),
                events.getDate(),
                events.getAddress() != null ? events.getAddress().getCity() : "",
                events.getAddress() != null ? events.getAddress().getUf() : "",
                events.getRemote(),
                events.getEventUrl(),
                events.getImgUrl())).stream().toList();
    }

    private static Date addTenYears(Date dataTeste) {

        // Obtenha uma instância do calendário
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataTeste);

        // Adicione 10 anos à data
        cal.add(Calendar.YEAR, 10);

        // Obtenha a nova data
        return cal.getTime();
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {

        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<Coupon> coupons = couponRepository.findByEventIdAndValidAfter(eventId, new Date());

        List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValid()))
                .collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                couponDTOs);
    }
}
