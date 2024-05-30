package com.simulacros.simulacro_1.api.error_handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.simulacros.simulacro_1.api.dto.errors.BaseErrorResp;
import com.simulacros.simulacro_1.api.dto.errors.ErrorsResp;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
// import com.simulacros.simulacro_1.util.exceptions.NotFoundException;

@RestControllerAdvice
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestController {
    /* Manejo de errores  400 de tipo que el usuario ingresa incorrectamente algo en el request */
    /* No me tengo que crear una exception para este error debido a que es una clase interna creada directamente por springboot y esta directamente relacionada con @NotBlank y @NotNull */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResp handleBadRequest(MethodArgumentNotValidException exception){
        /* Se inicializa la lista de erorres */
        List<Map<String, String>> errors = new ArrayList<>();
    
        /* Aquí se llena la lista con los errores */
        exception.getBindingResult().getFieldErrors().forEach(e -> {
            Map<String, String> error = new HashMap<>(); /* Estructura de tipo llave-valor */
            error.put("Error",e.getDefaultMessage()); /* Agrega al map el error */
            error.put("Error",e.getField()); /* Agrega al map donde ocurrió el error */
            errors.add(error); /* Guarda el error en la lista de errores */
        });
        
        /* Construyo la respuesta de los errores */
        return ErrorsResp.builder()
                .code(HttpStatus.BAD_REQUEST.value()) /* Atributo que hereda del padre */
                .status(HttpStatus.BAD_REQUEST.name()) /* Atributo que hereda del padre */
                .errors(errors) /* Atributo que tiene el hijo */
                .build();
    }

    /* Manejo de errores 404 de cuando no se encuentra la entidad solicitada */
    @ExceptionHandler(BadRequestException.class)
    public BaseErrorResp handleError(BadRequestException exception){
        List<Map<String,String>> errors = new ArrayList<>();

        Map<String,String> error = new HashMap<>();
        
        error.put("id", exception.getMessage());

        return ErrorsResp.builder()
                .code(HttpStatus.NOT_FOUND.value()) // 404
                .status(HttpStatus.NOT_FOUND.name()) // NOT_FOUND
                .errors(errors) // [ { "field": "mal", "error": "mal"} ]
                .build();
    }

    /* Otra forma de hacer la 404 desde la librería
    @ExceptionHandler(NotFoundException.class)
    public BaseErrorResp handleError(NotFoundException exception){
        List<Map<String,String>> errors = new ArrayList<>();

        Map<String,String> error = new HashMap<>();
        
        error.put("id", exception.getMessage());

        return ErrorsResp.builder()
                .code(HttpStatus.NOT_FOUND.value()) //400
                .status(HttpStatus.NOT_FOUND.name()) //BAD_REQUEST
                .errors(errors) // [ { "field": "mal", "error": "mal"} ]
                .build();

    } */
}


