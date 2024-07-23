package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import org.bukkit.entity.Player;

public interface IItemCommandsValidation {

    boolean isAddShopItemValid(Player player, String[] args);

    boolean isAddItemFromHandToCategoryValid(Player player, String[] args);

    boolean isEditItemSellPriceValid(Player player, String[] args);

    boolean isEditItemBuyPriceValid(Player player, String[] args);

    boolean isRemoveItemFromCategoryValid(Player player, String[] args);
}
