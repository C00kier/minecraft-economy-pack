package pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.repository;

import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

import java.util.Optional;

public interface IShopController {
    void addCategoryToShop(Shop shop, Category category);

    void removeCategoryFromShop(Shop shop, Category category);

    Optional<Category> findCategoryByName(Shop shop, String categoryName);

    Optional<Category> findCategoryByInventory(Shop shop, Inventory inventory);

    Optional<Category> findCategoryByItemId(Shop shop, int itemId);

    void updateShopInventory(Shop shop);

    Optional<Item> findItemByItemId(Shop shop, int itemId);
}
