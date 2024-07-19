package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

import java.util.Optional;

public interface IShopController {
    void addCategoryToShop(Shop shop, Category category);

    void removeCategoryFromShop(Shop shop, Category category);

    Optional<Category> findCategoryByName(Shop shop, String categoryName);

    Optional<Category> findCategoryByInventory(Shop shop, Inventory inventory);

    void updateShopInventory(Shop shop);

    Optional<Item> findItemByItemId(Shop shop, int itemId);
}
