package com.harsh.Ecom.Security;

import com.harsh.Ecom.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {

    // the file for all utilities related to JWT token

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
    public String getUsernameFronToken(String token){
        Claims claim = Jwts.parser()
                .verifyWith(getSecretKey()) // secretkey provided above
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claim.getSubject();    // subject we put in above method
    }
}
