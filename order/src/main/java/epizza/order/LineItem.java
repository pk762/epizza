package epizza.order;

import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Access(AccessType.FIELD)
@Table(name = "LINE_ITEM")
@Getter
@Setter
@NoArgsConstructor
public class LineItem  {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "PIZZA_ID", nullable = false)
    private Pizza pizza;

    @Basic
    @Column(name = "AMOUNT", nullable = false)
    private Integer amount;

    @Basic
    @Column(name = "PRICE", length = 20, nullable = false)
    private MonetaryAmount price;

    public LineItem(Pizza pizza) {
        this(pizza, 1);
    }

    public LineItem(Pizza pizza, Integer amount) {
        this.pizza = pizza;
        this.amount = amount;
        this.price = pizza.getPrice().multiply(amount);
    }
}
