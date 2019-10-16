package com.auth.repo;

import com.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByUuid(UUID userId);
}
