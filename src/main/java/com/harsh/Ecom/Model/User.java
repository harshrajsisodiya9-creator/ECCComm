package com.harsh.Ecom.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    // by default fetch type is lazy, change it to eager,
    //EAGER is required because security needs roles immediately
    @ElementCollection(fetch = FetchType.EAGER)                // JPA stores a separate table with foreign key to parent entity, no primary key entity identity
    @Enumerated(EnumType.STRING)
    private Set<Role> role = new HashSet<>();

    public User(){};

    public Set<Role> getRole() {
        return role;
    }

    // Domain method – THIS is how roles should be added
    public void addRole(Role role) {
        this.role.add(role);
    }

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
