package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.list;

import java.util.Objects;

import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategorySearchQuery;
import com.marcus.fullcycle.video.catalog.admin.domain.pagination.Pagination;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryRepository repository;

    public DefaultListCategoriesUseCase(final CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery input) {
        return repository.findAll(input).map(CategoryListOutput::from);
    }
}
