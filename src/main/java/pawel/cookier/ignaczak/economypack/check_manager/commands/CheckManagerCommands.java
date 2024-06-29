package pawel.cookier.ignaczak.economypack.check_manager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.check_manager.controller.CheckManagerController;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;

public class CheckManagerCommands implements CommandExecutor, TabCompleter {

    private final CheckManagerController checkManagerController;
    private final JavaPlugin plugin;

    public CheckManagerCommands(CheckManagerController checkManagerController, JavaPlugin plugin) {
        this.checkManagerController = checkManagerController;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String[] strings) {
        if(commandSender instanceof Player player && command.getName().equalsIgnoreCase(PluginConfig.CHECK_COMMAND)){
            checkManagerController.addCheckToPlayerInventory(player, plugin, strings);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender,
                                      @NotNull Command command,
                                      @NotNull String s,
                                      @NotNull String[] strings) {
        List<String> suggestions = new ArrayList<>();
        String commandName = command.getName();

        if (strings.length == 1 && commandName.equalsIgnoreCase(PluginConfig.CHECK_COMMAND)) {
            suggestions.add("<ilość>");
        }
        return suggestions;
    }
}
