package com.example.projekt.controllers;


import com.example.projekt.model.data.UserData;
import com.example.projekt.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@Valid @RequestBody UserData user) {
        boolean success = userService.registerUser(user);

        Map<String, String> response = new HashMap<>();
        if(success) {
            response.put("message", "Udana rejestracja");
            return ResponseEntity.ok(response);
        }
        else {
            response.put("message", "Użytkownik już istnieje");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        Map<String, String> response = new HashMap<>();
        if (userService.authenticate(username, password)) {
            response.put("message", "Logowanie udane");
            response.put("status", "loggedIn");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Błędne dane logowania");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        if(userService.updatePassword(credentials.get("username"), credentials.get("password"))) {
            response.put("message", "Hasło zostało zmienione");
            return ResponseEntity.ok(response);
        }
        else {
            response.put("message", "Podane hasło jest takie samo jak poprzednie");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
