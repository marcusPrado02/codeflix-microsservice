package com.marcus.fullcycle.video.catalog.admin.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int page,
        int perPage,
        long total,
        List<T> items) {
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> newItems = items.stream().map(mapper).toList();
        return new Pagination<>(page, perPage, total, newItems);
    }
}
