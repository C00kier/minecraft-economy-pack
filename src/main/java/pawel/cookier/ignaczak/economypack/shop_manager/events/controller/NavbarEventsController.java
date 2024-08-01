package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.INavbarEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.controller.ItemController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class NavbarEventsController implements INavbarEventsController {

    private final Shop shop;
    private final ShopEventsUtility utility;
    private final ShopEventsValidation validation;
    private final JavaPlugin plugin;
    private final ShopController shopController;
    private final ItemController itemController;

    public NavbarEventsController(Shop shop,
                                  ShopEventsUtility utility,
                                  ShopEventsValidation validation,
                                  JavaPlugin plugin,
                                  ShopController shopController,
                                  ItemController itemController) {
        this.shop = shop;
        this.utility = utility;
        this.validation = validation;
        this.plugin = plugin;
        this.shopController = shopController;
        this.itemController = itemController;
    }

    @Override
    public void nextPageButtonLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && validation.isClickedItemNextPageButtonIcon(clickedItem)) {
                nextButtonClickEvent(event);
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
                previousButtonClickEvent(event);
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
                backToShopButtonClickEvent(event);
            }
        }
    }

    @Override
    public void returnToCategoryButtonLeftClickEvent(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String inventoryTitle = view.getTitle();

        if (inventoryTitle.equalsIgnoreCase("Kup przedmiot")
                || inventoryTitle.equalsIgnoreCase("Sprzedaj przedmiot")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && validation.isClickedItemReturnIcon(clickedItem)) {
                backToCategoryInventory(event);
            }
        }
    }

    private void nextButtonClickEvent(InventoryClickEvent event) {
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

    private void previousButtonClickEvent(InventoryClickEvent event) {
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

    private void backToShopButtonClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        player.openInventory(shop.getInventory());
    }

    private void backToCategoryInventory(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        ItemStack itemStack = inventory.getItem(PluginConfig.SHOP_OPERATIONS_ITEM_PLACE);
        Integer itemId = itemController.getItemIdByItemStack(plugin, itemStack);

        if (itemId != null) {
            shopController.findCategoryByItemId(shop, itemId).ifPresent(category -> {
                Player player = (Player) event.getWhoClicked();
                utility.switchToCategoryInventory(player, category);
            });
        }
    }


}
