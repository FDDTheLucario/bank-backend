package dev.soulcatcher.controllers;

import dev.soulcatcher.dtos.AccountResponse;
import dev.soulcatcher.dtos.DetailedAccountResponse;
import dev.soulcatcher.dtos.Principal;
import dev.soulcatcher.exceptions.UnauthorizedException;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.services.AccountService;
import dev.soulcatcher.services.token.TokenService;
import jdk.nashorn.internal.parser.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TokenService tokenService;
    private final UserRepository userRepo;
    public AccountController(AccountService accountService, TokenService tokenService, UserRepository userRepo) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.userRepo = userRepo;
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/overview", produces = "application/json")
    public List<AccountResponse> getAccountInfo(@RequestHeader("Authorization") String token) {
        return accountService.findAllByToken(token);
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = "application/json")
    public DetailedAccountResponse getAccountInfo(@RequestHeader("Authorization") String token, @PathVariable("id") String accountId) {
        return accountService.getAccountInfo(token, accountId);
    }
}
