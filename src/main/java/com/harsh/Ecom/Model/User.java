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

    @JoinColumn(unique = true)
    private String username;

    private String password;

    public Long getId(){return id;}

    public String getUsername(){return username;}          // both getusername and get paswords are there in UserDetails
    public void setUsername(String username){this.username = username;}


    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}


    @Override // you will get error saying implement this methods beacuse its an abstract method defined in the interface class UserDetails which needs to be implemented in USer Class
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
