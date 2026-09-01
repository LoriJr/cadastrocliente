package com.viratech.cadastrocliente.model.builders;

import com.viratech.cadastrocliente.dto.AddressDTO;
import com.viratech.cadastrocliente.model.entity.Address;

public class AddressBuilder {

    private String zipCode;
    private String addressLine1;
    private String number;
    private String addressLine2;
    private String neighborhood;
    private String city;
    private String state;
    private AddressBuilder(){}

    public static AddressBuilder aAddress(){
        AddressBuilder builder = new AddressBuilder();
        setDefaultValues(builder);
        return builder;
    }

    private static void setDefaultValues(AddressBuilder builder) {
        builder.zipCode = "09781220";
        builder.addressLine1 = "Rua Tiradentes";
        builder.number = "100";
        builder.addressLine2 = "bloco 1 ap 1";
        builder.neighborhood = "iraja";
        builder.city = "sao bernardo do campo";
        builder.state = "sp";
    }

    public AddressBuilder getZipCode(String param){
        zipCode = param;
        return this;
    }

    public AddressBuilder getAddressLine1(String param){
        addressLine1 = param;
        return this;
    }

    public AddressBuilder getNumber(String param){
        number = param;
        return this;
    }

    public AddressBuilder getAddressLine2(String param){
        addressLine2 = param;
        return this;
    }

    public AddressBuilder getNeighborhood(String param){
        neighborhood = param;
        return this;
    }

    public AddressBuilder getCity(String param){
        city = param;
        return this;
    }

    public AddressBuilder getState(String param){
        state = param;
        return this;
    }

    public Address now() {
        return new Address(
                zipCode,
                addressLine1,
                number,
                addressLine2,
                neighborhood,
                city,
                state);
    }

    public AddressDTO nowDTO() {
        return new AddressDTO(
                zipCode,
                addressLine1,
                number,
                addressLine2,
                neighborhood,
                city,
                state);
    }


}
