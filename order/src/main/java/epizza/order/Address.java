package epizza.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Access(AccessType.FIELD)
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

    @Basic(optional = false)
    private String firstname;

    @Basic(optional = false)
    private String lastname;

    @Basic(optional = false)
    private String street;

    @Basic(optional = false)
    private String city;

    @Basic(optional = false)
    private String postalCode;

    @Basic(optional = true)
    private String telephone;

    @Basic(optional = true)
    private String email;
}
