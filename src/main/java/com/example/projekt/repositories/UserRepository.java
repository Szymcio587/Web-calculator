package com.example.projekt.repositories;

import com.example.projekt.model.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUsernameAndPassword(String username, String password);
    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByEmail(String email);

    @Modifying
    @Query("UPDATE UserData u SET u.password = :password WHERE u.username = :username")
    void updatePassword(@Param("username") String username, @Param("password") String newPassword);
}
