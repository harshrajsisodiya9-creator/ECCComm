package com.harsh.Ecom.DTO;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String name;
    private String username;
    private String password;

    public SignUpRequestDto(String name,String username,String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
