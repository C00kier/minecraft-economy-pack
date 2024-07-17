package pawel.cookier.ignaczak.economypack.shop.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopEventsController;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

import java.util.Objects;

public class ShopEvents implements Listener {

    private final Shop shop;
    private final ShopEventsController shopEventsController;
    private final JavaPlugin plugin;

    public ShopEvents(Shop shop, ShopEventsController shopEventsController, JavaPlugin plugin) {
        this.shop = shop;
        this.shopEventsController = shopEventsController;
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.equals(shop.getInventory())) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            if (shopEventsController.isShiftMouseClick(event)) return;

            shopEventsController.clickCategoryEvent(shop, event, clickedItem);
        }
    }

    @EventHandler
    public void sellAllItemsOfCertainType(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && shopEventsController.isItemStackInCurrentlyOpenInventory(inventory, clickedItem)
                    && event.getClick() == ClickType.SHIFT_RIGHT) {
                Player player = (Player) event.getWhoClicked();

                shopEventsController.exchangeAllItemStacksOfSameTypeForMoney(plugin, player, clickedItem);
            }
        }
    }

    @EventHandler
    public void nextPageButton(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && clickedItem.getType() == PluginConfig.SHOP_NAVBAR_NEXT_BUTTON_MATERIAL
                    && Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals("Następna Strona")) {
                shopEventsController.nextButtonClickEvent(shop, event);
            }
        }
    }
}
