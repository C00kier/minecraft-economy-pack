package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.controller.CategoryController;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.IShopEventsUtility;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class ShopEventsUtility implements IShopEventsUtility {

    private final CategoryController categoryController;
    private final BalanceManager balanceManager;

    public ShopEventsUtility(CategoryController categoryController, BalanceManager balanceManager) {
        this.categoryController = categoryController;
        this.balanceManager = balanceManager;
    }

    @Override
    public boolean doesShopContainExistingCategoryByInventory(Shop shop, Inventory inventory) {
        return shop.getCategoryList()
                .stream()
                .anyMatch(category -> category.getInventory().equals(inventory));
    }

    @Override
    public void switchToCategoryInventory(Player player,
                                          Category category) {
        player.closeInventory();

        int itemsInCategory = category.getListOfItems().size();
        int totalPages = (int) Math.ceil((double) itemsInCategory / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
        int pageToOpen = category.getCurrentPage();

        if (pageToOpen <= totalPages) {
            categoryController.displayCategoryInventoryBasedByPage(balanceManager, player, category, pageToOpen);
        }

        player.openInventory(category.getInventory());
    }
}
