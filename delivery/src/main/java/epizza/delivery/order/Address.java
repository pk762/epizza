package epizza.delivery.order;

import lombok.Data;

@Data
public class Address {

    private String firstname;
    private String lastname;
    private String street;
    private String city;
    private String postalCode;
    private String telephone;

}
