package ru.zendal.core.config.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zendal.core.config.parser.engine.JsonEngineParser;
import ru.zendal.core.config.parser.exception.TradingPlatformConfigurationParserException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * Configuration parser for trading platform
 *
 * @param <T> Type of configuration object
 */
public abstract class TradingPlatformConfigurationParser<T> {

    private final Logger log = LoggerFactory.getLogger(TradingPlatformConfigurationParser.class);

    /**
     * Instance of JSON parser for work with JSON.
     */
    private JsonEngineParser jsonEngineParser;

    /**
     * Instantiates a new Trading platform configuration parser.
     *
     * @param jsonEngineParser the json engine parser
     */
    TradingPlatformConfigurationParser(JsonEngineParser jsonEngineParser) {
        this.jsonEngineParser = jsonEngineParser;
    }


    /**
     * Gets configuration.
     *
     * @return the configuration
     */
    public abstract T getConfiguration();

    /**
     * Gets name configuration file.
     *
     * @return the name configuration file
     */
    protected abstract String getNameConfigurationFile();


    /**
     * Load configuration t.
     *
     * @param file         the file
     * @param defaultValue the default value
     * @return the t
     * @throws TradingPlatformConfigurationParserException the trading platform configuration parser exception
     */
    T loadConfiguration(File file, T defaultValue) throws TradingPlatformConfigurationParserException {
        try {
            return this.initializeConfiguration(file, defaultValue);
        } catch (IOException e) {
            log.error("Error load configuration: {}.", file.toString(), e);
            throw new TradingPlatformConfigurationParserException("Can't load configuration, file: " + file.toString(), e);
        }
    }

    T saveConfiguration(File file, T value) throws TradingPlatformConfigurationParserException {
        try {
            file.delete();
            return this.initializeConfiguration(file, value);
        } catch (IOException e) {
            log.error("Error load configuration: {}.", file.toString(), e);
            throw new TradingPlatformConfigurationParserException("Can't load configuration, file: " + file.toString(), e);
        }
    }

    /**
     * Initialize configuration file if not exists, else just get
     *
     * @param file
     * @param defaultValue
     * @return
     * @throws IOException
     */
    private T initializeConfiguration(File file, T defaultValue) throws IOException {
        if (file.exists()) {
            return this.prepareExistsFile(file, defaultValue.getClass());
        } else {
            return this.prepareNewFile(file, defaultValue);
        }
    }


    /**
     * Prepare exists file and generate trading platform configuration
     *
     * @param file File to JSON file configuration
     * @return trading platform configuration object
     * @throws IOException when can't read file
     */
    private T prepareExistsFile(File file, Class clazz) throws IOException {
        return (T) jsonEngineParser.fromJson(this.readFromFile(file), clazz);
    }

    /**
     * Generate new file and put into this file default configuration
     *
     * @param file File to JSON file configuration
     * @return trading platform configuration object
     * @throws IOException when can't create or write into file
     */
    private T prepareNewFile(File file, T defaultValue) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        this.writeIntoFile(file, jsonEngineParser.toJson(defaultValue));
        return defaultValue;

    }

    /**
     * Read data from file
     *
     * @param file target file
     * @return content of file
     * @throws IOException when can't read from file
     */
    private String readFromFile(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.readAllLines(file.toPath()).forEach(builder::append);
        return builder.toString();
    }

    /**
     * Write data to file
     *
     * @param file target file
     * @param data data for write
     * @throws IOException when can't write into file
     */
    private void writeIntoFile(File file, String data) throws IOException {
        PrintWriter writer = new PrintWriter(file.getPath(), "UTF-8");
        for (String line : data.split("\n")) {
            writer.println(line);
        }
        writer.close();
    }


}
