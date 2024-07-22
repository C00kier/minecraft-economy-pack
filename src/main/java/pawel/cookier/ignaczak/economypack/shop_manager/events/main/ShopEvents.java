package pawel.cookier.ignaczak.economypack.shop_manager.events.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop_manager.events.controller.ShopEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class ShopEvents implements Listener {

    private final Shop shop;
    private final ShopEventsController shopEventsController;
    private final JavaPlugin plugin;
    private final ShopEventsValidation shopEventsValidation;

    public ShopEvents(Shop shop, ShopEventsController shopEventsController, JavaPlugin plugin) {
        this.shop = shop;
        this.shopEventsController = shopEventsController;
        this.plugin = plugin;
        this.shopEventsValidation = new ShopEventsValidation();
    }

    //menu navigation
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
    public void nextPageButton(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && shopEventsValidation.isClickedItemNextPageButtonIcon(clickedItem)) {
                shopEventsController.nextButtonClickEvent(shop, event);
            }
        }
    }

    @EventHandler
    public void previousPageButton(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && shopEventsValidation.isClickedItemPreviousPageButtonIcon(clickedItem)) {
                shopEventsController.previousButtonClickEvent(shop, event);
            }
        }
    }

    @EventHandler
    public void returnToShopButton(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && shopEventsValidation.isClickedItemReturnIcon(clickedItem)) {
                shopEventsController.backButtonClickEvent(shop, event);
            }
        }
    }

    @EventHandler
    public void openBuyItemMenu(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && event.getClick() == ClickType.LEFT
                    && !shopEventsValidation.isClickedItemElementOfNavbar(clickedItem)) {
                shopEventsController.openBuyItemMenu(event, clickedItem);
            }
        }
    }

    @EventHandler
    public void openSellItemMenu(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (shopEventsController.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && event.getClick() == ClickType.RIGHT
                    && !shopEventsValidation.isClickedItemElementOfNavbar(clickedItem)) {
                shopEventsController.openSellItemMenu(event, clickedItem);
            }
        }
    }

    //selling items
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
}
