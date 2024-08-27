package com.github.fabiopinho.ecommerce.domain.coupon;

public record CouponRequestDTO(String code, Integer discount, Long valid) {
}
