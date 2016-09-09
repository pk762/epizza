package com.epages.microservice.handson.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Access(AccessType.FIELD)
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address implements Serializable {

    private static final long serialVersionUID = 2667019347732732455L;

    @Column(name = "FIRSTNAME", length = 255, nullable = false)
    private String firstname;

    @Column(name = "LASTNAME", length = 255, nullable = false)
    private String lastname;

    @Column(name = "STREET", length = 255, nullable = false)
    private String street;

    @Column(name = "CITY", length = 255, nullable = false)
    private String city;

    @Column(name = "POSTALCODE", length = 255, nullable = false)
    private String postalCode;

    @Column(name = "TELEPHONE", length = 255, nullable = false)
    private String telephone;

    @Column(name = "EMAIL", length = 255, nullable = true)
    private String email;
}
