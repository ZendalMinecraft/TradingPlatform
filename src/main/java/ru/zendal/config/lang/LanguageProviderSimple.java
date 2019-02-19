package ru.zendal.config.lang;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import ru.zendal.config.LanguageConfig;
import ru.zendal.config.lang.pack.CommandStorageLanguagePack;
import ru.zendal.config.lang.pack.CommandToLanguagePack;

/**
 * The type Language provider simple.
 */
public class LanguageProviderSimple implements LanguageProvider {

    /**
     * Resource for get row messages
     */
    private LanguageConfig languageConfig;

    /**
     * Instantiates a new Language provider simple.
     *
     * @param languageConfig the language config
     */
    @Inject
    public LanguageProviderSimple(LanguageConfig languageConfig) {
        this.languageConfig = languageConfig;
    }

    @Override
    public CommandStorageLanguagePack getCommandStorageLanguage() {
        return new CommandStorageLanguagePack() {
            @Override
            public String getErrorExecutorNotPlayer() {
                return languageConfig.getMessage("command.storage.error.executorNotPlayer")
                        .toString();
            }

            @Override
            public String getMessageIsClear() {
                return languageConfig.getMessage("command.storage.message.isClear")
                        .toString();
            }
        };
    }

    @Override
    public CommandToLanguagePack getCommandToLanguage() {
        return new CommandToLanguagePack() {
            @Override
            public void sendMessageOnRequest(Player buyer) {
                languageConfig.getMessage("command.to.message")
                        .setBuyer(buyer).sendMessage(buyer);
            }

            @Override
            public String getMessageOnSecondPlayerConfirmTrade(String nameOfBuyer) {
                return languageConfig.getMessage("command.to.message.onSecondPlayerConfirm")
                        .setBuyer(nameOfBuyer).toString();
            }
        };
    }
}
