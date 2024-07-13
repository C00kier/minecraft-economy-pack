package pawel.cookier.ignaczak.economypack.shop.repository;

import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

import java.util.Optional;

public interface IShopController {
    void addCategoryToShop(Shop shop, Category category);
    void removeCategoryFromShop(Shop shop, Category category);
    Optional<Category> findCategoryByName(Shop shop, String categoryName);

    //file operations
    void updateShop();
    void loadShopFromFile();
}
