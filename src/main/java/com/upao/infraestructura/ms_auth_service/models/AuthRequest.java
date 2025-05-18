package com.upao.infraestructura.ms_auth_service.models;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
