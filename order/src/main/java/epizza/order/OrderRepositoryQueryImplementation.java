package epizza.order;

import com.google.common.annotations.VisibleForTesting;

@VisibleForTesting
enum OrderRepositoryQueryImplementation {
    NAMED_QUERY, // JPA Named Query
    CRITERIA_QUERY, // JPA Criteria Query
    QUERY_BY_SPECIFICATION, // Spring Data Query By Specification
    QUERY_BY_EXAMPLE, // Spring Data Query By Example
    QUERY_ANNOTATION, // Spring Data @Query Annotation
    NAMING_CONVENTION, //  Spring Data method naming convention
    QUERYDSL // Querydsl
}
