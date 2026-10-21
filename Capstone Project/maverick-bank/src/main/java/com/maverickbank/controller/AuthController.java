package com.maverickbank.controller;

import com.maverickbank.dto.response.TokenDto;
import com.maverickbank.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtility jwtUtility;
    /* This api is authenticated by Spring. which means if the control reaches this method, Spring
    * already has checked the credentials and it can share the username */
    @GetMapping("/api/auth/login")
    public TokenDto login(Principal principal){
        String loggedInUsername = principal.getName();
        String token = jwtUtility.generateToken(loggedInUsername);
        return new TokenDto(token,
                jwtUtility.extractExpiration(token),
                LocalDateTime.now().toString());
    }
}
