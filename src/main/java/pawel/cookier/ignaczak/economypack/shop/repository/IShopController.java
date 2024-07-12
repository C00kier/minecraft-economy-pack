package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface IShopController {
    void openInventoryMenu(Player player);
    boolean isShiftMouseClick(InventoryClickEvent event);
}
