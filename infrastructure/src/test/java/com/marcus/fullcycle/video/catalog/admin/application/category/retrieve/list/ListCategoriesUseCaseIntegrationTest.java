package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.list;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.marcus.fullcycle.video.catalog.admin.IntegrationTest;
import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategorySearchQuery;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;

@IntegrationTest
public class ListCategoriesUseCaseIntegrationTest {
    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                Category.newCategory("Movies", null, true),
                Category.newCategory("Netflix Originals", "Netflix authored titles", true),
                Category.newCategory("Amazon Originals", "Amazon authored titles", true),
                Category.newCategory("Documentaries", null, true),
                Category.newCategory("Sports", null, true),
                Category.newCategory("Kids", "Category for children", true),
                Category.newCategory("Series", null, true))
                .map(CategoryJpaEntity::from)
                .toList();

        jpaRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "asjhd asdojh";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemCount = 0;
        final var expectedTotal = 0;

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, result.items().size());
        Assertions.assertEquals(expectedPage, result.page());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
    }

    @ParameterizedTest
    @CsvSource({
            "mov,0,10,1,1,Movies",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "children,0,10,1,1,Kids",
            "Amazon,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemCount,
            final long expectedTotal,
            final String expectedCategoryName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, result.items().size());
        Assertions.assertEquals(expectedPage, result.page());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedCategoryName, result.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Movies",
            "createdAt,desc,0,10,7,7,Series",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemCount,
            final long expectedTotal,
            final String expectedCategoryName) {
        final var expectedTerms = "";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, result.items().size());
        Assertions.assertEquals(expectedPage, result.page());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedCategoryName, result.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentaries",
            "1,2,2,7,Kids;Movies",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemCount,
            final long expectedTotal,
            final String expectedCategoriesName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, result.items().size());
        Assertions.assertEquals(expectedPage, result.page());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = result.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
