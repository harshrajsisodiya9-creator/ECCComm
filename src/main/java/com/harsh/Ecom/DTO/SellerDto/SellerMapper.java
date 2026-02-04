package com.harsh.Ecom.DTO.SellerDto;

import com.harsh.Ecom.Model.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    SellerResponseDto toResponse(Seller seller);
}
