package com.harsh.Ecom.Security;

import com.harsh.Ecom.DTO.LoginMapper;
import com.harsh.Ecom.DTO.LoginRequestDto;
import com.harsh.Ecom.DTO.LoginResponseDto;
import com.harsh.Ecom.DTO.SignUpDto;
import com.harsh.Ecom.Model.Provider;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements OAuth2Handler{

    private final LoginMapper mapper;

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final PasswordEncoder encoder;
    public AuthService(PasswordEncoder encoder, LoginMapper mapper, AuthUtil authUtil, UserRepository userRepository){
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public User signupInternal(LoginRequestDto signupDto, Provider provider, String providerId){
        User user = userRepository.findByUsername(signupDto.getUsername()).orElse(null);
        // we don't need an already used username hence we throw a null, if there is already a username then we have a problem
        if(user != null){
            throw new IllegalArgumentException("Username already taken");
        }
        user = mapper.toUser(signupDto);
        user.setProviderId(providerId);
        user.setProvider(provider);
        if(provider == Provider.EMAIL){
            user.setPassword(encoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> oAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        //save and declare username(if doesn't exist),fetch providerId(unique id of the user provided by the provider(google,github etc)) and providerType
        Provider provider = authUtil.getProviderFromRegistrationId(registrationId);
        String providerId = authUtil.getProviderId(oAuth2User,registrationId,provider);

        User user = userRepository.findByProviderIdAndProvider(providerId, provider).orElse(null);

        String email = oAuth2User.getAttribute("email");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        if(user == null && emailUser == null){
            String username = authUtil.getUsernameFromOAuth2User(oAuth2User,email,provider);
            user = signupInternal(new LoginRequestDto(username, null), provider, providerId); // sign up the user
        } else if (user  != null) {
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                userRepository.save(user);
            }
        }
        else{            // user == null but emailUser exist(i.e user is trying to get in using a provider different than what is saved in repo
                            // but his email is saved in the repo through a different provider)
                            // what im gonna do is throw an error and ask him to login through the correct provider for now
            throw new BadCredentialsException("This email is already registered with provider " + provider);
        }
        // logging the user now
        //LoginResponseDto loginResponseDto = login(new LoginRequestDto(user.getUsername(),null));
        // why authenticate the user again if he's just signed up or authenticated using oAuth2
        // instead return the response by yourself

        LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDto);
        // returning response to successHandler which will change it to json in HttpResponse response
    }

    public SignUpDto signup(LoginRequestDto loginRequestDto) {
        User user = signupInternal(loginRequestDto,Provider.EMAIL, null);
        return new SignUpDto(user.getUsername());
    }
}
