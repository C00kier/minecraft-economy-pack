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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoryController implements ICategoryController {

    @Override
    public void addItemToCategory(Category category, Item item) {
        List<Item> currentList = new ArrayList<>(category.getListOfItems());
        currentList.add(item);
        category.setListOfItems(currentList);
        updateCategory(category);
    }

    @Override
    public void removeItemFromCategory(Category category, Item item) {
        List<Item> currentList = new ArrayList<>(category.getListOfItems());
        currentList.remove(item);
        category.setListOfItems(currentList);
        updateCategory(category);
    }

    @Override
    public Optional<Item> findItemByName(Category category, String itemName) {
        return category.getListOfItems()
                .stream()
                .filter(item ->
                        Objects.requireNonNull(item.getItemStack().getItemMeta())
                                .getDisplayName()
                                .equalsIgnoreCase(itemName))
                .findFirst();
    }

    @Override
    public void editCategoryItemStackName(Category category, String newName) {
        ItemStack itemStack = category.getCategoryItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.setDisplayName(newName);
        itemStack.setItemMeta(itemMeta);

        category.setCategoryItemStack(itemStack);
    }

    @Override
    public void editCategoryItemStackMaterial(Category category, ItemStack newItemStack) {
        category.setCategoryItemStack(newItemStack);
    }

    @Override
    public void updateCategory(Category category) {
        category.getInventory().clear();
        category.getInventory().setContents(
                category.getListOfItems()
                        .stream()
                        .map(Item::getItemStack)
                        .toArray(ItemStack[]::new));
    }

    @Override
    public void addNavBarToInventory(Player player,
                                     BalanceManager balanceManager,
                                     Inventory inventory,
                                     Material buttonMaterial,
                                     Material separatorMaterial,
                                     Material backButtonMaterial){

        setEmptyFieldsInNavbar(inventory);
        setPlayerHeadInNavbar(player, balanceManager, inventory);
        setReturnInNavbar(inventory);
        setButtonPartInNavbar(inventory, buttonMaterial, separatorMaterial, backButtonMaterial);
    }

    private void setPlayerHeadInNavbar(Player player,
                                       BalanceManager balanceManager,
                                       Inventory inventory){

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if(skullMeta != null){
            skullMeta.setDisplayName("Stan konta");
            skullMeta.setOwningPlayer(player);
            skullMeta.setLore(
                    List.of("%s$".formatted(balanceManager.getBalance(player.getUniqueId()))));

            playerHead.setItemMeta(skullMeta);
            inventory.setItem(PluginConfig.SHOP_NAVBAR_BALANCE_PLACE, playerHead);
        }
    }

    private void setReturnInNavbar(Inventory inventory){
        ItemStack returnItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = returnItem.getItemMeta();

        if(meta != null){
            meta.setDisplayName("Wróć");
            returnItem.setItemMeta(meta);
            inventory.setItem(PluginConfig.SHOP_NAVBAR_RETURN_PLACE, returnItem);
        }
    }

    private void setButtonPartInNavbar(Inventory inventory,
                                       Material buttonMaterial,
                                       Material separatorMaterial,
                                       Material backButtonMaterial){
        ItemStack nextButton = IShopUtility.createItemStack(buttonMaterial, "Następna Strona");
        ItemStack separatorItem = IShopUtility.createItemStack(separatorMaterial, null);
        ItemStack previousButton = IShopUtility.createItemStack(backButtonMaterial, "Poprzednia Strona");

        inventory.setItem(PluginConfig.SHOP_NAVBAR_PREVIOUS_PAGE_BUTTON_PLACE, previousButton);
        inventory.setItem(PluginConfig.SHOP_NAVBAR_BUTTON_SEPARATOR_PLACE, separatorItem);
        inventory.setItem(PluginConfig.SHOP_NAVBAR_NEXT_PAGE_BUTTON_PLACE, nextButton);
    }

    private void setEmptyFieldsInNavbar(Inventory inventory){
        for (int place: PluginConfig.EMPTY_NAVBAR_FIELDS) {
            inventory.setItem(place, null);
        }
    }
}

