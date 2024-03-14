package com.marcus.fullcycle.video.catalog.admin.infrastructure.category;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.marcus.fullcycle.video.catalog.admin.domain.category.Category;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryId;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategoryRepository;
import com.marcus.fullcycle.video.catalog.admin.domain.category.CategorySearchQuery;
import com.marcus.fullcycle.video.catalog.admin.domain.pagination.Pagination;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.category.persistence.CategoryJpaRepository;
import com.marcus.fullcycle.video.catalog.admin.infrastructure.utils.SpecificationUtils;

/**
 * Implements the repository contract defined by the domain of the application.
 */
@Service
public class CategoryMySqlRepository implements CategoryRepository {
    private final CategoryJpaRepository repository;

    public CategoryMySqlRepository(final CategoryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public Category update(final Category category) {
        return save(category);
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return repository.findById(id.getValue()).map(CategoryJpaEntity::toDomainCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<CategoryJpaEntity>like("name", str)
                        .or(SpecificationUtils.like("description", str)))
                .orElse(null);

        final var pageResult = repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toDomainCategory).toList());
    }

    @Override
    public void deleteById(CategoryId id) {
        final var idValue = id.getValue();
        if (repository.existsById(idValue)) {
            repository.deleteById(idValue);
        }
    }

    private Category save(final Category category) {
        return repository.save(CategoryJpaEntity.from(category)).toDomainCategory();
    }
}
