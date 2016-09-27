package epizza.order.checkout;

import com.google.common.base.MoreObjects;

import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import epizza.order.catalog.Pizza;

@Embeddable
@Access(AccessType.FIELD)
// FIXME introduce lombok
public class OrderItem {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Pizza pizza;

    @Basic(optional = false)
    private Integer quantity;

    @Basic(optional = false)
    private MonetaryAmount price;

    private OrderItem() {
        // JPA needs this
    }

    private OrderItem(Pizza pizza, Integer quantity, MonetaryAmount price) {
        this.pizza = pizza;
        this.quantity = quantity;
        this.price = price;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pizza", pizza)
                .add("quantity", quantity)
                .toString();
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public static class OrderItemBuilder {

        private Pizza pizza;

        private Integer quantity;

        private MonetaryAmount price;

        public OrderItemBuilder pizza(Pizza pizza) {
            this.pizza = pizza;
            return this;
        }

        public OrderItemBuilder price(MonetaryAmount price) {
            this.price = price;
            return this;
        }

        public OrderItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return price(pizza.getPrice().multiply(quantity));
        }

        public OrderItem build() {
            return new OrderItem(pizza, quantity, price);
        }
    }
}
