package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.model.User;
import com.viratech.cadastrocliente.model.dto.UserRequestDTO;
import com.viratech.cadastrocliente.model.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.
        ComponentModel.SPRING, uses = AddressMapper.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toListUserResponseDTO(List<User> userList);
}
