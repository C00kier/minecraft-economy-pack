package pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model;

import org.bukkit.inventory.ItemStack;

public class Item {
    private static int itemId = 0;

    private final int id;
    private ItemStack itemStack;
    private double sellPrice;
    private double buyPrice;

    public Item(ItemStack itemStack, double sellPrice, double buyPrice) {
        this.itemStack = itemStack;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.id = itemId++;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }
}
