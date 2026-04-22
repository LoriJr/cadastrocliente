package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.
        ComponentModel.SPRING, uses = AddressMapper.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toListUserResponseDTO(List<User> userList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserRequestDTO dto, @MappingTarget User user);
}
