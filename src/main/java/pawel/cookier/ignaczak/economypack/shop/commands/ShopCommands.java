package pawel.cookier.ignaczak.economypack.shop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopController;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopTabController;

import java.util.ArrayList;
import java.util.List;

public class ShopCommands implements CommandExecutor, TabCompleter {

    private final ShopController shopController;
    private final ShopTabController shopTabController;

    public ShopCommands(ShopController shopController, ShopTabController shopTabController) {
        this.shopController = shopController;
        this.shopTabController = shopTabController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player player) {
            String commandName = command.getName().toLowerCase();

            switch (commandName) {
                case PluginConfig.CALL_SHOP_COMMAND -> shopController.openInventoryMenu(player);
                case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> shopController.addShopCategory(
                        player,
                        ShopConfig.SHOP_MAIN_INVENTORY,
                        args);
                case PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND -> shopController.removeCategoryFromShop(
                        player,
                        ShopConfig.SHOP_MAIN_INVENTORY,
                        args
                );
            }
        }
        return true;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender,
                                      @NotNull Command command,
                                      @NotNull String s,
                                      @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        String commandName = command.getName();
        switch (commandName) {
            case PluginConfig.CALL_SHOP_COMMAND -> suggestions.add("");
            case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> suggestions.addAll(
                    shopTabController.addCategoryOnTabComplete(args));
            case PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND -> suggestions.addAll(
                    shopTabController.removeCategoryOnTabComplete(args));
        }

        return suggestions;
    }


}
