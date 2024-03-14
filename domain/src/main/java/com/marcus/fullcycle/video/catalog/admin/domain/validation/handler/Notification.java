package com.marcus.fullcycle.video.catalog.admin.domain.validation.handler;

import java.util.ArrayList;
import java.util.List;

import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.Error;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.ValidationHandler;

// Notification pattern. This class is used to store and return all validations errors instead of throwing exceptions.
public class Notification implements ValidationHandler {
    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error error) {
        return create().append(error);
    }

    public static Notification create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    @Override
    public Notification append(final Error error) {
        errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation validation) {
        try {
            validation.validate();
        } catch (final DomainException e) {
            errors.addAll(e.getErrors());
        } catch (final Throwable t) {
            errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return errors;
    }
}
