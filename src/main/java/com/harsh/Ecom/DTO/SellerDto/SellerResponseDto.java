package com.harsh.Ecom.DTO.SellerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDto {
    private String jwt;
    private Long id;
}
