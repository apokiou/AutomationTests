package com.allwyn.bookstore.config;

/**
 * Single source of truth for API resource paths.
 * Keeping these here means a path change touches exactly one place.
 */
public final class Endpoints {

    public static final String BOOKS = "/Books";
    public static final String BOOK_BY_ID = "/Books/{id}";

    public static final String AUTHORS = "/Authors";
    public static final String AUTHOR_BY_ID = "/Authors/{id}";

    private Endpoints() {
        // Constants holder - no instances.
    }
}
