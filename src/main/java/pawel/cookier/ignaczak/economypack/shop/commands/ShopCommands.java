package pawel.cookier.ignaczak.economypack.shop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopTabController;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopCommands implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;
    private final Shop shop;
    private final ShopCommandsController shopCommandsController;
    private final ShopTabController shopTabController;

    public ShopCommands(JavaPlugin plugin,
                        Shop shop,
                        ShopCommandsController shopCommandsController,
                        ShopTabController shopTabController) {
        this.plugin = plugin;
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
                shopCommandsController.openInventory(player, shop.getInventory());
            }

            if (player.isOp()) {
                switch (commandName) {
                    case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> shopCommandsController.addShopCategory(
                            player, args);
                    case PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND -> shopCommandsController.removeCategoryFromShop(
                            player, args);
                    case PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND -> shopCommandsController.editShopCategoryName(
                            player, args);
                    case PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND ->
                            shopCommandsController.editShopCategoryItemStack(player, args);
                    case PluginConfig.ADD_SHOP_ITEM_COMMAND -> shopCommandsController.addShopItem(plugin, player, args);
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
            case PluginConfig.ADD_SHOP_CATEGORY_COMMAND ->
                    suggestions.addAll(shopTabController.addCategoryOnTabComplete(args));
            case PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND ->
                suggestions.addAll(shopTabController.editCategoryItemStackTypeOnTabComplete(args));
            case PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND,
                    PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND ->
                    suggestions.addAll(shopTabController.removeEditNameCategoryOnTabComplete(args));
            case PluginConfig.ADD_SHOP_ITEM_COMMAND ->
                    suggestions.addAll(shopTabController.addItemOnTabComplete(args));
        }

        return suggestions;
    }


}
