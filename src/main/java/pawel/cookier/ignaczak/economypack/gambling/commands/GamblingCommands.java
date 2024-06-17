package pawel.cookier.ignaczak.economypack.gambling.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.gambling.controllers.GamblingController;

import java.util.List;

public class GamblingCommands implements CommandExecutor, TabCompleter {

    private final GamblingController gamblingController;

    public GamblingCommands(GamblingController gamblingController) {
        this.gamblingController = gamblingController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (command.getName().toLowerCase()) {
                case PluginConfig.GAMBLE_COMMAND -> gamblingController.runGamble(player, args);
                case PluginConfig.SLOTS_COMMAND -> gamblingController.runSlots(player, args);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("<amount>");
        }
        return null;
    }
}
