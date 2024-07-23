package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface ICategoryCommandsValidation {
    boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args);

    boolean isRemoveCategoryValid(Player player, String[] args);

    boolean isEditCategoryNameValid(Player player, String[] args);

    boolean isEditCategoryItemStackValid(Player player, String[] args);

}
