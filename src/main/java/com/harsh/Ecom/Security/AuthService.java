package com.harsh.Ecom.Security;

import com.harsh.Ecom.DTO.LoginRequestDto;
import com.harsh.Ecom.DTO.LoginResponseDto;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

//    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    public AuthService(AuthenticationManager authenticationManager){this.authenticationManager = authenticationManager;}


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );
    }
}
