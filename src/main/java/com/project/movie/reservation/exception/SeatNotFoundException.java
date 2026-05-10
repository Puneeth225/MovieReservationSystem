package com.project.movie.reservation.exception;

import org.springframework.http.HttpStatus;

public class SeatNotFoundException extends CustomException{

    public SeatNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
