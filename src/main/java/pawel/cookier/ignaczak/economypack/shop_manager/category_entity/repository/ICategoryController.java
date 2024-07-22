package pawel.cookier.ignaczak.economypack.shop_manager.category_entity.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;

public interface ICategoryController {
    void addItemToCategory(Category category, Item item);

    void removeItemFromCategory(Category category, Item item);

    void editCategoryItemStackName(Category category, String newName);

    void editCategoryItemStackMaterial(Category category, ItemStack newItemStack);

    void displayCategoryInventoryBasedByPage(BalanceManager balanceManager, Player player, Category category, int pageToDisplay);
}
