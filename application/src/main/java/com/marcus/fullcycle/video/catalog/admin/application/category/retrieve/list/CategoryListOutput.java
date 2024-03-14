package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.list;

import java.time.Instant;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;

public record CategoryListOutput(
        CategoryId id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt) {
    public static CategoryListOutput from(final Category category) {
        return new CategoryListOutput(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getDeletedAt());
    }
}
