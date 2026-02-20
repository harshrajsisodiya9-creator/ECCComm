package com.harsh.Ecom.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDto {

    @NotBlank(message = "Shouldn't be blank")
    private String name;
    @NotBlank(message = "Shouldn't be blank")
    private String username;
    @Size(min=4, message = "Minimum 4 characters required")
    private String password;

    public SignUpRequestDto(String name,String username,String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
