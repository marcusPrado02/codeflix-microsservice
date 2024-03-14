package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.get;

import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private final CategoryRepository repository;

    public DefaultGetCategoryByIdUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public GetCategoryOutput execute(String id) {
        final var categoryId = CategoryId.from(id);
        return repository.findById(categoryId).map(GetCategoryOutput::from).orElseThrow(notFound(categoryId));
    }

    private Supplier<DomainException> notFound(final CategoryId id) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(id.getValue())));
    }
}
