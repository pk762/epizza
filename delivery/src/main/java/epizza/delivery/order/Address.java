package epizza.delivery.order;

import lombok.Value;

@Value
public class Address {

    String firstname;
    String lastname;
    String street;
    String city;
    String postalCode;
    String telephone;

}
