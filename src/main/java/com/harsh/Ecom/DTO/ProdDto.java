package com.harsh.Ecom.DTO;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdDto {
    private int price;
    private String name;

    private String imageName;
    private String imageType;
    private byte[] imageData;
}
