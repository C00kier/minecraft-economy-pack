package pawel.cookier.ignaczak.economypack.shop.models;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private static int categoryID = 0;

    private final int id;
    private String name;
    private ItemStack categoryItemStack;
    private List<Item> listOfItems;

    public Category(String name, ItemStack categoryItemStack) {
        this.name = name;
        this.categoryItemStack = categoryItemStack;
        this.listOfItems = new ArrayList<>();
        this.id = categoryID++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
