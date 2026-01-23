package com.harsh.Ecom.Security;

import com.harsh.Ecom.DTO.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Handler {

    ResponseEntity<LoginResponseDto> oAuth2LoginRequest(OAuth2User oAuth2User,String registrationId);
}
