package com.pigeondev.Pigeon.Ecommerce.repository;

import com.pigeondev.Pigeon.Ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
