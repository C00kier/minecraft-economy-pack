package pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.repository.IShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShopController implements IShopController {

    @Override
    public void addCategoryToShop(Shop shop, Category category) {
        List<Category> currentCategories = new ArrayList<>(shop.getCategoryList());
        currentCategories.add(category);
        shop.setCategoryList(currentCategories);
        updateShopInventory(shop);
    }

    @Override
    public void removeCategoryFromShop(Shop shop, Category category) {
        List<Category> currentCategories = new ArrayList<>(shop.getCategoryList());
        currentCategories.remove(category);
        shop.setCategoryList(currentCategories);
        updateShopInventory(shop);
    }

    @Override
    public Optional<Category> findCategoryByName(Shop shop, String categoryName) {
        return shop.getCategoryList()
                .stream()
                .filter(category -> Objects.requireNonNull(category.getCategoryItemStack().getItemMeta())
                        .getDisplayName()
                        .equalsIgnoreCase(categoryName))
                .findFirst();
    }

    @Override
    public Optional<Category> findCategoryByInventory(Shop shop, Inventory inventory) {
        return shop.getCategoryList()
                .stream()
                .filter(category -> category.getInventory().equals(inventory))
                .findFirst();
    }

    @Override
    public void updateShopInventory(Shop shop) {
        shop.getInventory().clear();
        shop.getInventory().setContents(
                shop.getCategoryList()
                        .stream()
                        .map(Category::getCategoryItemStack)
                        .toArray(ItemStack[]::new));
    }

    @Override
    public Optional<Item> findItemByItemId(Shop shop, int itemId) {
        return shop.getCategoryList().stream()
                .flatMap(category -> category.getListOfItems().stream())
                .filter(item -> item.getId() == itemId)
                .findFirst();
    }

}
