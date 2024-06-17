package pawel.cookier.ignaczak.economypack.translation_manager.controllers;

import pawel.cookier.ignaczak.economypack.translation_manager.repository.ITranslationManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.MissingResourceException;

public class TranslationManager implements ITranslationManager {
    private ResourceBundle messages;
    private static final Logger LOGGER = Logger.getLogger(TranslationManager.class.getName());

    public TranslationManager(Locale locale) {
        loadLocale(locale);
    }

    public void loadLocale(Locale locale) {
        try {
            String resourceName = "translation_" + locale.getLanguage() + ".properties";
            LOGGER.info("Attempting to load resource: " + resourceName);
            InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
            if (resourceStream != null) {
                InputStreamReader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8);
                this.messages = new PropertyResourceBundle(reader);
                LOGGER.info("Successfully loaded resource: " + resourceName);
            } else {
                LOGGER.warning("Resource not found: " + resourceName + ". Falling back to default language.");
                loadDefaultLanguage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDefaultLanguage();
        }
    }

    private void loadDefaultLanguage() {
        try {
            String defaultResource = "translation_en.properties";
            LOGGER.info("Attempting to load default resource: " + defaultResource);
            InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(defaultResource);
            if (resourceStream != null) {
                InputStreamReader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8);
                this.messages = new PropertyResourceBundle(reader);
                LOGGER.info("Successfully loaded default resource: " + defaultResource);
            } else {
                throw new MissingResourceException("Default resource not found: " + defaultResource, this.getClass().getName(), defaultResource);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load any language file.");
        }
    }

    public String getMessage(String key) {
        try {
            return messages.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.warning("Translation missing for: " + key);
            return "Translation missing for: " + key;
        }
    }
}