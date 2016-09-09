package com.epages.microservice.handson.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddressResource {

    public AddressResource(Address address) {
        this.city = address.getCity();
        this.email = address.getEmail();
        this.firstname = address.getFirstname();
        this.lastname = address.getLastname();
        this.postalCode = address.getPostalCode();
        this.street = address.getStreet();
        this.telephone = address.getTelephone();
    }

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String street;

    @NotNull
    private String city;

    @NotNull
    private String postalCode;

    @NotNull
    private String telephone;

    private String email;

    public Address toEntity() {
        return Address.builder()
                .city(city)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .postalCode(postalCode)
                .street(street)
                .telephone(telephone)
                .build();
    }
}
