package pawel.cookier.ignaczak.economypack.shop.validation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.shop.repository.ICommandsValidation;

public class CommandsValidation implements ICommandsValidation {
    @Override
    public boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args) {
        return false;
    }

    @Override
    public boolean isRemoveCategoryValid(Player player, Inventory inventory, String[] args) {
        return false;
    }

    @Override
    public boolean isEditCategoryNameValid(Player player, Inventory inventory, String[] args) {
        return false;
    }

    @Override
    public boolean isEditCategoryItemStackValid(Player player, Inventory inventory, String[] args) {
        return false;
    }
}
