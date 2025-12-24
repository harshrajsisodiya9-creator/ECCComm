package com.harsh.Ecom.DTO;

import com.harsh.Ecom.Model.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdMapper {

    @Mapping(source = "prodName", target = "name")
    @Mapping(source = "prodPrice", target = "price")
    ProdDto toDto(Product prod);

    @InheritInverseConfiguration            // name -> prodName , price -> prodPrice inverse if above mapping inheritance
    Product toEntity(ProdDto dto);
}