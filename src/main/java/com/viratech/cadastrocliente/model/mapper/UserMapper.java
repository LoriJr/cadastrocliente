package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.dto.UserExportDTO;
import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.entity.User;
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

    @Mapping(target = "addressLine1", source = "address.addressLine1")
    @Mapping(target = "number", source = "address.number")
    @Mapping(target = "addressLine2", source = "address.addressLine2")
    @Mapping(target = "neighborhood", source = "address.neighborhood")
    @Mapping(target = "zipCode", source = "address.zipCode")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "state", source = "address.state")
    UserExportDTO toExportDTO(User user);
}
