package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.model.Address;
import com.viratech.cadastrocliente.model.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface AddressMapper {

    Address toEntity(AddressDTO dto);

    AddressDTO toDTO(Address entity);
}
