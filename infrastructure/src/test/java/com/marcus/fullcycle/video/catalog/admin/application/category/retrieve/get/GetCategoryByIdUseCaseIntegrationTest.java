package com.marcus.fullcycle.video.catalog.admin.application.category.retrieve.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.marcus.fullcycle.video.catalog.admin.IntegrationTest;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

@IntegrationTest
public class GetCategoryByIdUseCaseIntegrationTest {
    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @SpyBean
    private CategoryRepository repository;

    @Test
    public void givenAValidId_whenCallGetCategoryById_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.getId();

        save(category);

        final var actualCategory = useCase.execute(expectedId.getValue());

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

        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenRepositoryThrowsError_shouldReturnException() {
        final var expectedId = CategoryId.from("123");
        final var expectedErrorMessage = "Repository error";

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(repository).findById(expectedId);

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... categories) {
        final var list = Arrays.stream(categories).map(CategoryJpaEntity::from).toList();
        jpaRepository.saveAllAndFlush(list);
    }
}
