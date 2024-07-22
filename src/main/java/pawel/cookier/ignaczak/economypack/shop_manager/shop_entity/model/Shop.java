package pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private List<Category> categoryList;
    private Inventory inventory;
    private String shopName;

    public Shop() {
        this.categoryList = new ArrayList<>();
        this.shopName = "Shop";
        this.inventory = Bukkit.createInventory(null, PluginConfig.SHOP_INVENTORY_SIZE, shopName);
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

}
