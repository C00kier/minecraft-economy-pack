package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;
import pawel.cookier.ignaczak.economypack.shop.repository.ICategoryController;

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
    public boolean doesCategoryContainDisplayName(Category category, String displayName) {
        if (category == null || displayName == null) {
            return false;
        }

        return category.getListOfItems()
                .stream()
                .map(Item::getItemStack)
                .map(ItemStack::getItemMeta)
                .filter(Objects::nonNull)
                .map(ItemMeta::getDisplayName)
                .anyMatch(name -> name.equalsIgnoreCase(displayName));
    }

}

