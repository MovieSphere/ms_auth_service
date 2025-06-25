
package com.upao.infraestructura.ms_auth_service.services;

import com.upao.infraestructura.ms_auth_service.models.AuthRequest;
import com.upao.infraestructura.ms_auth_service.models.User;
import com.upao.infraestructura.ms_auth_service.repositories.UserRepository;
import com.upao.infraestructura.ms_auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(AuthRequest request) {
        logger.info("Intentando registrar usuario: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Intento de registrar usuario ya existente: {}", request.getUsername());
            throw new RuntimeException("El usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);
        logger.info("Usuario registrado exitosamente: {}", request.getUsername());
    }

    public String login(AuthRequest request) {
        logger.info("Intentando iniciar sesión para: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            logger.error("Fallo de autenticación para: {}", request.getUsername());
            throw e;
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado en login: {}", request.getUsername());
                    return new RuntimeException("Usuario no encontrado");
                });

        String token = jwtUtil.generateToken(user);
        logger.info("Token generado exitosamente para: {}", request.getUsername());
        return token;
    }
}
