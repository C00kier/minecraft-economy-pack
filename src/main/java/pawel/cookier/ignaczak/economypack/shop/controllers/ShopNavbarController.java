package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopNavbarController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.List;

public class ShopNavbarController implements IShopNavbarController {

    @Override
    public void addNavbarToCategoryInventory(Player player, BalanceManager balanceManager, Inventory inventory) {
        setEmptyFieldsInNavbar(inventory);
        setPlayerHeadInNavbar(player, balanceManager, inventory);
        setReturnInNavbar(inventory);
        setButtonPartInNavbar(inventory);
    }

    @Override
    public void addNavbarToItemInventory(Player player, BalanceManager balanceManager, Inventory inventory) {
        setPlayerHeadInNavbar(player, balanceManager, inventory);
        setReturnInNavbar(inventory);
    }

    private void setPlayerHeadInNavbar(Player player,
                                       BalanceManager balanceManager,
                                       Inventory inventory) {

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

        if (skullMeta != null) {
            skullMeta.setDisplayName("Stan konta");
            skullMeta.setOwningPlayer(player);
            skullMeta.setLore(
                    List.of("%s$".formatted(balanceManager.getBalance(player.getUniqueId()))));

            playerHead.setItemMeta(skullMeta);
            inventory.setItem(PluginConfig.SHOP_NAVBAR_BALANCE_PLACE, playerHead);
        }
    }

    private void setReturnInNavbar(Inventory inventory) {
        ItemStack returnItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = returnItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("Wróć");
            returnItem.setItemMeta(meta);
            inventory.setItem(PluginConfig.SHOP_NAVBAR_RETURN_PLACE, returnItem);
        }
    }

    private void setButtonPartInNavbar(Inventory inventory) {
        ItemStack nextButton = IShopUtility.createItemStack(
                PluginConfig.SHOP_NAVBAR_NEXT_BUTTON_MATERIAL,
                "Następna Strona");
        ItemStack separatorItem = IShopUtility.createItemStack(
                PluginConfig.SHOP_NAVBAR_SEPARATOR_MATERIAL,
                "-");
        ItemStack previousButton = IShopUtility.createItemStack(
                PluginConfig.SHOP_NAVBAR_PREVIOUS_BUTTON_MATERIAL,
                "Poprzednia Strona");

        inventory.setItem(PluginConfig.SHOP_NAVBAR_PREVIOUS_PAGE_BUTTON_PLACE, previousButton);
        inventory.setItem(PluginConfig.SHOP_NAVBAR_BUTTON_SEPARATOR_PLACE, separatorItem);
        inventory.setItem(PluginConfig.SHOP_NAVBAR_NEXT_PAGE_BUTTON_PLACE, nextButton);
    }

    private void setEmptyFieldsInNavbar(Inventory inventory) {
        for (int place : PluginConfig.EMPTY_NAVBAR_FIELDS) {
            inventory.setItem(place, null);
        }
    }
}
