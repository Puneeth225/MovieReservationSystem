package com.project.movie.reservation.exception;

import org.springframework.http.HttpStatus;

public class ShowTimingClashException extends CustomException{

    public ShowTimingClashException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
