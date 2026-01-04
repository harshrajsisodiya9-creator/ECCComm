package com.harsh.Ecom.Security;

import com.harsh.Ecom.DTO.LoginMapper;
import com.harsh.Ecom.DTO.LoginRequestDto;
import com.harsh.Ecom.DTO.LoginResponseDto;
import com.harsh.Ecom.DTO.SignUpDto;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final LoginMapper mapper;

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    public AuthService(PasswordEncoder encoder, LoginMapper mapper, AuthenticationManager authenticationManager, AuthUtil authUtil, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.mapper = mapper;
        this.encoder = encoder;
    }


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        // typecasting (UserDetails) explicitly here cause getPrincipal just returns a generic object

        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();

        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDto(token, user.getId());
    }

    public SignUpDto signup(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElse(null);
        // we don't need an already used username hence we through a null, if there is already a username then we have a problem
        if(user != null){
            throw new IllegalArgumentException("Username already taken");
        }
        user = mapper.toUser(loginRequestDto);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return new SignUpDto(user.getUsername());
    }
}
