package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IShopCommandsValidation {
    boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args);

    boolean isRemoveCategoryValid(Player player, String[] args);

    boolean isEditCategoryNameValid(Player player, String[] args);

    boolean isEditCategoryItemStackValid(Player player, String[] args);

    boolean isAddShopItemValid(Player player, String[] args);

    boolean isAddItemFromHandToCategoryValid(Player player, String[] args);

    boolean isEditItemSellPriceValid(Player player, String[] args);

    boolean isEditItemBuyPriceValid(Player player, String[] args);

    boolean isRemoveItemFromCategoryValid(Player player, String[] args);
}
