package com.marcus.fullcycle.video.catalog.admin.domain.validation.handler;

import java.util.List;

import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.Error;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.ValidationHandler;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(final Validation validation) {
        try {
            validation.validate();
        } catch (final Exception e) {
            throw DomainException.with(new Error(e.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
