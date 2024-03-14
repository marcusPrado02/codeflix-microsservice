package com.marcus.fullcycle.video.catalog.admin.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.handler.ThrowsValidationHandler;

public class CategoryTest {
    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "`name` should not be null";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenATooShortName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "fi ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "`name` must be between 3 and 255 characters";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenATooLongName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "a".repeat(256);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "`name` must be between 3 and 255 characters";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnEmptyDescription_whenCallNewCategoryAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Movies";
        final var expectedDescription = "    ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenIsActiveIsFalse_whenCallNewCategoryAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Movies";
        final var expectedDescription = "    ";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var deactivatedCategory = category.deactivate();

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), deactivatedCategory.getId());
        Assertions.assertEquals(category.getName(), deactivatedCategory.getName());
        Assertions.assertEquals(category.getDescription(), deactivatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, deactivatedCategory.isActive());
        Assertions.assertEquals(createdAt, deactivatedCategory.getCreatedAt());
        Assertions.assertTrue(deactivatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(deactivatedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());

        final var activatedCategory = category.activate();

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), activatedCategory.getId());
        Assertions.assertEquals(category.getName(), activatedCategory.getName());
        Assertions.assertEquals(category.getDescription(), activatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, activatedCategory.isActive());
        Assertions.assertEquals(createdAt, activatedCategory.getCreatedAt());
        Assertions.assertTrue(activatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(activatedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallUpdateToInactivate_thenReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = false;

        final var category = Category.newCategory("Movie", "Most viewed categor", true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallUpdateToActivate_thenReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movie", "Most viewed categor", false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movies", "Most viewed categor", false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeletedAt());
    }
}
