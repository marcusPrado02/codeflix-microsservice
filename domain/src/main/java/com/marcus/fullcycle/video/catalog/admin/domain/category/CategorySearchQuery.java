package com.marcus.fullcycle.video.catalog.admin.domain.category;

public record CategorySearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction) {
}
