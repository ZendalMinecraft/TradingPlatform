package ru.zendal.core.config.parser.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implement based on GSON library.
 */
public class GsonParser implements JsonEngineParser {

    /**
     * Instance of GSON
     */
    private final Gson gson;

    /**
     * Instantiates a new Gson parser.
     */
    public GsonParser() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    @Override
    public String toJson(Object src) {
        return gson.toJson(src);
    }
}
