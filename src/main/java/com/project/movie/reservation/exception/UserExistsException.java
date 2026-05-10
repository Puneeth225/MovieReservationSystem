package com.project.movie.reservation.exception;

import org.springframework.http.HttpStatus;

public class UserExistsException extends CustomException{

    public UserExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
