package com.harsh.Ecom.Security;

import com.harsh.Ecom.DTO.LoginRequestDto;
import com.harsh.Ecom.DTO.LoginResponseDto;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class LoginService {

    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repo;

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // typecasting (UserDetails) explicitly here cause getPrincipal just returns a generic object
        //Username/password	    UserDetails
        //OAuth2	            OAuth2User
        //JWT	                UserDetails
        //Anonymous	            String ("anonymousUser")

        User user = repo.findByUsername(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("No user exist with this username"));
        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDto(token,user.getId());

    }
}
