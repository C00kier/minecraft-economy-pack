package pawel.cookier.ignaczak.economypack.shop_manager.events.repository;

import org.bukkit.inventory.ItemStack;

public interface IShopEventsValidation {
    boolean isClickedItemElementOfNavbar(ItemStack clickedItem);

    boolean isClickedItemPreviousPageButtonIcon(ItemStack clickedItem);

    boolean isClickedItemNextPageButtonIcon(ItemStack clickedItem);

    boolean isClickedItemReturnIcon(ItemStack clickedItem);
}
