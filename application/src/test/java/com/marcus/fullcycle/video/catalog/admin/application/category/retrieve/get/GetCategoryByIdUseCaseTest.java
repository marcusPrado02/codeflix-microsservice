package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.get;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

    @Test
    public void givenAValidId_whenCallGetCategoryById_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.getId();

        Mockito.when(repository.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallGetCategoryById_shouldReturnNotFound() {
        final var expectedId = CategoryId.from("123");
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        Mockito.when(repository.findById(expectedId)).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenRepositoryThrowsError_shouldReturnException() {
        final var expectedId = CategoryId.from("123");
        final var expectedErrorMessage = "Repository error";

        Mockito.when(repository.findById(expectedId)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
