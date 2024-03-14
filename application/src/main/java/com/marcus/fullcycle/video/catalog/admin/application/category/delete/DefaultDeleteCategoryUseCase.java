package com.marcus.fullcycle.video.catalog.admin.application.category.delete;

import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {
    private final CategoryRepository repository;

    public DefaultDeleteCategoryUseCase(final CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void execute(final String id) {
        repository.deleteById(CategoryId.from(id));
    }
}
