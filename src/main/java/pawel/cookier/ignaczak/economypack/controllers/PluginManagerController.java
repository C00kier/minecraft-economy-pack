package pawel.cookier.ignaczak.economypack.controllers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.managers.TranslationManager;

import java.util.Locale;

public class PluginManagerController implements IPluginManagerController {

    private final TranslationManager translationManager;

    public PluginManagerController(TranslationManager translationManager) {
        this.translationManager = translationManager;
    }

    public void executeTranslationCommand(CommandSender sender, String[] args) {
        if (sender.isOp()) {
            if (args.length == 1) {
                String translationName = args[0];
                setTranslation(sender, translationName);
            } else {
                sender.sendMessage(ChatColor.LIGHT_PURPLE +
                        translationManager.getMessage("message.pluginManager.incorrectTranslationArguments"));
            }
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE +
                    translationManager.getMessage("message.common.lackOfPermission"));
        }
    }

    private void setTranslation(CommandSender sender, String translationName) {
        switch (translationName.toLowerCase()) {
            case PluginConfig.TRANSLATION_POLISH -> changeTranslation(
                    sender,
                    PluginConfig.TRANSLATION_TAG_POLISH,
                    translationName
            );
            case PluginConfig.TRANSLATION_ENGLISH -> changeTranslation(
                    sender,
                    PluginConfig.TRANSLATION_TAG_ENGLISH,
                    translationName
            );
            case PluginConfig.TRANSLATION_SPANISH -> changeTranslation(
                    sender,
                    PluginConfig.TRANSLATION_TAG_SPANISH,
                    translationName
            );
            case PluginConfig.TRANSLATION_FRENCH -> changeTranslation(
                    sender,
                    PluginConfig.TRANSLATION_TAG_FRENCH,
                    translationName
            );
            case PluginConfig.TRANSLATION_GERMAN -> changeTranslation(
                    sender,
                    PluginConfig.TRANSLATION_TAG_GERMAN,
                    translationName
            );
            default -> sender.sendMessage(ChatColor.LIGHT_PURPLE +
                    translationManager.getMessage("message.pluginManager.translationNotFound")
                            .formatted(translationName));
        }
    }

    private void changeTranslation(CommandSender sender, String translationTag, String translationLanguage) {
        translationManager.loadLocale(new Locale(translationTag));
        sender.sendMessage(ChatColor.GREEN +
                translationManager.getMessage("message.pluginManager.translationChanged")
                        .formatted(translationLanguage));
    }
}
