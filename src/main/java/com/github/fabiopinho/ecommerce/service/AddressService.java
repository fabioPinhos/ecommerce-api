package com.github.fabiopinho.ecommerce.service;

import com.github.fabiopinho.ecommerce.domain.address.Address;
import com.github.fabiopinho.ecommerce.domain.event.Event;
import com.github.fabiopinho.ecommerce.domain.event.EventRequestDTO;
import com.github.fabiopinho.ecommerce.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(EventRequestDTO data, Event event){

        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);

        return addressRepository.save(address);

    }
}
