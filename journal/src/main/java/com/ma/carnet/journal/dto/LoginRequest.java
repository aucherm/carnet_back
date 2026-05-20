package com.ma.carnet.journal.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String mail;
    private String password;
}