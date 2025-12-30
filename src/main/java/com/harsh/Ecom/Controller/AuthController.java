package com.harsh.Ecom.Controller;

import com.harsh.Ecom.DTO.LoginRequestDto;
import com.harsh.Ecom.DTO.LoginResponseDto;
import com.harsh.Ecom.Security.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        try {
            LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
            return ResponseEntity.ok(loginResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.UNAUTHORIZED);
        }
    }
}
