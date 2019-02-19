package ru.zendal.config.lang;

import ru.zendal.config.lang.pack.CommandStorageLanguagePack;
import ru.zendal.config.lang.pack.CommandToLanguagePack;

/**
 * Interface for getting lang pack
 */
public interface LanguageProvider {
    /**
     * Get lang pack for command "storage"
     *
     * @return lang pack for command "storage"
     */
    CommandStorageLanguagePack getCommandStorageLanguage();

    /**
     * Get lang pack for command "to"
     *
     * @return lang pack for command "to"
     */
    CommandToLanguagePack getCommandToLanguage();


}
