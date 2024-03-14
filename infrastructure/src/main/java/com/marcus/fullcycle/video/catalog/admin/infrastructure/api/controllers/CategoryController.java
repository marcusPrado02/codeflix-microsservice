package com.marcus.fullcycle.video.catalog.admin.infrastructure.api.controllers;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.marcus.fullcycle.video.catalog.admin.application.category.create.CreateCategoryCommand;
import com.marcus.fullcycle.video.catalog.admin.application.category.create.CreateCategoryOutput;
import com.marcus.fullcycle.video.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.marcus.fullcycle.video.catalog.admin.domain.pagination.Pagination;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.handler.Notification;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.api.CategoryApi;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.models.CreateCategoryRequest;

@RestController
public class CategoryController implements CategoryApi {
    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var command = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = notification -> ResponseEntity.unprocessableEntity()
                .body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity
                .created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(command)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
