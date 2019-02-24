package ru.zendal.core.config.parser.engine;

/**
 * Interface for interact with JSON data
 */
public interface JsonEngineParser {

    /**
     * Load object type T
     *
     * @param json     string data json
     * @param classOfT type T
     * @param <T>      any object
     * @return T instance
     */
    <T> T fromJson(String json, Class<T> classOfT);

    /**
     * Convert Object to JSON (Text)
     *
     * @param src any object
     * @return JSON (Text)
     */
    String toJson(Object src);
}
