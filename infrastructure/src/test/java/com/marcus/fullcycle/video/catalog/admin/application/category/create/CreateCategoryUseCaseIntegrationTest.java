package com.marcus.fullcycle.video.catalog.admin.application.category.create;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.junit.jupiter.api.Assertions;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;
import com.marcus.fullcycle.video.catalog.admin.IntegrationTest;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;

@IntegrationTest
public class CreateCategoryUseCaseIntegrationTest {
    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @SpyBean
    private CategoryRepository repository;

    @Test
    public void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, jpaRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Assertions.assertEquals(1, jpaRepository.count());

        final var category = jpaRepository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenCallCreateCategory_shouldReturnAnError() {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "`name` should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, jpaRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Assertions.assertEquals(0, jpaRepository.count());

        Mockito.verify(repository, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallCreateCategory_shouldReturnInactiveCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        Assertions.assertEquals(0, jpaRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Assertions.assertEquals(1, jpaRepository.count());

        final var category = jpaRepository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenRepositoryThrowsException_shouldReturnAnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;
        final var expectedErrorMessage = "Repository error";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(repository).create(Mockito.any());

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }
}
