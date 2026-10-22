package com.springboot.librarymanagement.controller;


import com.springboot.librarymanagement.dto.request.RegisterDto;
import com.springboot.librarymanagement.dto.response.TokenDto;
import com.springboot.librarymanagement.service.AuthService;
import com.springboot.librarymanagement.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtility jwtUtility;
    private final AuthService authService;

    /* This api is authenticated by Spring. which means if the control reaches this method, Spring
     * already has checked the credentials and it can share the username */
    @PostMapping("/api/auth/register")
    public void register(@RequestBody RegisterDto dto){
        authService.register(dto);
    }

    @GetMapping("/api/auth/login")
    public TokenDto login(Principal principal){
        String loggedInUsername = principal.getName();
        String token = jwtUtility.generateToken(loggedInUsername);
        return new TokenDto(token,
                jwtUtility.extractExpiration(token),
                LocalDateTime.now().toString());
    }
}
