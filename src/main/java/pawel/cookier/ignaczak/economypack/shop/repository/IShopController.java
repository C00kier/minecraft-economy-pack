package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface IShopController {
    void openInventoryMenu(Player player);
    boolean isShiftMouseClick(InventoryClickEvent event);
    void addShopCategory(Player player, Inventory inventory, String[] args);
}
