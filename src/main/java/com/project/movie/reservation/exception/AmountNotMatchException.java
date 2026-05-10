package com.project.movie.reservation.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AmountNotMatchException extends CustomException {

    private Double amountToBePaid;

    public AmountNotMatchException(String message, HttpStatus httpStatus, Double amount) {
        super(message, httpStatus);
        this.amountToBePaid = amount;
    }

}
