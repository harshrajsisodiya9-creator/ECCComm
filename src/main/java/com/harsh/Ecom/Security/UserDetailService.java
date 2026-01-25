package com.harsh.Ecom.Security;

import com.harsh.Ecom.Model.Role;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository){this.userRepository = userRepository;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();

        return org.springframework.security.core.userdetails.User         // using full package name as we have already colliding name User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();                                              // this will return user which is the class of spring security not
    }                                                                  // not our own class

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Set<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("Role_" + role)) // writing Role_Customer etc to the DB as Role_ * String  so thats why this type of syntax
                .toList();                                                              // Role enum → ROLE_* string in DB
    }
}
