package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.entity.Player;
import pawel.cookier.ignaczak.economypack.shop.models.InventoryMenu;
import pawel.cookier.ignaczak.economypack.shop.repository.IMenuRepository;

public class MenuController implements IMenuRepository {
    @Override
    public void openInventoryMenu(Player player, InventoryMenu inventoryMenu) {
        player.openInventory(inventoryMenu.inventory());
    }


}
