package pawel.cookier.ignaczak.economypack.shop_manager.events.validation;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.IShopEventsValidation;

public class ShopEventsValidation implements IShopEventsValidation {

    @Override
    public boolean isClickedItemElementOfNavbar(ItemStack clickedItem) {
        return isClickedItemBalanceIcon(clickedItem)
                || isClickedItemPreviousPageButtonIcon(clickedItem)
                || isClickedItemSeparatorIcon(clickedItem)
                || isClickedItemNextPageButtonIcon(clickedItem)
                || isClickedItemReturnIcon(clickedItem);
    }

    @Override
    public boolean isClickedItemPreviousPageButtonIcon(ItemStack clickedItem) {
        return doesItemStackContainsMaterialAndDisplayName(clickedItem,
                PluginConfig.SHOP_NAVBAR_PREVIOUS_BUTTON_MATERIAL,
                "Poprzednia Strona");
    }

    @Override
    public boolean isClickedItemNextPageButtonIcon(ItemStack clickedItem) {
        return doesItemStackContainsMaterialAndDisplayName(clickedItem,
                PluginConfig.SHOP_NAVBAR_NEXT_BUTTON_MATERIAL,
                "Następna Strona");
    }

    @Override
    public boolean isClickedItemReturnIcon(ItemStack clickedItem) {
        return doesItemStackContainsMaterialAndDisplayName(clickedItem, Material.BARRIER, "Wróć");
    }

    private boolean isClickedItemBalanceIcon(ItemStack clickedItem) {
        return doesItemStackContainsMaterialAndDisplayName(clickedItem, Material.PLAYER_HEAD, "Stan konta");
    }

    private boolean isClickedItemSeparatorIcon(ItemStack clickedItem) {
        return doesItemStackContainsMaterialAndDisplayName(clickedItem,
                PluginConfig.SHOP_NAVBAR_SEPARATOR_MATERIAL,
                "-");
    }

    private boolean doesItemStackContainsMaterialAndDisplayName(ItemStack itemStack,
                                                                Material materialType,
                                                                String displayName) {
        if (itemStack.getType() != materialType) return false;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            return meta.getDisplayName().equals(displayName);
        }

        return false;
    }
}
