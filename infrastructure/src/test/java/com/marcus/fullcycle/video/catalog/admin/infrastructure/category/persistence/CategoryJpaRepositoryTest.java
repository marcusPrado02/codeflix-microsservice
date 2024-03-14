package com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.marcus.fullcycle.video.catalog.admin.MySqlRepositoryTest;
import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;

@MySqlRepositoryTest
public class CategoryJpaRepositoryTest {
    @Autowired
    private CategoryJpaRepository jpaRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedPropertyName = "name";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> jpaRepository.save(entity));
        final var cause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedPropertyName, cause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, cause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> jpaRepository.save(entity));
        final var cause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedPropertyName, cause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, cause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> jpaRepository.save(entity));
        final var cause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedPropertyName, cause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, cause.getMessage());
    }
}
