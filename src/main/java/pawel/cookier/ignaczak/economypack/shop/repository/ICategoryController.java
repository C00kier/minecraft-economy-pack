package pawel.cookier.ignaczak.economypack.shop.repository;

import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;

import java.util.Optional;

public interface ICategoryController {
    void addItemToCategory(Category category, Item item);
    void removeItemFromCategory(Category category, Item item);
    Optional<Item> findItemByName(Category category, String itemName);
}
