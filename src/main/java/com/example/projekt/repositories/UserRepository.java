package com.example.projekt.repositories;

import com.example.projekt.model.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUsernameAndPassword(String username, String password);
    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByEmail(String email);
}
