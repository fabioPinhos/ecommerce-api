package com.github.fabiopinho.ecommerce.repositories;

import com.github.fabiopinho.ecommerce.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
