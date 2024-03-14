package com.marcus.fullcycle.video.catalog.admin.application.category.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;
import com.marcus.fullcycle.video.catalog.admin.IntegrationTest;
import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

@IntegrationTest
public class DeleteCategoryUseCaseIntegrationTest {
    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @SpyBean
    private CategoryRepository repository;

    @Test
    public void givenAValidId_whenCallDeleteCategory_shouldBeOk() {
        final var category = Category.newCategory("Movies", "Most watched category", true);
        final var expectedId = category.getId();

        save(category);

        Assertions.assertEquals(1, jpaRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, jpaRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryId.from("123");

        Assertions.assertEquals(0, jpaRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, jpaRepository.count());
    }

    @Test
    public void givenAValidId_whenRepositoryThrowsError_shouldReturnException() {
        final var category = Category.newCategory("Movies", "Most watched category", true);
        final var expectedId = category.getId();

        Mockito.doThrow(new IllegalStateException("Repository error")).when(repository).deleteById(expectedId);

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(repository, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    private void save(final Category... categories) {
        final var list = Arrays.stream(categories).map(CategoryJpaEntity::from).toList();
        jpaRepository.saveAllAndFlush(list);
    }
}
