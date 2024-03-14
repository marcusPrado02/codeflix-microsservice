package com.marcus.fullcycle.video.catalog.admin.application.category.create;

import com.marcus.fullcycle.video.catalog.admin.application.UseCase;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.handler.Notification;

import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
