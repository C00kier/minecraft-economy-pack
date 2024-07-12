package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopController;

public class ShopController implements IShopController {
    private final Inventory shopMain;

    public ShopController() {
        this.shopMain = ShopConfig.SHOP_MAIN_INVENTORY;
    }

    @Override
    public void openInventoryMenu(Player player) {
        player.openInventory(shopMain);
    }

    @Override
    public boolean isShiftMouseClick(InventoryClickEvent event){
        return event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
    }

}
