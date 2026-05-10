package com.project.movie.reservation.dto;

import com.project.movie.reservation.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
}
