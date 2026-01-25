package com.harsh.Ecom.DTO.SellerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SellerRequestDto {
    private Long id;
    private String name;
    private String email;
}
