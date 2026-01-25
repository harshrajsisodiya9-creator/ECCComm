package com.harsh.Ecom.DTO;


public class SignUpDto {
    private String username;

    public SignUpDto(){};
    public SignUpDto(String username){this.username = username;}

    public String getUsername(){return username;}
}
