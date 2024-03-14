package com.marcus.fullcycle.video.catalog.admin.domain.category;

import com.marcus.fullcycle.video.catalog.admin.domain.validation.Error;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.ValidationHandler;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;

    private final Category category;

    public CategoryValidator(Category category, ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        String name = category.getName();
        if (name == null)
            validationHandler().append(new Error("`name` should not be null"));
        else if (name.isEmpty())
            validationHandler().append(new Error("`name` should not be empty"));
        else if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH)
            validationHandler().append(new Error("`name` must be between 3 and 255 characters"));
    }
}
