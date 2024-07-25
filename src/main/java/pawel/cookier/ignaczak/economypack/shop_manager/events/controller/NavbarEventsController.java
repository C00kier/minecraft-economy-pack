package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.INavbarEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class NavbarEventsController implements INavbarEventsController {

    private final Shop shop;
    private final ShopEventsUtility utility;
    private final ShopEventsValidation validation;
    private final ShopController shopController;

    public NavbarEventsController(Shop shop,
                                  ShopEventsUtility utility,
                                  ShopEventsValidation validation,
                                  ShopController shopController) {
        this.shop = shop;
        this.utility = utility;
        this.validation = validation;
        this.shopController = shopController;
    }

    @Override
    public void nextPageButtonLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && validation.isClickedItemNextPageButtonIcon(clickedItem)) {
                nextButtonClickEvent(shop, event);
            }
        }
    }

    @Override
    public void previousPageButtonLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && validation.isClickedItemPreviousPageButtonIcon(clickedItem)) {
                previousButtonClickEvent(shop, event);
            }
        }
    }

    @Override
    public void returnToShopButtonLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && validation.isClickedItemReturnIcon(clickedItem)) {
                backButtonClickEvent(shop, event);
            }
        }
    }

    private void nextButtonClickEvent(Shop shop, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        shopController.findCategoryByInventory(shop, inventory).ifPresent(category -> {
            int itemsInCategory = category.getListOfItems().size();
            int totalPages = (int) Math.ceil((double) itemsInCategory / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
            int currentPage = category.getCurrentPage();

            if (currentPage < totalPages) {
                category.setCurrentPage(currentPage + 1);
                Player player = (Player) event.getWhoClicked();
                utility.switchToCategoryInventory(player, category);
            }
        });
    }

    private void previousButtonClickEvent(Shop shop, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        shopController.findCategoryByInventory(shop, inventory).ifPresent(category -> {
            int currentPage = category.getCurrentPage();

            if (currentPage > 1) {
                category.setCurrentPage(currentPage - 1);
                Player player = (Player) event.getWhoClicked();
                utility.switchToCategoryInventory(player, category);
            }
        });
    }

    private void backButtonClickEvent(Shop shop, InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        player.openInventory(shop.getInventory());
    }

}
