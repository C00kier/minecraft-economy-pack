package pawel.cookier.ignaczak.economypack.shop.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

public class ShopEvents implements Listener {

    private final Shop shop;
    private final ShopCommandsController shopCommandsController;

    public ShopEvents(Shop shop, ShopCommandsController shopCommandsController) {
        this.shop = shop;
        this.shopCommandsController = shopCommandsController;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        if (event.getInventory().equals(shop.getInventory()))
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
