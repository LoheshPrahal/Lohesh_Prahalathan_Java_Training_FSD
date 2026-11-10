package com.maverickbank.controller;

import com.maverickbank.dto.response.TokenDto;
import com.maverickbank.service.UserService;
import com.maverickbank.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtility jwtUtility;
    private final UserService userService;
    /* This api is authenticated by Spring. which means if the control reaches this method, Spring
    * already has checked the credentials and it can share the username */
    @GetMapping("/api/auth/login")
    public TokenDto login(Principal principal){
        String loggedInUsername = principal.getName();
        // The role is saved in the token and it is sent to the frontend, so that it can open the right dashboard
        String role = userService.getUserByUsername(loggedInUsername).getRole().toString();
        String token = jwtUtility.generateToken(loggedInUsername, role);
        return new TokenDto(token,
                jwtUtility.extractExpiration(token),
                LocalDateTime.now().toString(),
                role);
    }
}
