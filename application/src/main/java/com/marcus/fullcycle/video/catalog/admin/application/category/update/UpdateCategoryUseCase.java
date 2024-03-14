package com.marcus.fullcycle.video.catalog.admin.application.category.update;

import com.marcus.fullcycle.video.catalog.admin.application.UseCase;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.handler.Notification;

import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
