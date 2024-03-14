package com.marcus.fullcycle.video.catalog.admin.application.category.create;

import java.util.Objects;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.handler.Notification;

import io.vavr.API;
import io.vavr.control.Either;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryRepository repository;

    public DefaultCreateCategoryUseCase(CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand input) {
        final var notification = Notification.create();
        final var category = Category.newCategory(input.name(), input.description(), input.isActive());
        category.validate(notification);
        return notification.hasError() ? API.Left(notification) : create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(Category category) {
        return API.Try(() -> repository.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
