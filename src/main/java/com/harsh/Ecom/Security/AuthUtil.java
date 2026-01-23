package com.harsh.Ecom.Security;

import com.harsh.Ecom.Model.Provider;
import com.harsh.Ecom.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {

    // the file for all utilities related to OAuth and JWT

    //OAuth2
    public Provider getProviderFromRegistrationId(String registrationId){
        return switch (registrationId.toLowerCase()){
            case "google" -> Provider.GOOGLE;
            case "github" -> Provider.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider");
        };
    }

    public String getProviderId(OAuth2User oAuth2User, String registrationId, Provider provider) {
        return switch (provider){
            case GOOGLE -> oAuth2User.getAttribute("sub").toString();
            case GITHUB -> oAuth2User.getAttribute("id").toString();
            default -> {throw new IllegalArgumentException("Unsupported OAuth2 provider");}
        };
    }

    public String getUsernameFromOAuth2User(OAuth2User oAuth2User,String email,Provider provider) {
        if(email == null || email.isBlank()){
             return switch (provider){
                case GITHUB -> oAuth2User.getAttribute("login");
                case GOOGLE -> oAuth2User.getAttribute("sub");
                default -> throw new IllegalArgumentException("Unsupported OAuth2 provider");
             };
        }
        return email;
    }

    // JWT
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())       // this is the subject which we get down below
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSecretKey())        // signed secretkey
                .compact();
    }

    // Claims is a Map<String, Object>  for e.g claim.get("role") will return role of the user
    public String getUsernameFromToken(String token){
        Claims claim = Jwts.parser()
                .verifyWith(getSecretKey()) // secretkey provided above
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claim.getSubject();    // subject we put in above method
    }
}
