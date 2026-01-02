package com.harsh.Ecom.DTO;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String jwt;
    private Long userId;

    public LoginResponseDto(){};
    public LoginResponseDto(String jwt, Long userId){this.jwt = jwt;this.userId=userId;}
}
