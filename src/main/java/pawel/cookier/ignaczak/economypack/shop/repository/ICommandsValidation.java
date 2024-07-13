package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface ICommandsValidation {
    boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args);
    boolean isRemoveCategoryValid(Player player, Inventory inventory, String[] args);
    boolean isEditCategoryNameValid(Player player, Inventory inventory, String[] args);
    boolean isEditCategoryItemStackValid(Player player, Inventory inventory, String[] args);
}
