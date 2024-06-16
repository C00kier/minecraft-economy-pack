package pawel.cookier.ignaczak.economypack.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.controllers.PluginManagerController;

import java.util.ArrayList;
import java.util.List;

public class PluginManagerCommands implements CommandExecutor, TabCompleter {

    private final PluginManagerController pluginManagerController;

    public PluginManagerCommands(PluginManagerController pluginManagerController) {
        this.pluginManagerController = pluginManagerController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        String commandName = command.getName();
        if (commandName.equalsIgnoreCase(PluginConfig.TRANSLATION_COMMAND)
                && sender instanceof Player) {
            pluginManagerController.executeTranslationCommand(sender, args);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {

        List<String> suggestions = new ArrayList<>();
        if (args.length == 1 && command.getName().equalsIgnoreCase("translation")) {
            suggestions.addAll(List.of(
                    PluginConfig.TRANSLATION_POLISH,
                    PluginConfig.TRANSLATION_ENGLISH,
                    PluginConfig.TRANSLATION_SPANISH,
                    PluginConfig.TRANSLATION_FRENCH,
                    PluginConfig.TRANSLATION_GERMAN
            ));
        }

        return suggestions;
    }
}
