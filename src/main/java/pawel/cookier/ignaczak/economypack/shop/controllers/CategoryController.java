package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;
import pawel.cookier.ignaczak.economypack.shop.repository.ICategoryController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.List;
import java.util.Optional;

public class CategoryController implements ICategoryController {

    @Override
    public void addItemToCategory(Category category, Item item) {
        List<Item> currentList = category.getListOfItems();
        currentList.add(item);
        category.setListOfItems(currentList);
    }

    @Override
    public void removeItemFromCategory(Category category, Item item) {
        List<Item> currentList = category.getListOfItems();
        currentList.remove(item);
        category.setListOfItems(currentList);
    }

    @Override
    public Optional<Item> findItemByName(Category category, String itemName) {
        return category.getListOfItems().stream()
                .filter(item -> {
                    ItemMeta itemMeta = item.getItemStack().getItemMeta();
                    return itemMeta != null && itemMeta.getDisplayName().equalsIgnoreCase(itemName);
                })
                .findFirst();
    }

    @Override
    public void editCategoryItemStackName(Category category, String newName) {
        ItemStack itemStack = category.getCategoryItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(newName);
            itemStack.setItemMeta(itemMeta);
            category.setCategoryItemStack(itemStack);
        }
    }

    @Override
    public void editCategoryItemStackMaterial(Category category, ItemStack newItemStack) {
        category.setCategoryItemStack(newItemStack);
    }

    @Override
    public void setCategoryInventoryByPage(BalanceManager balanceManager,
                                           Player player,
                                           Category category,
                                           int pageToDisplay) {
        Inventory inventory = category.getInventory();
        inventory.clear();

        List<ItemStack> itemStacksList = category.getListOfItems().stream()
                .map(Item::getItemStack)
                .toList();

        int totalItems = itemStacksList.size();
        int start = (pageToDisplay - 1) * PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP;
        int end = Math.min(start + PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP, totalItems);

        for (int i = start; i < end; i++) {
            inventory.setItem(i - start, itemStacksList.get(i));
        }

        addNavBarToInventory(player, balanceManager, inventory);
        category.setCurrentPage(pageToDisplay);
    }

    private void addNavBarToInventory(Player player, BalanceManager balanceManager, Inventory inventory) {
        setEmptyFieldsInNavbar(inventory);
        setPlayerHeadInNavbar(player, balanceManager, inventory);
        setReturnInNavbar(inventory);
        setButtonPartInNavbar(inventory);
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
            meta.setDisplayName("Wyjdź");
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

