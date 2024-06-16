package pawel.cookier.ignaczak.economypack.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.controllers.MoneyManagerController;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class MoneyManagerCommands implements CommandExecutor, TabCompleter {

    private final MoneyManagerController moneyManagerController;

    public MoneyManagerCommands(MoneyManagerController moneyManagerController) {
        this.moneyManagerController = moneyManagerController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (command.getName().toLowerCase()) {
                case PluginConfig.BALANCE_COMMAND -> moneyManagerController.checkBalance(player, args);
                case PluginConfig.EXCHANGE_COMMAND -> moneyManagerController.exchangeGold(player, args);
                case PluginConfig.PAY_COMMAND -> moneyManagerController.payToPlayer(player, args);
                case PluginConfig.ADD_USER_MANUALLY_COMMAND -> moneyManagerController.addPlayerManually(player, args);
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
        List<String> suggestions = new ArrayList<>();
        String commandName = command.getName();

        if (args.length == 1) {
            switch (commandName.toLowerCase()) {
                case PluginConfig.EXCHANGE_COMMAND, PluginConfig.PAY_COMMAND -> suggestions.add("<amount>");
                case PluginConfig.ADD_USER_MANUALLY_COMMAND, PluginConfig.BALANCE_COMMAND -> suggestions.add("<name>");
            }
        } else if (args.length == 2 && commandName.equalsIgnoreCase(PluginConfig.PAY_COMMAND)) {
            for (Player player : getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }
}
