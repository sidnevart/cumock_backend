package com.example.cumock.repository;

import com.example.cumock.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByTelegramId(Long telegramId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
