package com.example.projekt.service;


import com.example.projekt.model.data.UserData;
import com.example.projekt.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean registerUser(UserData userData) {
        Optional<UserData> user1 = userRepository.findByUsername(userData.getUsername());
        Optional<UserData> user2 = userRepository.findByEmail(userData.getEmail());
        if(user1.isPresent() || user2.isPresent())
            return false;
        userRepository.save(userData);
        return true;
    }

    public boolean updatePassword(String username, String password) {
        Optional<UserData> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            UserData user = userOptional.get();
            if (!password.equals(user.getPassword())) {
                user.setPassword(password);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean authenticate(String username, String password) {
        logger.info("Authenticating user: {}", username);
        Optional<UserData> user = userRepository.findByUsernameAndPassword(username, password);
        logger.info("Query result: {}", user.isPresent());
        return user.isPresent();
    }
}
