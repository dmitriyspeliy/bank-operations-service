package com.effectivemobile.bankoperationsservice.utils.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 400
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ErrorResponse> badRequestException() {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message("No user with this login")
                        .code(BAD_REQUEST.toString())
                        .build());
    }

    /**
     * 400
     */
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }


    /**
     * 401
     */
    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ErrorResponse> unauthorizedException(UnauthorizedException ex) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }


    /**
     * 403
     */
    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<ErrorResponse> forbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }

    /**
     * 404
     */
    @ExceptionHandler(ElemNotFound.class)
    public final ResponseEntity<ErrorResponse> elemNotFound(ElemNotFound ex) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }

    /**
     * 500
     */
    @ExceptionHandler(InternalServerException.class)
    public final ResponseEntity<ErrorResponse> internalServerException(InternalServerException ex) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }


    /**
     * 400
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class})
    public final ResponseEntity<ErrorResponse> invalidParam() {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message("Invalid param or method does not exist")
                        .code(String.valueOf(BAD_REQUEST.value()))
                        .build());
    }

    /**
     * 406
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .code(String.valueOf(NOT_ACCEPTABLE.value()))
                        .build());
    }

    /**
     * 505
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public final ResponseEntity<ErrorResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
                .status(HTTP_VERSION_NOT_SUPPORTED)
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .code(String.valueOf(HTTP_VERSION_NOT_SUPPORTED.value()))
                        .build());
    }

}
