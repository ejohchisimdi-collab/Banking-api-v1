package com.chisimdi.Banking.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExistsException.class)
    public ResponseEntity<ApiError>existsHandler(ExistsException e){
        ApiError apiError=new ApiError(500,e.getMessage());
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError>insufficientFundsHandler(InsufficientFundsException e){
        ApiError apiError=new ApiError(500, e.getMessage());
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError>resourceNotFoundException(ResourceNotFoundException e){
        ApiError apiError=new ApiError(404, e.getMessage());
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError2>validationHandler(MethodArgumentNotValidException e){
        log.warn("Validation error {}",e.getMessage(),e);
        ApiError2 apiError2=new ApiError2(400,"validation error");
        for (FieldError error:e.getBindingResult().getFieldErrors()){
            apiError2.getErrors().put(error.getField(), error.getDefaultMessage());

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError2);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError>generalHandler(Exception e){
        log.error("internal Server Error {}",e.getMessage(),e);
        ApiError apiError=new ApiError(500,"internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    @ExceptionHandler(WrongRoleException.class)
    public ResponseEntity<ApiError>roleHandler(WrongRoleException e){
        log.warn(e.getMessage());
        ApiError apiError=new ApiError(500,e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError>illegalArgumentHandler(IllegalArgumentException e){
        log.warn(e.getMessage());
        ApiError apiError=new ApiError(400, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError>invalidCredentialsExceptionResponseEntity(InvalidCredentialsException e){
        log.warn(e.getMessage(),e);
        ApiError apiError=new ApiError(400, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError>deniedHandler(AuthorizationDeniedException e){
        log.warn(e.getMessage());
        ApiError apiError=new ApiError(401,"UnAuthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

}
