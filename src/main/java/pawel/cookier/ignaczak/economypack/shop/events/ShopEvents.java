package pawel.cookier.ignaczak.economypack.shop.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopCategoryController;

public class ShopEvents implements Listener {

    private final ShopCategoryController shopCategoryController;

    public ShopEvents(ShopCategoryController shopCategoryController) {
        this.shopCategoryController = shopCategoryController;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        if (event.getInventory().equals(ShopConfig.SHOP_MAIN_INVENTORY))
        {
            event.setCancelled(true);

            if(shopCategoryController.isShiftMouseClick(event)){
                return;
            }

            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
        }
    }
}
