package pawel.cookier.ignaczak.economypack.shop_manager.category_entity.controller;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.repository.ICategoryController;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.navbar.controller.ShopNavbarController;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;

import java.util.List;

public class CategoryController implements ICategoryController {

    private final ShopNavbarController shopNavbarController;

    public CategoryController(ShopNavbarController shopNavbarController) {
        this.shopNavbarController = shopNavbarController;
    }

    @Override
    public void addItemToCategory(Category category, Item item) {
        List<Item> currentList = category.getListOfItems();
        currentList.add(item);
        category.setListOfItems(currentList);
    }

    @Override
    public void removeItemFromCategory(Category category, Item item) {
        List<Item> currentList = category.getListOfItems();
        currentList.remove(item);
        category.setListOfItems(currentList);
    }

    @Override
    public void editCategoryItemStackName(Category category, String newName) {
        ItemStack itemStack = category.getCategoryItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(newName);
            itemStack.setItemMeta(itemMeta);
            category.setCategoryItemStack(itemStack);
        }
    }

    @Override
    public void editCategoryItemStackMaterial(Category category, ItemStack newItemStack) {
        category.setCategoryItemStack(newItemStack);
    }

    @Override
    public void setCategoryInventoryByPage(BalanceManager balanceManager,
                                           Player player,
                                           Category category,
                                           int pageToDisplay) {
        Inventory inventory = category.getInventory();
        inventory.clear();

        List<ItemStack> itemStacksList = category.getListOfItems().stream()
                .map(Item::getItemStack)
                .toList();

        int totalItems = itemStacksList.size();
        int start = (pageToDisplay - 1) * PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP;
        int end = Math.min(start + PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP, totalItems);

        for (int i = start; i < end; i++) {
            inventory.setItem(i - start, itemStacksList.get(i));
        }

        shopNavbarController.addNavbarToCategoryInventory(player, balanceManager, inventory);
        category.setCurrentPage(pageToDisplay);
    }

}

