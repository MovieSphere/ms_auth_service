package com.upao.infraestructura.ms_auth_service.models;

public class ResponseToken {
    private String jwt;

    public ResponseToken() {
    }

    public ResponseToken(String jwt) {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}

