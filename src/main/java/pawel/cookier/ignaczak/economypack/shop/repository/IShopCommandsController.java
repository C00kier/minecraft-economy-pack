package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface IShopCommandsController {
    void openInventory(Player player, Inventory inventory);
    boolean isShiftMouseClick(InventoryClickEvent event);
    void addShopCategory(Player player, Inventory inventory, String[] args);
    void removeCategoryFromShop(Player player, Inventory inventory, String[] args);
    void editShopCategoryName(Player player, Inventory inventory, String[] args);
    void editShopCategoryItemStack(Player player, Inventory inventory, String[] args);
    void switchBetweenInventoriesBasedOnItemStack(Player player, ItemStack itemStack);
}
