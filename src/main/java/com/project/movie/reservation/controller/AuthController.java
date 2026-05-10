package com.project.movie.reservation.controller;

import com.project.movie.reservation.dto.AuthRequestDto;
import com.project.movie.reservation.dto.AuthResponseDto;
import com.project.movie.reservation.dto.SignUpRequestDto;
import com.project.movie.reservation.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signupUser(@RequestBody SignUpRequestDto signupRequestDto){
        String token = authService.signupUser(signupRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthResponseDto.builder().token(token).build());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authenticateUser(@RequestBody AuthRequestDto authRequestDto){
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword());
        authenticationManager.authenticate(authToken);
        String token = authService.authenticateUser(authRequestDto.getUsername());
        return ResponseEntity.ok(AuthResponseDto.builder().token(token).build());
    }

}


//eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJteXVzZXIxIiwiUk9MRVMiOlsiUk9MRV9TVVBFUl9BRE1JTiJdLCJpYXQiOjE3Nzg0MzczMjksImV4cCI6MTc3ODU0NTMyOX0.mKODRX6u9Z8Wv9C_JCWq6vLg88HXv4gCXivlE6EE32dLTruFemBELGmhmNa2t_1O