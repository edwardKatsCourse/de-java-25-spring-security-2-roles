package com.example.controller;

import com.example.controller.dto.LoginRequestDTO;
import com.example.controller.dto.LoginResponseDTO;
import com.example.entity.Account;
import com.example.entity.AccountSession;
import com.example.repository.AccountRepository;
import com.example.repository.AccountSessionRepository;
import com.example.service.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class MyController {

    private final PasswordService passwordService;
    private final AccountRepository accountRepository;
    private final AccountSessionRepository accountSessionRepository;


    // Play! Framework
    // routes
    // /users        GET
    // /users/:id    GET

    @PostMapping("/accounts/{id}")

//    @Secured("ADMIN")
    @PreAuthorize("hasAuthority('ADMIN') || #id == authentication.principal.accoidunt.id")
    public Account getAccountById(@PathVariable("id") Long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        Account account = passwordService.getMatchedAccount(request.getUsername(), request.getPassword());
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // UUID

        AccountSession accountSession = AccountSession.builder()
                .account(account)
                .sessionId(UUID.randomUUID().toString())
                .build();

        accountSessionRepository.save(accountSession);

        return LoginResponseDTO.builder()
                .sessionId(accountSession.getSessionId())
                .build();
    }

    @GetMapping("/accounts/me")
    public Account getMyAccount(@AuthenticationPrincipal AccountSession accountSession) {
        return accountRepository
                .findById(accountSession.getAccount().getId())
                .orElse(null);
    }

//    public static void main(String[] args) {
//        Set<String> uuids = new HashSet<>();
//
//        for (int i = 0; i < 50_000_000; i++) {
//            var result = uuids.add(UUID.randomUUID().toString());
//            if (!result) {
//                throw new RuntimeException("Duplicate!! " + i);
//            }
//        }
//    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal AccountSession accountSession) {
        accountSessionRepository.delete(accountSession);
    }

}
