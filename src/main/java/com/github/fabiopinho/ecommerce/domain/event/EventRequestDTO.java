package com.github.fabiopinho.ecommerce.domain.event;

public record EventRequestDTO(String title,
                              String description,
                              Long date,
                              String city,
                              String state,
                              Boolean remote,
                              String eventUrl) {
}
