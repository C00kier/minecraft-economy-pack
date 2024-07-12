package pawel.cookier.ignaczak.economypack.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.utility.IShopUtility;

public class ShopConfig {
    //names
    public final static String SHOP_MAIN_NAME = "Sklep";

    //size
    private final static int SHOP_MAIN_SIZE = 45;

    //inventories
    public static Inventory SHOP_MAIN_INVENTORY = Bukkit.createInventory(null,SHOP_MAIN_SIZE, SHOP_MAIN_NAME);

    //inventory categories
    private final static ItemStack BUILDING_MATERIALS_ITEM = IShopUtility.createItemStack(
            Material.BRICKS, ChatColor.AQUA + "BUDOWLANE");
    private final static ItemStack ORES_ITEM = IShopUtility.createItemStack(
            Material.GOLD_INGOT, ChatColor.AQUA + "MATERIAŁY");
    private final static ItemStack FOOD_ITEM = IShopUtility.createItemStack(
            Material.COOKED_BEEF, ChatColor.AQUA + "JEDZENIE");
    private final static ItemStack MOBS_ITEM = IShopUtility.createItemStack(
            Material.BONE, ChatColor.AQUA + "MOBY");
    private final static ItemStack PLANTS_ITEM = IShopUtility.createItemStack(
            Material.WHEAT_SEEDS, ChatColor.AQUA + "ROŚLINY");
    private final static ItemStack ENCHANTS_ITEM = IShopUtility.createItemStack(
            Material.ENCHANTED_BOOK, ChatColor.AQUA + "ENCHANTY");
    private final static ItemStack EGGS_ITEM = IShopUtility.createItemStack(
            Material.BEE_SPAWN_EGG, ChatColor.AQUA + "JAJA SPAWNUJĄCE");
    private final static ItemStack SPAWNERS_ITEM = IShopUtility.createItemStack(
            Material.SPAWNER, ChatColor.AQUA + "SPAWNERY");
    private final static ItemStack OTHERS_ITEM = IShopUtility.createItemStack(
            Material.LIGHT_BLUE_DYE, ChatColor.AQUA + "INNE");

    //shop main setup
    static {
        SHOP_MAIN_INVENTORY.setItem(11, BUILDING_MATERIALS_ITEM);
        SHOP_MAIN_INVENTORY.setItem(12, ORES_ITEM);
        SHOP_MAIN_INVENTORY.setItem(13, FOOD_ITEM);
        SHOP_MAIN_INVENTORY.setItem(14, MOBS_ITEM);
        SHOP_MAIN_INVENTORY.setItem(15, PLANTS_ITEM);
        SHOP_MAIN_INVENTORY.setItem(21, ENCHANTS_ITEM);
        SHOP_MAIN_INVENTORY.setItem(22, EGGS_ITEM);
        SHOP_MAIN_INVENTORY.setItem(23, SPAWNERS_ITEM);
        SHOP_MAIN_INVENTORY.setItem(31, OTHERS_ITEM);
    }

}
