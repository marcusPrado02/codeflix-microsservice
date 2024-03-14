package com.marcus.fullcycle.video.catalog.admin.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

/**
 * Helps to build pieces of query filtering logic.
 */
public final class SpecificationUtils {
    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.upper(root.get(prop)),
                        like(term));
    }

    private static String like(final String term) {
        return "%" + term.toUpperCase() + "%";
    }
}
