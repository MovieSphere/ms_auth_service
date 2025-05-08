package com.upao.infraestructura.ms_auth_service.repositories;

import com.upao.infraestructura.ms_auth_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
