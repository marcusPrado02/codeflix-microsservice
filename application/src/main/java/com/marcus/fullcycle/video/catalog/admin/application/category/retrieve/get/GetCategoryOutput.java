package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.get;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;

import java.time.Instant;

public record GetCategoryOutput(
        CategoryId id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GetCategoryOutput from(final Category category) {
        return new GetCategoryOutput(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}
