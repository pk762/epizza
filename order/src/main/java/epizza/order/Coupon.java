package epizza.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@Builder
@ToString(of = "code")
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @Length(min=1, max = 12)
    private String code;

    private MonetaryAmount value;
}
