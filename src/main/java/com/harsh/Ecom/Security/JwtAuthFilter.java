package com.harsh.Ecom.Security;

import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    //see if the lombok fu**ing works this time
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("incoming request : {}", request.getRequestURI());

            final String requestHandlerToken = request.getHeader("Authorization");

            if (requestHandlerToken == null || !requestHandlerToken.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Authorization : "Bearer Username.JWT_Token" get the second part i.e 1 after splitting
            String token = requestHandlerToken.split("Bearer ")[1];

            // Jwt token = header.payload.signature all are encoded
            //header consist of alg and typ(JWT here) before encoding
            //payload consist of sub:"blah69", "role" : "ADMIN", iat, exp
            // signature is header + payload + secret key
            // note : all this header.payload.signature is done before this point we are just splitting and verifying them
            // all this comment so that i can remember header is appearing twice and both are different ones

            String username = authUtil.getUsernameFromToken(token);
            Collection<? extends GrantedAuthority> authorities = authUtil.getAuthorities(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsername(username).orElseThrow();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);            // continue in the filter chain
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            // shift the error(if occurs) to spring mvc(not filter chain) , which will then be handled by our globalexceptionhandler
        }
    }
}
