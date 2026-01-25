package com.harsh.Ecom.Security;

import com.harsh.Ecom.DTO.*;
import com.harsh.Ecom.Model.Customer;
import com.harsh.Ecom.Model.Provider;
import com.harsh.Ecom.Model.Role;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.CustomerRepo;
import com.harsh.Ecom.Repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService implements OAuth2Handler{

    private final LoginMapper mapper;
    private final CustomerRepo customerRepo;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final PasswordEncoder encoder;

    public User signupInternal(SignUpRequestDto signUpRequestDto, Provider provider, String providerId){
        User user = userRepository.findByUsername(signUpRequestDto.getUsername()).orElse(null);
        // we don't need an already used username hence we throw a null, if there is already a username then we have a problem
        if(user != null){
            throw new IllegalArgumentException("Username already taken");
        }
        user = mapper.toUserFromSignUpRequest(signUpRequestDto);
        user.setProviderId(providerId);
        user.setProvider(provider);
        user.addRole(Role.CUSTOMER);
        if(provider == Provider.EMAIL){
            user.setPassword(encoder.encode(user.getPassword()));
        }

        userRepository.save(user);

        Customer customer = Customer.builder()
                        .name(signUpRequestDto.getName())
                        .email(signUpRequestDto.getUsername())
                        .user(user)
                        .build();

        customerRepo.save(customer);
        return user;
    }

    @Override // upar oAuthHandler ka interface(self made(see problemsfaced)) hai isliye override dala hai
    @Transactional
    public ResponseEntity<LoginResponseDto> oAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        //save and declare username(if doesn't exist),fetch providerId(unique id of the user provided by the provider(google,github etc)) and providerType
        Provider provider = authUtil.getProviderFromRegistrationId(registrationId);
        String providerId = authUtil.getProviderId(oAuth2User,registrationId,provider);
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByProviderIdAndProvider(providerId, provider).orElse(null);

        String email = oAuth2User.getAttribute("email");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        if(user == null && emailUser == null){
            String username = authUtil.getUsernameFromOAuth2User(oAuth2User,email,provider);
            user = signupInternal(new SignUpRequestDto(name,email,null), provider, providerId); // sign up the user
        } else if (user  != null) {
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                //userRepository.save(user);
                // save is not needed cause we are using transcational and it will automatically do it
                // we loaded the user from the database eariler from find by provider so @Transactional tells JPA to track the user and it will automatically save the
                // user once the transaction is complete
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

    public SignUpDto signup(SignUpRequestDto signUpRequestDto) {
        User user = signupInternal(signUpRequestDto,Provider.EMAIL, null);
        return new SignUpDto(user.getUsername());
    }
}