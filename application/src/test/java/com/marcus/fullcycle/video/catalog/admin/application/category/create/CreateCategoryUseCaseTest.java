package com.marcus.fullcycle.video.catalog.admin.application.category.create;

import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void beforeEach() {
        Mockito.reset(repository);
    }

    @Test
    public void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(repository, Mockito.times(1))
                .create(Mockito.argThat(category -> {
                    return Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription,
                                    category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.nonNull(category.getId())
                            && Objects.nonNull(category.getCreatedAt())
                            && Objects.nonNull(category.getUpdatedAt())
                            && Objects.isNull(category.getDeletedAt());
                }));
    }

    @Test
    public void givenAnInvalidName_whenCallCreateCategory_shouldReturnAnError() {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "`name` should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Mockito.verify(repository, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallCreateCategory_shouldReturnInactiveCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(repository, Mockito.times(1))
                .create(Mockito.argThat(category -> {
                    return Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription,
                                    category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.nonNull(category.getId())
                            && Objects.nonNull(category.getCreatedAt())
                            && Objects.nonNull(category.getUpdatedAt())
                            && Objects.nonNull(category.getDeletedAt());
                }));
    }

    @Test
    public void givenAValidCommand_whenRepositoryThrowsException_shouldReturnAnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;
        final var expectedErrorMessage = "Repository error";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1))
                .create(Mockito.argThat(category -> {
                    return Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription,
                                    category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.nonNull(category.getId())
                            && Objects.nonNull(category.getCreatedAt())
                            && Objects.nonNull(category.getUpdatedAt())
                            && Objects.nonNull(category.getDeletedAt());
                }));
    }
}
