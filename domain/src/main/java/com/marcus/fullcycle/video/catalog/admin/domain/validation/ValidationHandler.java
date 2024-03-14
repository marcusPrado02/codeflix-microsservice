package com.marcus.fullcycle.video.catalog.admin.domain.validation;

import java.util.List;

/**
 * Specifies how validation errors should be handled. One implementation of this
 * interface could for example throw an exception whenever a validation error
 * is appended.
 */
public interface ValidationHandler {
    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler handler);

    ValidationHandler validate(Validation validation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    public interface Validation {
        void validate();
    }
}
