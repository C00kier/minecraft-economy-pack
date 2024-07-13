package pawel.cookier.ignaczak.economypack.shop.models;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private static int categoryID = 0;

    private final int id;
    private ItemStack categoryItemStack;
    private List<Item> listOfItems;

    public Category(ItemStack categoryItemStack) {
        this.categoryItemStack = categoryItemStack;
        this.listOfItems = new ArrayList<>();
        this.id = categoryID++;
    }

    public int getId() {
        return id;
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
