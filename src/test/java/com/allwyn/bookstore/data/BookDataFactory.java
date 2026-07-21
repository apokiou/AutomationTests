package com.allwyn.bookstore.data;

import com.allwyn.bookstore.models.Book;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Produces Book test fixtures. Randomising the fields keeps tests independent
 * and avoids collisions when they run in parallel.
 */
public final class BookDataFactory {

    private BookDataFactory() {
    }

    public static Book validBook() {
        int unique = ThreadLocalRandom.current().nextInt(1_000, 1_000_000);
        return Book.builder()
                .id(unique)
                .title("Automation Book " + unique)
                .description("Created by the automated suite")
                .pageCount(ThreadLocalRandom.current().nextInt(50, 900))
                .excerpt("Excerpt for book " + unique)
                .publishDate(OffsetDateTime.now(ZoneOffset.UTC).toString())
                .build();
    }

    /** A book with only the mandatory-ish fields, used to test minimal payloads. */
    public static Book minimalBook() {
        int unique = ThreadLocalRandom.current().nextInt(1_000, 1_000_000);
        return Book.builder()
                .id(unique)
                .title("Minimal " + unique)
                .pageCount(1)
                .build();
    }
}
