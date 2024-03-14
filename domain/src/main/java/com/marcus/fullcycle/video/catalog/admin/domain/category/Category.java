package com.marcus.fullcycle.video.catalog.admin.domain.category;

import java.time.Instant;
import java.util.Objects;

import com.marcus.fullcycle.video.catalog.admin.domain.AggregateRoot;
import com.marcus.fullcycle.video.catalog.admin.domain.utils.InstantUtils;
import com.marcus.fullcycle.video.catalog.admin.domain.validation.ValidationHandler;

public class Category extends AggregateRoot<CategoryId> implements Cloneable {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryId id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id);
        this.name = name != null ? name.trim() : null;
        this.description = description;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "`createdAt` should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "`updatedAt` should not be null");
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final boolean isActive) {
        final var id = CategoryId.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if (getDeletedAt() == null)
            deletedAt = InstantUtils.now();

        active = false;
        updatedAt = InstantUtils.now();
        return this;
    }

    public Category activate() {
        deletedAt = null;
        active = true;
        updatedAt = InstantUtils.now();
        return this;
    }

    public Category update(
            final String name,
            final String description,
            final boolean isActive) {
        if (isActive)
            activate();
        else
            deactivate();

        this.name = name;
        this.description = description;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public Category clone() {
        // All fields of this class are Immutable, so there's no need to copy anything
        // manually.
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static Category with(
            final CategoryId id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Category(id, name, description, active, createdAt, updatedAt, deletedAt);
    }
}