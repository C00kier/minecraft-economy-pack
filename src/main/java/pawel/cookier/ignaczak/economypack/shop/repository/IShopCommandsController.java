package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public interface IShopCommandsController {
    void openInventory(Player player, Inventory inventory);

    //category
    void addShopCategory(Player player, String[] args);

    void removeCategoryFromShop(Player player, String[] args);

    void editShopCategoryName(Player player, String[] args);

    void editShopCategoryItemStack(Player player, String[] args);

    //item
    void addShopItem(JavaPlugin plugin, Player player, String[] args);

}
