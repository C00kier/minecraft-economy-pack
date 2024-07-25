package pawel.cookier.ignaczak.economypack.shop_manager.events.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public interface IShopEventsUtility {
    boolean doesShopContainExistingCategoryByInventory(Shop shop, Inventory inventory);

    void switchToCategoryInventory(Player player,
                                   Category category);
}
