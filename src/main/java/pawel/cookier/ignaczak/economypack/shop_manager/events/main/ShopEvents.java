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
                shop, itemController);
        this.itemInventoryEventsController = new ItemInventoryEventsController(javaPlugin, shopEventsValidation, itemController);
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
    public void returnToCategoryButton(InventoryClickEvent event) {
        navbarEventsController.returnToCategoryButtonLeftClickEvent(event);
    }

    @EventHandler
    public void openItemMenu(InventoryClickEvent event) {
        categoryInventoryEventsController.openItemMenuLeftClickEvent(event);
    }

    //item inventory button events
    @EventHandler
    public void minusItemQuantity64Button(InventoryClickEvent event) {
        itemInventoryEventsController.minusItemQuantity64ButtonEvent(event);
    }

    @EventHandler
    public void minusItemQuantity16Button(InventoryClickEvent event) {
        itemInventoryEventsController.minusItemQuantity16ButtonEvent(event);
    }

    @EventHandler
    public void minusItemQuantity1Button(InventoryClickEvent event) {
        itemInventoryEventsController.minusItemQuantity1ButtonEvent(event);
    }

    @EventHandler
    public void plusItemQuantity64Button(InventoryClickEvent event) {
        itemInventoryEventsController.plusItemQuantity64ButtonEvent(event);
    }

    @EventHandler
    public void plusItemQuantity16Button(InventoryClickEvent event) {
        itemInventoryEventsController.plusItemQuantity16ButtonEvent(event);
    }

    @EventHandler
    public void plusItemQuantity1Button(InventoryClickEvent event) {
        itemInventoryEventsController.plusItemQuantity1ButtonEvent(event);
    }

    //operations
    @EventHandler
    public void buyItemButton(InventoryClickEvent event){
        itemInventoryEventsController.buyButtonEvent(event);
    }

    @EventHandler
    public void sellItemButton(InventoryClickEvent event){
        itemInventoryEventsController.sellButtonEvent(event);
    }

    @EventHandler
    public void sellAllItemsOfCertainType(InventoryClickEvent event) {
        categoryInventoryEventsController.sellAllItemsOfCertainTypeShiftRightClickEvent(event);
    }


}
