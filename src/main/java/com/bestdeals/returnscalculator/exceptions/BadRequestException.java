package com.bestdeals.returnscalculator.exceptions;

public class BadRequestException extends Throwable {
    public BadRequestException(String error) {
        super(error);
    }
}
