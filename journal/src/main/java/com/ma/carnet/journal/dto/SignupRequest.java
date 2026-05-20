package com.ma.carnet.journal.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
}