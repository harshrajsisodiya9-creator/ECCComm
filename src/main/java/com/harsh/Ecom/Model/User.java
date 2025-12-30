package com.harsh.Ecom.Model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true)                // every username must be unique
    private String username;

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}


    private String password;

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
