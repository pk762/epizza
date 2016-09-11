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

//    @JsonIgnore
    @ElementCollection
    @CollectionTable(name = "PIZZA_ORDER_ITEM")
    private List<LineItem> orderItems = Lists.newArrayList();

    @Basic(optional = true)
    private String comment;

    @Embedded
    private Address deliveryAddress;

    public void setOrderItems(List<LineItem> items) {
        this.orderItems.clear();
        items.forEach(this::addOrderItem);
    }

    public void addOrderItem(LineItem item) {
        this.orderItems.add(item);
    }

    public MonetaryAmount getTotalPrice() {
        return orderItems.stream()
                .map(LineItem::getPrice)
                .reduce(MonetaryAmount::add)
                .orElse(Money.of(0.0, "EUR"));
    }
}
