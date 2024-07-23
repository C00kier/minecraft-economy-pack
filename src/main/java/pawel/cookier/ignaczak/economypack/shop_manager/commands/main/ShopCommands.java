package pawel.cookier.ignaczak.economypack.shop_manager.commands.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.controller.CategoryCommandsController;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.controller.ItemCommandsController;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.controller.ShopTabController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

import java.util.List;

public class ShopCommands implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;
    private final Shop shop;
    private final CategoryCommandsController categoryCommandsController;
    private final ItemCommandsController itemCommandsController;
    private final ShopTabController shopTabController;

    public ShopCommands(JavaPlugin plugin,
                        Shop shop,
                        CategoryCommandsController categoryCommandsController,
                        ItemCommandsController itemCommandsController, ShopTabController shopTabController) {
        this.plugin = plugin;
        this.shop = shop;
        this.categoryCommandsController = categoryCommandsController;
        this.itemCommandsController = itemCommandsController;
        this.shopTabController = shopTabController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player player) {
            String commandName = command.getName().toLowerCase();

            registerOpenShopCommand(player, commandName);
            categoryCommandsController.registerCategoryOpCommands(player, commandName, args);
            itemCommandsController.registerItemOpCommands(plugin, player, commandName, args);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender,
                                      @NotNull Command command,
                                      @NotNull String s,
                                      @NotNull String[] args) {
        String commandName = command.getName();
        return shopTabController.getListOfSuggestionsForCommands(commandName, args);
    }

    private void registerOpenShopCommand(Player player, String commandName) {
        if (commandName.equalsIgnoreCase(PluginConfig.CALL_SHOP_COMMAND)) {
            player.openInventory(shop.getInventory());
        }
    }

}
