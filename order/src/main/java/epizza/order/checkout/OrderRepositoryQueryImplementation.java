package epizza.order.checkout;

import com.google.common.annotations.VisibleForTesting;

@VisibleForTesting
enum OrderRepositoryQueryImplementation {
    NAMED_QUERY // JPA Named Query
// SCHNIPP
    , NAMING_CONVENTION //  Spring Data method naming convention
    , QUERY_ANNOTATION // Spring Data @Query Annotation
    , QUERYDSL // Querydsl
    , CRITERIA_QUERY // JPA Criteria Query
    , QUERY_BY_SPECIFICATION // Spring Data Query By Specification
    , QUERY_BY_EXAMPLE // Spring Data Query By Example
// SCHNAPP
}
