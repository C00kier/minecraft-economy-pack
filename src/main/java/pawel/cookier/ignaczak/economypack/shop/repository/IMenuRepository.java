package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import pawel.cookier.ignaczak.economypack.shop.models.InventoryMenu;

public interface IMenuRepository {
    void openInventoryMenu(Player player, InventoryMenu inventoryMenu);
}
