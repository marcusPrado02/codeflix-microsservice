package com.marcus.fullcycle.video.catalog.admin.domain.category;

import java.util.Optional;

import com.marcus.fullcycle.video.catalog.admin.domain.pagination.Pagination;

public interface CategoryRepository {
    Category create(Category category);

    Category update(Category category);

    Optional<Category> findById(CategoryId id);

    Pagination<Category> findAll(CategorySearchQuery query);

    void deleteById(CategoryId id);
}
