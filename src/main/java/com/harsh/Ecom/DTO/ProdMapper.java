package com.harsh.Ecom.DTO;

import com.harsh.Ecom.Model.Product;
import org.mapstruct.*;


// de Dto se rhe hai response, le ProdResponse(response from user) se rhe hai
@Mapper(componentModel = "spring")
public interface ProdMapper {

    @Mapping(source = "prodName", target = "name")
    @Mapping(source = "prodPrice", target = "price")
    ProdDto toDto(Product prod);

    @Mapping(source = "prodName", target = "name")
    @Mapping(source = "prodPrice", target = "price")
    @Mapping(source = "prodId", target = "id")
    ProdResponseDto toResponseDto(Product prod);

    @InheritInverseConfiguration            // name -> prodName , price -> prodPrice inverse if above mapping inheritance
    Product toEntity(ProdResponseDto dto);

    // another method for patching entity
    // using the toEntity one can break JPA identity wipe fields and overwrite with nulls
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProdResponseDto prodResponseDto, @MappingTarget Product entity);
    // provide an entity which already exists so that all the corresponding changes gets into it and the remaining ones stay as it is and then that gets saved to the repo
}