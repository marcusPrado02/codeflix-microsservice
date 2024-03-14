package com.marcus.fullcycle.video.catalog.admin.application.category.update;

import java.util.Objects;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.Error;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.handler.Notification;

import io.vavr.API;
import io.vavr.control.Either;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
    private final CategoryRepository repository;

    public DefaultUpdateCategoryUseCase(CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand input) {
        final var category = repository.findById(CategoryId.from(input.id()))
                .orElseThrow(() -> notFound(input));
        final var notification = Notification.create();
        category.update(input.name(), input.description(), input.isActive())
                .validate(notification);
        return notification.hasError() ? API.Left(notification) : update(category);
    }

    private DomainException notFound(final UpdateCategoryCommand input) {
        return DomainException.with(new Error(
                "Category with ID %s was not found".formatted(input.id())));
    }

    private Either<Notification, UpdateCategoryOutput> update(Category category) {
        return API.Try(() -> repository.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
