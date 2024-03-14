package com.marcus.fullcycle.video.catalog.admin.domain.category;

import java.util.Objects;
import java.util.UUID;

import com.marcus.fullcycle.video.catalog.admin.domain.Identifier;

public class CategoryId extends Identifier {
    private final String value;

    private CategoryId(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoryId unique() {
        return CategoryId.from(UUID.randomUUID());
    }

    public static CategoryId from(final String anId) {
        return new CategoryId(anId);
    }

    public static CategoryId from(final UUID anId) {
        return new CategoryId(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoryId other = (CategoryId) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
