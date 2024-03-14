package com.marcus.fullcycle.video.catalog.admin.application.category.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;

import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;
import com.marcus.fullcycle.video.catalog.admin.IntegrationTest;
import java.util.Arrays;

@IntegrationTest
public class UpdateCategoryUseCaseIntegrationTest {
    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @SpyBean
    private CategoryRepository repository;

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movie", null, true);
        final var id = category.getId();

        save(category);
        Assertions.assertEquals(1, jpaRepository.count());

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var output = useCase.execute(command).get();

        Assertions.assertEquals(1, jpaRepository.count());
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var updatedCategory = jpaRepository.findById(output.id().getValue()).get();

        Assertions.assertEquals(id.getValue(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(category.getUpdatedAt()));
        Assertions.assertNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenCallUpdateCategory_shouldReturnAnError() {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "`name` should not be null";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory("Movie", null, true);
        final var id = category.getId();

        save(category);
        Assertions.assertEquals(1, jpaRepository.count());

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Mockito.verify(repository, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallUpdateCategory_shouldReturnInactiveCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, true);
        final var id = category.getId();

        save(category);
        Assertions.assertEquals(1, jpaRepository.count());

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var updatedCategory = jpaRepository.findById(output.id().getValue()).get();

        Assertions.assertEquals(id.getValue(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(category.getUpdatedAt()));
        Assertions.assertNotNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenRepositoryThrowsException_shouldReturnAnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Repository error";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var id = category.getId();

        save(category);
        Assertions.assertEquals(1, jpaRepository.count());

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(repository).update(Mockito.any());

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        final var untouchedCategory = jpaRepository.findById(id.getValue()).get();

        Assertions.assertEquals(id.getValue(), untouchedCategory.getId());
        Assertions.assertEquals(expectedName, untouchedCategory.getName());
        Assertions.assertEquals(expectedDescription, untouchedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, untouchedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), untouchedCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), untouchedCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), untouchedCategory.getDeletedAt());
        Assertions.assertNull(untouchedCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidId_whenCallUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var exception = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    private void save(final Category... categories) {
        final var list = Arrays.stream(categories).map(CategoryJpaEntity::from).toList();
        jpaRepository.saveAllAndFlush(list);
    }
}
