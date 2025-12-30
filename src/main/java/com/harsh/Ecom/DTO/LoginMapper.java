package com.harsh.Ecom.DTO;

import com.harsh.Ecom.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.transform.Source;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    @Mapping(source = "id", target = "userId")
    LoginResponseDto toResponse(User user);

    User toUser(LoginRequestDto dto);
}
