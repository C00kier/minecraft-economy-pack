package pawel.cookier.ignaczak.economypack.shop_manager.events.validation;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.IShopEventsValidation;

public class ShopEventsValidation implements IShopEventsValidation {

    @Override
    public boolean isClickedItemElementOfNavbar(ItemStack clickedItem) {
        if(clickedItem == null) return false;
        return isClickedItemBalanceIcon(clickedItem)
                || isClickedItemPreviousPageButtonIcon(clickedItem)
                || isClickedItemSeparatorIcon(clickedItem)
                || isClickedItemNextPageButtonIcon(clickedItem)
                || isClickedItemReturnIcon(clickedItem);
    }

    @Override
    public boolean isClickedItemPreviousPageButtonIcon(ItemStack clickedItem) {
        if(clickedItem == null) return false;
        return doesItemStackContainsMaterialAndDisplayName(clickedItem,
                PluginConfig.SHOP_NAVBAR_PREVIOUS_BUTTON_MATERIAL,
                "Poprzednia Strona");
    }

    @Override
    public boolean isClickedItemNextPageButtonIcon(ItemStack clickedItem) {
        if(clickedItem == null) return false;
        return doesItemStackContainsMaterialAndDisplayName(clickedItem,
                PluginConfig.SHOP_NAVBAR_NEXT_BUTTON_MATERIAL,
                "Następna Strona");
    }

    @Override
    public boolean isClickedItemReturnIcon(ItemStack clickedItem) {
        if(clickedItem == null) return false;
        return doesItemStackContainsMaterialAndDisplayName(clickedItem, Material.BARRIER, "Wróć");
    }

    @Override
    public boolean isClickedItemQuantityButton(ItemStack clickedItem, String buttonName, Material material, int amount) {
        if(clickedItem == null) return false;
        return isCorrectButtonName(clickedItem, buttonName)
                && isCorrectItemMaterial(clickedItem, material)
                && isClickedItemAmountCorrect(clickedItem, amount);
    }

    @Override
    public boolean isBuySellItemMenu(InventoryClickEvent event) {
        return event.getView().getTitle().equalsIgnoreCase("Kup / Sprzedaj");
    }

    private boolean isCorrectButtonName(ItemStack clickedItem, String buttonName){
        ItemMeta meta = clickedItem.getItemMeta();
        if(meta == null){
            return false;
        }
        return clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(buttonName);
    }

    private boolean isCorrectItemMaterial(ItemStack clickedItem, Material material){
        return clickedItem.getType() == material;
    }

    private boolean isClickedItemAmountCorrect(ItemStack clickedItem, int amount){
        return clickedItem.getAmount() == amount;
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
