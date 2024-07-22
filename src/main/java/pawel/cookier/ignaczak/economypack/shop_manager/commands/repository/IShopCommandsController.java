package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public interface IShopCommandsController {
    void openInventory(Player player, Inventory inventory);

    //category_entity
    void addShopCategory(Player player, String[] args);

    void removeCategoryFromShop(Player player, String[] args);

    void editShopCategoryName(Player player, String[] args);

    void editShopCategoryItemStack(Player player, String[] args);

    //item_entity
    void addShopItem(JavaPlugin plugin, Player player, String[] args);

    void addItemFromHandToCategory(JavaPlugin plugin, Player player, String[] args);

    void removeItemFromCategory(JavaPlugin plugin, Player player, String[] args);

    void editItemSellPrice(JavaPlugin plugin, Player player, String[] args);

    void editItemBuyPrice(JavaPlugin plugin, Player player, String[] args);
}
