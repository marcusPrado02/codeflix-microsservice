package com.marcus.fullcycle.video.catalog.admin.application.category.update;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;

public record UpdateCategoryOutput(
        CategoryId id) {
    public static UpdateCategoryOutput from(final Category category) {
        return new UpdateCategoryOutput(category.getId());
    }
}
