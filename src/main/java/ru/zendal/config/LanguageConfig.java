package ru.zendal.config;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class LanguageConfig {


    private File langFile;

    private Map<String, String> metaData = new HashMap<>();

    public LanguageConfig(File file) {
        this.langFile = file;
        this.processFile();
    }

    private void processFile() {
        try {
            Files.readLines(this.langFile, Charset.forName("UTF-8")).forEach(data -> {
                String[] message = data.split("=");
                metaData.put(message[0], message[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public AdaptiveMessage getMessage(String pathMessage) {
        return new AdaptiveMessage(metaData.get(pathMessage));
    }
}
