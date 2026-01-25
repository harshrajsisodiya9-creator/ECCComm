package com.harsh.Ecom.Security;

import com.harsh.Ecom.Model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/set/**").hasAnyRole(Role.ADMIN.name(),Role.SELLER.name())// all these are the endpoints which are provided by me to the application
                                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers("/auth/**").permitAll()                        //so any framework endpoints(/login which is there in oAuth2) will not be affected by these
                        .requestMatchers("/product/**").authenticated()                 // even if i write .anyRequest.authenticated(); (which only works with my created endpoints)
                      //. anyRequest().authenticated();
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)            // ↓
                .oauth2Login(oAuth2 -> oAuth2.failureHandler(      // this will create a login page /login
                        (request, response, exception) -> {
                            log.error("OAuth2 error : {}",exception.getMessage());
                        }
                        )
                        .successHandler(oAuth2SuccessHandler)
                );
//                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }
}



// by default in spring the sessions are saved inMemory and saves/connects with the userID and checks for validity
// we don't need sessions in JWT(as it is sessionless)
//