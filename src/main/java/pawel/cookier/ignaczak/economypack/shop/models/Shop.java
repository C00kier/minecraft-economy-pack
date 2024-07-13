package pawel.cookier.ignaczak.economypack.shop.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private List<Category> categoryList;

    public Shop() {
        this.categoryList = new ArrayList<>();
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
