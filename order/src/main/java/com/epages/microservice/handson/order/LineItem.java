package com.epages.microservice.handson.order;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Access(AccessType.FIELD)
@Table(name = "LINE_ITEM")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id", "pizza", "amount" })
public class LineItem  {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

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
