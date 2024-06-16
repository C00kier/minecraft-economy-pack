package pawel.cookier.ignaczak.economypack.managers;

import java.util.Locale;

public interface ITranslationManager {
    void loadLocale(Locale locale);
    String getMessage(String key);
}
