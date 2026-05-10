package com.project.movie.reservation.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomException{

    public UnAuthorizedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
