package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.model.entity.Address;
import com.viratech.cadastrocliente.dto.AddressDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface AddressMapper {

    Address toEntity(AddressDTO dto);

    AddressDTO toDTO(Address entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddress(AddressDTO dto, @MappingTarget Address address);
}
