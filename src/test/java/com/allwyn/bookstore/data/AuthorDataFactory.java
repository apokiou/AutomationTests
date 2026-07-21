package com.allwyn.bookstore.data;

import com.allwyn.bookstore.models.Author;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Produces Author test fixtures.
 */
public final class AuthorDataFactory {

    private AuthorDataFactory() {
    }

    public static Author validAuthor() {
        int unique = ThreadLocalRandom.current().nextInt(1_000, 1_000_000);
        return Author.builder()
                .id(unique)
                .idBook(ThreadLocalRandom.current().nextInt(1, 200))
                .firstName("First" + unique)
                .lastName("Last" + unique)
                .build();
    }
}
