package pawel.cookier.ignaczak.economypack.shop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopTabController;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopCommands implements CommandExecutor, TabCompleter {

    private final Shop shop;
    private final ShopCommandsController shopCommandsController;
    private final ShopTabController shopTabController;

    public ShopCommands(Shop shop, ShopCommandsController shopCommandsController, ShopTabController shopTabController) {
        this.shop = shop;
        this.shopCommandsController = shopCommandsController;
        this.shopTabController = shopTabController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player player) {
            String commandName = command.getName().toLowerCase();

            if (commandName.equalsIgnoreCase(PluginConfig.CALL_SHOP_COMMAND)) {
                shopCommandsController.openInventoryMenu(player);
            }

            if (player.isOp()) {
                switch (commandName) {
                    case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> shopCommandsController.addShopCategory(
                            player,
                            shop.getInventory(),
                            args);
                    case PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND -> shopCommandsController.removeCategoryFromShop(
                            player,
                            shop.getInventory(),
                            args);
                    case PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND -> shopCommandsController.editShopCategoryName(
                            player,
                            shop.getInventory(),
                            args);
                    case PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND ->
                            shopCommandsController.editShopCategoryItemStack(
                                    player,
                                    shop.getInventory(),
                                    args);
                }
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
            case PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND,
                    PluginConfig.ADD_SHOP_CATEGORY_COMMAND ->
                    suggestions.addAll(shopTabController.addEditCategoryIconOnTabComplete(args));
            case PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND,
                    PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND ->
                    suggestions.addAll(shopTabController.removeEditNameCategoryOnTabComplete(args));
        }

        return suggestions;
    }


}
