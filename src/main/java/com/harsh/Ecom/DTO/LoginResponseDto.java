package com.harsh.Ecom.DTO;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String jwt;
    private Long userId;
}
