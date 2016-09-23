package epizza.order;

import static java.util.Collections.emptyList;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryWithCriteraQuery {

    private final EntityManager entityManager;

    @Override
    public Page<Order> findUnassignedOrders(Pageable pageable) {
        CriteriaQuery<Order> criteria = entityManager.getCriteriaBuilder().createQuery(Order.class);
        Root<Order> orders = criteria.from(Order.class);
        Path<String> deliveryBoy = orders.get("deliveryBoy");

        criteria.select(orders).where(isNull(deliveryBoy));

        TypedQuery<Order> query = entityManager.createQuery(criteria);
        return readPage(query, pageable);
    }

    @Override
    public Long countUnassignedOrders() {
        CriteriaQuery<Long> criteria = entityManager.getCriteriaBuilder().createQuery(Long.class);
        Root<Order> orders = criteria.from(Order.class);
        Path<String> deliveryBoy = orders.get("deliveryBoy");

        criteria.select(count(orders)).where(isNull(deliveryBoy));

        TypedQuery<Long> query = entityManager.createQuery(criteria);
        return query.getSingleResult();
    }

    private Page<Order> readPage(TypedQuery<Order> query, Pageable pageable) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = countUnassignedOrders();
        List<Order> content = total > pageable.getOffset() ? query.getResultList() : emptyList();
        return new PageImpl<>(content, pageable, total);
    }

    private Predicate isNull(Path<?> path) {
        return entityManager.getCriteriaBuilder().isNull(path);
    }

    private Expression<Long> count(Root<?> root) {
        return entityManager.getCriteriaBuilder().count(root);
    }
}
