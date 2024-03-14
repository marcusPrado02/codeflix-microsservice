package com.marcus.fullcycle.video.catalog.admin;

import java.util.Collection;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class CleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        final var repositories = SpringExtension
                .getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values();
        cleanUp(repositories);
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
