package com.github.fabiopinho.ecommerce.service;

import com.github.fabiopinho.ecommerce.domain.coupon.Coupon;
import com.github.fabiopinho.ecommerce.domain.coupon.CouponRequestDTO;
import com.github.fabiopinho.ecommerce.domain.event.Event;
import com.github.fabiopinho.ecommerce.repositories.CouponRepository;
import com.github.fabiopinho.ecommerce.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    EventRepository eventRepository;

    public Coupon addCupounToEvent(UUID eventId, CouponRequestDTO coupomData){

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(coupomData.code());
        coupon.setDiscount(coupomData.discount());
        coupon.setValid(new Date( coupomData.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);

    }

    public List<Coupon> consultCoupons(UUID eventId, Date currentDate){
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}
