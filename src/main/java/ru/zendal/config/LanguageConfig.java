/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The type Language config.
 */
public class LanguageConfig {

    private Logger logger;

    private File langFile;

    private Map<String, String> metaData = new HashMap<>();

    /**
     * Instantiates a new Language config.
     *
     * @param file the file
     */
    public LanguageConfig(File file, Logger logger) {
        this.logger = logger;
        this.langFile = file;
        this.processFile();
    }

    public LanguageConfig(Logger logger) {
        this.logger = logger;
    }

    /**
     * Process language file
     */
    private void processFile() {
        try {
            boolean multiLine = false;
            StringBuilder buffer = new StringBuilder();
            String messageIndex = "";
            List<String> lines = Files.readLines(this.langFile, Charset.forName("UTF-8"));
            for (String line : lines) {
                String[] message = line.split("=");
                //Is multiLine mode
                if (multiLine) {
                    if (line.length() == 0) {
                        buffer.append("\n");
                    } else if (line.charAt(line.length() - 1) == '"') {
                        multiLine = false;
                        buffer.append(line);
                        metaData.put(messageIndex, buffer.deleteCharAt(buffer.length() - 1).toString());
                        buffer.setLength(0);
                    } else {
                        buffer.append(line).append("\n");
                    }
                } else if (message.length == 2) {
                    if (message[1].charAt(0) == '"') {
                        messageIndex = message[0];
                        multiLine = true;
                        buffer.append(message[1]);
                        buffer.deleteCharAt(0);
                    } else {
                        metaData.put(message[0], message[1]);
                    }
                }
            }

        } catch (IOException e) {
            this.logger.warning("Failed process language file, plugin can be working unstable. Check your lang File. (plugins/TradingPlatform/lang/*.lang)");
            e.printStackTrace();
        }
    }


    /**
     * Gets message.
     *
     * @param pathMessage the path message
     * @return the message
     */
    public AdaptiveMessage getMessage(String pathMessage) {

        if (langFile != null) {
            return new AdaptiveMessage(metaData.get(pathMessage));
        } else {
            return new AdaptiveMessage("is Test mode!");
        }
    }
}
