package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.list;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategorySearchQuery;
import com.marcus.fullcycle.video.catalog.admin.domain.pagination.Pagination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {
    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

    @Test
    public void givenAValidQuery_whenCallListCategories_thenShouldReturnCategories() {
        final var categories = List.of(
                Category.newCategory("Movies", null, true),
                Category.newCategory("Series", null, true));

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(repository.findAll(Mockito.eq(query))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(repository.findAll(Mockito.eq(query))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_repositoryThrowsError_thenReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Repository error";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        Mockito.when(repository.findAll(Mockito.eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
