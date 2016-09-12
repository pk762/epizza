package epizza.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(of={"pizza", "amount"})
public class OrderItem {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Pizza pizza;

    @Basic(optional = false)
    private Integer amount;

    @Basic(optional = false)
    private MonetaryAmount price;

    public static class OrderItemBuilder {
        public OrderItemBuilder amount(Integer amount) {
            this.amount = amount;
            return price(pizza.getPrice().multiply(amount));
        }
    }
}
