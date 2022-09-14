package dev.soulcatcher.advice;

import dev.soulcatcher.dtos.ErrorResponse;
import dev.soulcatcher.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientFundsException.class)
    public ErrorResponse handleInsufficientFunds(Throwable t) {
        t.printStackTrace();
        List<String> messages = new ArrayList<>();
        messages.add("Insufficient funds");
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), messages);
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse handleAlreadyExists(Throwable t) {
        t.printStackTrace();
        List<String> messages = new ArrayList<>();
        messages.add("Resource already exists");
        return new ErrorResponse(HttpStatus.CONFLICT.value(), messages);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFound(Throwable t) {
        t.printStackTrace();
        List<String> messages = new ArrayList<>();
        messages.add("Resource not found");
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), messages);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorized(Throwable t) {
        t.printStackTrace();
        List<String> messages = new ArrayList<>();
        messages.add("Unauthorized");
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), messages);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MalformedJwtException.class)
    public ErrorResponse handleTokenParse(Throwable t) {
        t.printStackTrace();
        List<String> messages = new ArrayList<>();
        messages.add("Invalid token. Please login again.");
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), messages);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ErrorResponse handleTokenExpiration(Throwable t) {
        ErrorResponse resp = handleUnauthorized(t);
        resp.getMessages().set(0, "Expired login token. Please login again.");
        return resp;
    }
}
