package pawel.cookier.ignaczak.economypack.shop.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopCommandsController;

public class ShopEvents implements Listener {

    private final ShopCommandsController shopCommandsController;

    public ShopEvents(ShopCommandsController shopCommandsController) {
        this.shopCommandsController = shopCommandsController;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        if (event.getInventory().equals(ShopConfig.SHOP_MAIN_INVENTORY))
        {
            event.setCancelled(true);

            if(shopCommandsController.isShiftMouseClick(event)){
                return;
            }

            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
        }
    }
}
