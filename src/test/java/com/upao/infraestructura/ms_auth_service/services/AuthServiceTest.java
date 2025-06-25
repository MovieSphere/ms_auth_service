package com.upao.infraestructura.ms_auth_service.services;

import com.upao.infraestructura.ms_auth_service.models.AuthRequest;
import com.upao.infraestructura.ms_auth_service.models.User;
import com.upao.infraestructura.ms_auth_service.repositories.UserRepository;
import com.upao.infraestructura.ms_auth_service.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test 1: Usuario ya registrado
    @Test
    void register_userAlreadyExists_throwsException() {
        // Arrange: Se configura el repositorio para devolver un usuario existente
        AuthRequest request = new AuthRequest("existingUser", "pass");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // Act & Assert: Se intenta registrar y se espera una excepción con mensaje específico
        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("El usuario ya existe", ex.getMessage());
    }

    // Test 2: Registro exitoso
    @Test
    void register_validUser_savesUser() {
        // Arrange: Usuario no existe y se prepara el encoder para devolver una contraseña encriptada
        AuthRequest request = new AuthRequest("newUser", "1234");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("hashed");

        // Act: Se llama al método register
        authService.register(request);

        // Assert: Se verifica que se haya guardado el usuario con los valores esperados
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("newUser") &&
                        user.getPassword().equals("hashed") &&
                        user.getRole().equals("USER")
        ));
    }

    // Test 3: Login exitoso
    @Test
    void login_validCredentials_returnsToken() {
        // Arrange: Se configura un usuario válido y el JWT a retornar
        AuthRequest request = new AuthRequest("user", "pass");
        User user = new User(null, "user", "pass", "USER");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("mocked-token");

        // Act: Se llama al método login
        String token = authService.login(request);

        // Assert: Se verifica que se retorne el token y que se haya autenticado
        assertEquals("mocked-token", token);
        verify(authenticationManager).authenticate(any());
    }

    // Test 4: Login con usuario inexistente
    @Test
    void login_userNotFound_throwsException() {
        // Arrange: Se configura el repositorio para devolver vacío (usuario no existe)
        AuthRequest request = new AuthRequest("ghost", "pass");
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        // Act & Assert: Se espera una excepción al intentar hacer login
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    // Test 5: Login lanza AuthenticationException
    @Test
    void login_authenticationFails_throwsException() {
        // Arrange: Se lanza excepción cuando se intenta autenticar
        AuthRequest request = new AuthRequest("user", "wrong");
        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any());

        // Act & Assert: Se espera que se lance BadCredentialsException
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
