package pawel.cookier.ignaczak.economypack.shop.models;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {
    private static int categoryID = 0;

    private final int id;
    private int currentPage;
    private ItemStack categoryItemStack;
    private List<Item> listOfItems;
    private Inventory inventory;

    public Category(ItemStack categoryItemStack) {
        this.currentPage = 1;
        this.categoryItemStack = categoryItemStack;
        this.listOfItems = new ArrayList<>();
        this.id = categoryID++;

        String categoryName = Objects.requireNonNull(categoryItemStack.getItemMeta()).getDisplayName();
        this.inventory = Bukkit.createInventory(null, PluginConfig.SHOP_INVENTORY_SIZE, categoryName);
    }

    public int getId() {
        return id;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ItemStack getCategoryItemStack() {
        return categoryItemStack;
    }

    public void setCategoryItemStack(ItemStack categoryItemStack) {
        this.categoryItemStack = categoryItemStack;
    }

    public List<Item> getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(List<Item> listOfItems) {
        this.listOfItems = listOfItems;
    }
}
