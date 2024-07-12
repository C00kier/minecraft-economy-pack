package pawel.cookier.ignaczak.economypack.config;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ShopConfig {
    //names (move to translation in the feature)
    private final static String SHOP_MAIN_NAME = "Sklep";

    //size
    public final static int SHOP_FIELDS_TO_FILL_UP = 36;
    private final static int SHOP_SIZE = 45;

    //inventories
    public static Inventory SHOP_MAIN_INVENTORY = Bukkit.createInventory(null, SHOP_SIZE, SHOP_MAIN_NAME);

}
