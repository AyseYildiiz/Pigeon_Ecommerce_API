package com.pigeondev.Pigeon.Ecommerce.repository;

import com.pigeondev.Pigeon.Ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
