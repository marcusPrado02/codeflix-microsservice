package com.marcus.fullcycle.video.catalog.admin.infrastructure.category;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.marcus.fullcycle.video.catalog.admin.MySqlRepositoryTest;
import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategorySearchQuery;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;

@MySqlRepositoryTest
public class CategoryMySqlRepositoryTest {
    @Autowired
    private CategoryMySqlRepository mySqlRepository;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, jpaRepository.count());

        final var actualCategory = mySqlRepository.create(category);

        Assertions.assertEquals(1, jpaRepository.count());
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var persistedCategory = jpaRepository.findById(category.getId().getValue()).get();

        Assertions.assertEquals(1, jpaRepository.count());
        Assertions.assertEquals(category.getId().getValue(), persistedCategory.getId());
        Assertions.assertEquals(expectedName, persistedCategory.getName());
        Assertions.assertEquals(expectedDescription, persistedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, persistedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), persistedCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), persistedCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), persistedCategory.getDeletedAt());
        Assertions.assertNull(persistedCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movie", null, expectedIsActive);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, jpaRepository.count());

        final var invalidEntity = jpaRepository.findById(category.getId().getValue()).get();
        Assertions.assertEquals("Movie", invalidEntity.getName());
        Assertions.assertNull(invalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, invalidEntity.isActive());

        final var updatedCategory = category.clone().update(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = mySqlRepository.update(updatedCategory);

        Assertions.assertEquals(1, jpaRepository.count());
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(category.getUpdatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var persistedCategory = jpaRepository.findById(category.getId().getValue()).get();

        Assertions.assertEquals(1, jpaRepository.count());
        Assertions.assertEquals(category.getId().getValue(), persistedCategory.getId());
        Assertions.assertEquals(expectedName, persistedCategory.getName());
        Assertions.assertEquals(expectedDescription, persistedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, persistedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), persistedCategory.getCreatedAt());
        Assertions.assertTrue(persistedCategory.getUpdatedAt().isAfter(category.getUpdatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), persistedCategory.getDeletedAt());
        Assertions.assertNull(persistedCategory.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        final var category = Category.newCategory("Movies", null, true);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, jpaRepository.count());

        mySqlRepository.deleteById(category.getId());

        Assertions.assertEquals(0, jpaRepository.count());
    }

    @Test
    public void givenAnInvalidCategoryId_whenTryToDeleteIt_shouldIgnore() {
        Assertions.assertEquals(0, jpaRepository.count());

        mySqlRepository.deleteById(CategoryId.from("invalid"));

        Assertions.assertEquals(0, jpaRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, jpaRepository.count());

        final var actualCategory = mySqlRepository.findById(category.getId()).get();

        Assertions.assertEquals(1, jpaRepository.count());
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, jpaRepository.count());

        final var actualCategory = mySqlRepository.findById(CategoryId.from("empty"));

        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, jpaRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, jpaRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenMultiplePagesOfCategories_whenCallsFindAllWithEachPage_shouldReturnPaginated() {
        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, jpaRepository.count());

        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());

        expectedPage = 1;

        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies.getId(), actualResult.items().get(0).getId());

        expectedPage = 2;

        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, jpaRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMostWatchedAsTerms_whenCallsFindAllAndTermsMatchCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", "Most watched category", true);
        final var series = Category.newCategory("Series", "A watched category", true);
        final var documentaries = Category.newCategory("Documentaries", "The least watched category", true);

        Assertions.assertEquals(0, jpaRepository.count());

        jpaRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, jpaRepository.count());

        final var query = new CategorySearchQuery(0, 1, "MOST WATCHED", "name", "asc");
        final var actualResult = mySqlRepository.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.page());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies.getId(), actualResult.items().get(0).getId());
    }
}
