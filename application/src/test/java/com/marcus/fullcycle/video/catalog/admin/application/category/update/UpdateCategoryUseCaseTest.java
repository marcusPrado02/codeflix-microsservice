package com.marcus.fullcycle.video.catalog.admin.application.category.update;

import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void beforeEach() {
        Mockito.reset(repository);
    }

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movie", null, true);
        final var id = category.getId();

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(id)))
                .thenReturn(Optional.of(category.clone()));

        Mockito.when(repository.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(id));
        Mockito.verify(repository, Mockito.times(1))
                .update(Mockito.argThat(updatedCategory -> {
                    return Objects.equals(expectedName, updatedCategory.getName())
                            && Objects.equals(expectedDescription,
                                    updatedCategory.getDescription())
                            && Objects.equals(expectedIsActive, updatedCategory.isActive())
                            && Objects.equals(category.getId(), updatedCategory.getId())
                            && Objects.equals(category.getCreatedAt(),
                                    updatedCategory.getCreatedAt())
                            && updatedCategory.getUpdatedAt()
                                    .isAfter(category.getUpdatedAt())
                            && Objects.isNull(updatedCategory.getDeletedAt());
                }));
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

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(id)))
                .thenReturn(Optional.of(category.clone()));

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

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(id)))
                .thenReturn(Optional.of(category.clone()));

        Mockito.when(repository.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(repository, Mockito.times(1))
                .update(Mockito.argThat(updatedCategory -> {
                    return Objects.equals(expectedName, updatedCategory.getName())
                            && Objects.equals(expectedDescription,
                                    updatedCategory.getDescription())
                            && Objects.equals(expectedIsActive, updatedCategory.isActive())
                            && Objects.equals(category.getId(), updatedCategory.getId())
                            && Objects.equals(category.getCreatedAt(),
                                    updatedCategory.getCreatedAt())
                            && updatedCategory.getUpdatedAt()
                                    .isAfter(category.getUpdatedAt())
                            && Objects.nonNull(updatedCategory.getDeletedAt());
                }));
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

        final var command = UpdateCategoryCommand.with(
                id.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(id)))
                .thenReturn(Optional.of(category.clone()));

        Mockito.when(repository.update(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1))
                .update(Mockito.argThat(updatedCategory -> {
                    return Objects.equals(expectedName, updatedCategory.getName())
                            && Objects.equals(expectedDescription,
                                    updatedCategory.getDescription())
                            && Objects.equals(expectedIsActive, updatedCategory.isActive())
                            && Objects.equals(category.getId(), updatedCategory.getId())
                            && Objects.equals(category.getCreatedAt(),
                                    updatedCategory.getCreatedAt())
                            && updatedCategory.getUpdatedAt()
                                    .isAfter(category.getUpdatedAt())
                            && Objects.isNull(updatedCategory.getDeletedAt());
                }));
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

        Mockito.when(repository.findById(Mockito.eq(CategoryId.from(expectedId))))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

        Mockito.verify(repository, Mockito.times(1))
                .findById(Mockito.eq(CategoryId.from(expectedId)));
        Mockito.verify(repository, Mockito.times(0))
                .update(Mockito.any());
    }
}
