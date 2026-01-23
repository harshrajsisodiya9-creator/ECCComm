package com.harsh.Ecom.Model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true)
    private String username;

    private String password;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    public Long getId(){return id;}

    public String getProviderId(){return providerId;}
    public void setProviderId(String providerId){this.providerId = providerId;}

    public Provider getProvider(){return provider;}
    public void setProvider(Provider provider){this.provider = provider;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}


    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

}
