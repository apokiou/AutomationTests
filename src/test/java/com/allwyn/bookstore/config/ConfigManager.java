package com.allwyn.bookstore.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Centralised, read-only access to test configuration.
 *
 * <p>Resolution order for every key (highest priority first):
 * <ol>
 *     <li>JVM system property (e.g. {@code -DbaseUri=...})</li>
 *     <li>Environment variable (UPPER_SNAKE_CASE, e.g. {@code BASE_URI})</li>
 *     <li>{@code config.properties} on the classpath</li>
 * </ol>
 * This makes the same suite runnable locally and in CI without code changes.
 */
public final class ConfigManager {

    private static final Properties PROPERTIES = load();

    private ConfigManager() {
        // Utility class - no instances.
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream stream = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            properties.load(stream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
        return properties;
    }

    public static String get(String key) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem.trim();
        }
        String fromEnv = System.getenv(key.toUpperCase().replace('.', '_'));
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }
        String fromFile = PROPERTIES.getProperty(key);
        if (fromFile == null || fromFile.isBlank()) {
            throw new IllegalStateException("Missing configuration value for key: " + key);
        }
        return fromFile.trim();
    }

    public static String baseUri() {
        return get("baseUri");
    }

    public static String basePath() {
        return get("basePath");
    }

    public static int timeoutMs() {
        return Integer.parseInt(get("timeoutMs"));
    }
}
