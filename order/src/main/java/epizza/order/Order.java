package epizza.order;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.javamoney.moneta.Money;
import org.springframework.hateoas.Identifiable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@Entity
@Access(AccessType.FIELD)
@Table(name = "PIZZA_ORDER")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id", "orderItems" })
public class Order implements Identifiable<Long> {

    public static final Money DEFAULT_PRICE = Money.of(0.0, "EUR");
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Basic(optional = false)
    private LocalDateTime orderedAt;

    @Enumerated(value = EnumType.STRING)
    @Basic(optional = false)
    private OrderStatus status = OrderStatus.NEW;

    @Column(name = "ETD")
    private LocalDateTime estimatedTimeOfDelivery;

    @ElementCollection
    @CollectionTable(name = "PIZZA_ORDER_ITEM")
    private List<OrderItem> orderItems = Lists.newArrayList();

    private String comment;

    @Basic(optional = false)
    private MonetaryAmount totalPrice = DEFAULT_PRICE;

    @Embedded
    private Address deliveryAddress;

    public List<OrderItem> getOrderItems() {
        return ImmutableList.copyOf(orderItems);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems.clear();
        this.totalPrice = DEFAULT_PRICE;
        orderItems.forEach(this::addOrderItem);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        this.totalPrice = this.totalPrice.add(orderItem.getPrice());
    }
}
