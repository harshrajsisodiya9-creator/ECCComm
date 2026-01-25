package com.harsh.Ecom.Security;

import com.harsh.Ecom.Model.Provider;
import com.harsh.Ecom.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
        List<String> rolesss = user.getRole().stream().
                map(role -> "Role_" + role.name())      // role.name() is the to way to get the ENUM
                .toList();                                      // ["ROLE_ADMIN","ROLE_CUSTOMER"]

        return Jwts.builder()
                .subject(user.getUsername())       // this is the subject which we get down below
                .claim("roles",rolesss)           // "roles": ["ROLE_ADMIN", "ROLE_CUSTOMER"]
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSecretKey())        // signed secretkey
                .compact();
    }

    public Claims getClaimFromJwt(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Claims is a Map<String, Object>  for e.g claim.get("role") will return role of the user
    public String getUsernameFromToken(String token){
        Claims claim = getClaimFromJwt(token);
        return claim.getSubject();    // subject we put in above method
    }

    // GrantedAuthorities is a interface which is extended by SimpleAuthority and OauthAuthority, JwtGrantedAuthority so we hence
    // we write Collection<? extends GrantedAuthority> and cause we want a generic which is not specifically used
    // and we use prefer loose and interface implementation although in this case we could have written
    // List<SimpleGrantedAuthority> and gotten away with it (difference in flexibility and restrictiveness)
    public Collection<? extends GrantedAuthority> getAuthorities(String token){
        Claims claim = getClaimFromJwt(token);
        List<String> roles = claim.get("roles",List.class);        // get the roles and cast it in to a List
        return roles.stream()                                           // same as (Object) claims.get("roles")
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();
    }

    // Claims {
    //  "sub": "harsh",
    //  "roles": ["ROLE_ADMIN", "ROLE_USER"],
    //  "iat": 1700000000,
    //  "exp": 1700086400
    //} Claim token client side se aa rha hai
    // so we are reading the roles like this from claims while in UserDetailService go and check there
}
