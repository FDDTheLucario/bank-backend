package dev.soulcatcher.controllers;

import dev.soulcatcher.dtos.AuthResponse;
import dev.soulcatcher.dtos.LoginRequest;
import dev.soulcatcher.dtos.Principal;
import dev.soulcatcher.services.AuthService;
import dev.soulcatcher.services.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;
    private static final String AUTHORIZATION = "Authorization";
    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse resp) {
        AuthResponse auth = authService.login(loginRequest);
        Principal payload = new Principal(auth.getId(), auth.getUsername());
        String token = tokenService.generateToken(payload);
        resp.setHeader(AUTHORIZATION, token);
        return auth;
    }
}
