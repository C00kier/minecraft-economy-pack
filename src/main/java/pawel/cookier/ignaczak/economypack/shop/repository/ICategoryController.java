package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;

public interface ICategoryController {
    void addItemToCategory(Category category, Item item);

    void removeItemFromCategory(Category category, Item item);

    void editCategoryItemStackName(Category category, String newName);

    void editCategoryItemStackMaterial(Category category, ItemStack newItemStack);

    void setCategoryInventoryByPage(BalanceManager balanceManager, Player player, Category category, int pageToDisplay);
}
