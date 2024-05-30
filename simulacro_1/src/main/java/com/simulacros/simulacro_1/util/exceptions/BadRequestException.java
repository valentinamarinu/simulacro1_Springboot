package com.simulacros.simulacro_1.util.exceptions;

/* EXCEPTION 404 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
