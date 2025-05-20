package com.upao.infraestructura.ms_auth_service.controllers;


import com.upao.infraestructura.ms_auth_service.models.AuthRequest;
import com.upao.infraestructura.ms_auth_service.models.AuthResponse;
import com.upao.infraestructura.ms_auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Usuario Registrado");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
