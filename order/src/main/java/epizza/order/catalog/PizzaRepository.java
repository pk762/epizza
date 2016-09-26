package epizza.order.catalog;

import com.google.common.collect.Iterables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    default Optional<Pizza> findByUri(URI uri) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(uri).build();
        String lastPathSegment = Iterables.getLast(uriComponents.getPathSegments());
        return Optional.ofNullable(findOne(Long.valueOf(lastPathSegment)));
    }
}
