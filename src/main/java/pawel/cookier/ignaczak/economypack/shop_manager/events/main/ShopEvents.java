package pawel.cookier.ignaczak.economypack.shop_manager.events.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.controller.CategoryController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.controller.*;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.controller.ItemController;
import pawel.cookier.ignaczak.economypack.shop_manager.navbar.controller.ShopNavbarController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class ShopEvents implements Listener {

    private final ShopInventoryInventoryEventsController shopInventoryEventsController;
    private final CategoryInventoryEventsController categoryInventoryEventsController;
    private final ItemInventoryEventsController itemInventoryEventsController;
    private final NavbarEventsController navbarEventsController;

    public ShopEvents(JavaPlugin javaPlugin,
                      Shop shop,
                      ShopController shopController,
                      CategoryController categoryController,
                      ItemController itemController,
                      ShopNavbarController shopNavbarController,
                      BalanceManager balanceManager) {
        ShopEventsValidation shopEventsValidation = new ShopEventsValidation();
        ShopEventsUtility shopEventsUtility = new ShopEventsUtility(categoryController, balanceManager);

        this.shopInventoryEventsController = new ShopInventoryInventoryEventsController(
                shopController,
                shop,
                shopEventsUtility);
        this.categoryInventoryEventsController = new CategoryInventoryEventsController(
                shopEventsUtility,
                shopEventsValidation,
                balanceManager,
                javaPlugin,
                shopNavbarController,
                shop);
        this.itemInventoryEventsController = new ItemInventoryEventsController();
        this.navbarEventsController = new NavbarEventsController(
                shop,
                shopEventsUtility,
                shopEventsValidation,
                javaPlugin,
                shopController,
                itemController);
    }

    //menu navigation
    @EventHandler
    public void onShopInventoryClick(InventoryClickEvent event) {
        shopInventoryEventsController.onShopInventoryLeftClickEvent(event);
    }

    @EventHandler
    public void nextPageButton(InventoryClickEvent event) {
        navbarEventsController.nextPageButtonLeftClickEvent(event);
    }

    @EventHandler
    public void previousPageButton(InventoryClickEvent event) {
        navbarEventsController.previousPageButtonLeftClickEvent(event);
    }

    @EventHandler
    public void returnToShopButton(InventoryClickEvent event) {
        navbarEventsController.returnToShopButtonLeftClickEvent(event);
    }

    @EventHandler
    public void returnToCategoryButton(InventoryClickEvent event){
        navbarEventsController.returnToCategoryButtonLeftClickEvent(event);
    }

    @EventHandler
    public void openBuyItemMenu(InventoryClickEvent event) {
        categoryInventoryEventsController.openBuyItemMenuLeftClickEvent(event);
    }

    @EventHandler
    public void openSellItemMenu(InventoryClickEvent event) {
        categoryInventoryEventsController.openSellItemMenuRightClickEvent(event);
    }

    //selling items
    @EventHandler
    public void sellAllItemsOfCertainType(InventoryClickEvent event) {
        categoryInventoryEventsController.sellAllItemsOfCertainTypeShiftRightClickEvent(event);
    }


}
