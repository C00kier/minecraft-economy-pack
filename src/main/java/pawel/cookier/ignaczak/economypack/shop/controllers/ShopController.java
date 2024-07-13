package pawel.cookier.ignaczak.economypack.shop.controllers;

import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopController implements IShopController {

    @Override
    public void addCategoryToShop(Shop shop, Category category) {
        List<Category> currentCategories = new ArrayList<>(shop.getCategoryList());
        currentCategories.add(category);
        shop.setCategoryList(currentCategories);
    }

    @Override
    public void removeCategoryFromShop(Shop shop, Category category) {
        List<Category> currentCategories = new ArrayList<>(shop.getCategoryList());
        currentCategories.remove(category);
        shop.setCategoryList(currentCategories);
    }

    @Override
    public Optional<Category> findCategoryByName(Shop shop, String categoryName) {
        return shop.getCategoryList()
                .stream()
                .filter(category -> category.getName().equalsIgnoreCase(categoryName))
                .findFirst();

    }

    @Override
    public void updateShop() {

    }

    @Override
    public void loadShopFromFile() {

    }
}
