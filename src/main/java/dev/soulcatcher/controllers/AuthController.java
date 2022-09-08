package dev.soulcatcher.controllers;

import dev.soulcatcher.dtos.AuthResponse;
import dev.soulcatcher.dtos.LoginRequest;
import dev.soulcatcher.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse resp) {
        AuthResponse auth = authService.login(loginRequest);
        return auth;
    }
}
