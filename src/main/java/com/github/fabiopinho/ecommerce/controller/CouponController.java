package com.github.fabiopinho.ecommerce.controller;

import com.github.fabiopinho.ecommerce.domain.coupon.Coupon;
import com.github.fabiopinho.ecommerce.domain.coupon.CouponRequestDTO;
import com.github.fabiopinho.ecommerce.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId,
                                                   @RequestBody CouponRequestDTO data){

        Coupon coupon = couponService.addCupounToEvent(eventId, data);
        return ResponseEntity.ok(coupon);

    }
}
