package com.upao.infraestructura.ms_auth_service.controllers;


import com.upao.infraestructura.ms_auth_service.models.ResponseToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping
    public ResponseToken getJWToken() {
        ResponseToken token = new ResponseToken();
        token.setJwt("eylk23423u4ho23542243e.2j3nk23");
        return token;
    }
}

