package com.marcus.fullcycle.video.catalog.admin.application.category.create;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;

public record CreateCategoryOutput(String id) {
    public static CreateCategoryOutput from(final String id) {
        return new CreateCategoryOutput(id);
    }

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId().getValue());
    }
}
