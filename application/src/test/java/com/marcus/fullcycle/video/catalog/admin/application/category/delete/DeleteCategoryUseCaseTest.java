package com.marcus.fullcycle.video.catalog.admin.application.category.delete;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

    @Test
    public void givenAValidId_whenCallDeleteCategory_shouldBeOk() {
        final var category = Category.newCategory("Movies", "Most watched category", true);
        final var expectedId = category.getId();

        Mockito.doNothing().when(repository).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(repository, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryId.from("123");

        Mockito.doNothing().when(repository).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(repository, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenRepositoryThrowsError_shouldReturnException() {
        final var category = Category.newCategory("Movies", "Most watched category", true);
        final var expectedId = category.getId();

        Mockito.doThrow(new IllegalStateException("Repository error")).when(repository).deleteById(expectedId);

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(repository, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }
}
