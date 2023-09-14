package com.oluwaseun.dronedispatch.exception;

import com.oluwaseun.dronedispatch.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Calendar;
import java.util.Objects;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(UnauthorisedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorisedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getRequestURI())
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .timestamp(Calendar.getInstance().getTime())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadArgumentException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = null;
        if(ex instanceof MethodArgumentNotValidException exception) {
            errorResponse = ErrorResponse.builder()
                    .path(request.getRequestURI())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                    .timestamp(Calendar.getInstance().getTime())
                    .build();
        }
        if(ex instanceof MissingServletRequestParameterException exception) {
            errorResponse = ErrorResponse.builder()
                    .path(request.getRequestURI())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(exception.getMessage())
                    .timestamp(Calendar.getInstance().getTime())
                    .build();
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateEntityException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(Calendar.getInstance().getTime())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getRequestURI())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .timestamp(Calendar.getInstance().getTime())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
