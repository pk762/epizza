package epizza.order;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.hateoas.Identifiable;

import com.google.common.collect.Sets;

@Entity
@Access(AccessType.FIELD)
@Table(name = "PIZZA")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id", "name" })
public class Pizza implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Basic
    @Column(name = "NAME", length = 75, nullable = false, unique = true)
    private String name;

    @Basic
    @Column(name = "DESCRIPTION", length = 255, nullable = true)
    private String description;

    @Basic
    @Column(name = "IMAGE_URL", length = 255, nullable = false)
    private String imageUrl;

    @Basic
    @Column(name = "PRICE")
    private MonetaryAmount price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PIZZA_TOPPINGS", joinColumns = @JoinColumn(name = "PIZZA_ID", nullable = false), uniqueConstraints = @UniqueConstraint(name = "U_PIZZA_TOPPINGS_PIZZA_ID_TOPPING", columnNames = {
            "PIZZA_ID", "TOPPING" }))
    @Enumerated(EnumType.STRING)
    @Column(name = "TOPPING", length = 30, nullable = false)
    @Singular
    private Set<Topping> toppings = Sets.newHashSet();
}
