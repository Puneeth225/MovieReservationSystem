package com.project.movie.reservation.dto;

import lombok.Data;

@Data
public class UserRequestDto {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
