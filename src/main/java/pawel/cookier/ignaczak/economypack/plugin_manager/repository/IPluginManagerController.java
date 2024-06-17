package pawel.cookier.ignaczak.economypack.plugin_manager.repository;

import org.bukkit.command.CommandSender;

public interface IPluginManagerController {
    void executeTranslationCommand(CommandSender sender, String[] args);
}
