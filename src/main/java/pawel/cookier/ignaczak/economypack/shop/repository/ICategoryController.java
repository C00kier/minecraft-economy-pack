package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;

import java.util.Optional;

public interface ICategoryController {
    void addItemToCategory(Category category, Item item);

    void removeItemFromCategory(Category category, Item item);

    Optional<Item> findItemByName(Category category, String itemName);

    void editCategoryItemStackName(Category category, String newName);

    void editCategoryItemStackMaterial(Category category, ItemStack newItemStack);

    void setCategoryInventoryByPage(BalanceManager balanceManager, Player player, Category category, int pageToDisplay);
}
