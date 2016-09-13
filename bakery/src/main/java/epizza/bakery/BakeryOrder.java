package epizza.bakery;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BAKERY_ORDER")
@Getter
@Setter
public class BakeryOrder {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Basic
    @Column(unique = true, nullable = false)
    private URI orderLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BakeryOrderState bakeryOrderState;
}
