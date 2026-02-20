package com.harsh.Ecom.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ProdDto implements Serializable {
    private int id;
    private int price;
    private String name;

    private String objectName;
    private Long objectSize;
    private String url;
}
