package pawel.cookier.ignaczak.economypack.translation_manager.repository;

import java.util.Locale;

public interface ITranslationManager {
    void loadLocale(Locale locale);
    String getMessage(String key);
}
