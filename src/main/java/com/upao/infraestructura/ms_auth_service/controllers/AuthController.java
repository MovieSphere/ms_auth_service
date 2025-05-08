package com.upao.infraestructura.ms_auth_service.controllers;

import com.upao.infraestructura.ms_auth_service.models.ResponseToken;
import com.upao.infraestructura.ms_auth_service.models.User;
import com.upao.infraestructura.ms_auth_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseToken getJWToken() {
        return new ResponseToken("eylk23423u4ho23542243e.2j3nk23");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseToken> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.ok(new ResponseToken("mocked.jwt.token"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
