package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.ICategoryInventoryEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.controller.ItemController;
import pawel.cookier.ignaczak.economypack.shop_manager.navbar.controller.ShopNavbarController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;
import pawel.cookier.ignaczak.economypack.shop_manager.utility.IShopUtility;

public class CategoryInventoryEventsController implements ICategoryInventoryEventsController {

    private final ShopEventsUtility utility;
    private final ShopEventsValidation validation;
    private final BalanceManager balanceManager;
    private final JavaPlugin plugin;
    private final ShopNavbarController shopNavbarController;
    private final Shop shop;
    private final ItemController itemController;

    public CategoryInventoryEventsController(ShopEventsUtility utility,
                                             ShopEventsValidation validation,
                                             BalanceManager balanceManager,
                                             JavaPlugin plugin,
                                             ShopNavbarController shopNavbarController,
                                             Shop shop, ItemController itemController) {
        this.utility = utility;
        this.validation = validation;
        this.balanceManager = balanceManager;
        this.plugin = plugin;
        this.shopNavbarController = shopNavbarController;
        this.shop = shop;
        this.itemController = itemController;
    }

    @Override
    public void openItemMenuLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && event.getClick() == ClickType.LEFT
                    && !validation.isClickedItemElementOfNavbar(clickedItem)) {
                openItemMenu(event, clickedItem);
            }
        }
    }

    @Override
    public void sellAllItemsOfCertainTypeShiftRightClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (isItemStackInCurrentlyOpenInventory(inventory, clickedItem)
                    && event.getClick() == ClickType.SHIFT_RIGHT) {
                Player player = (Player) event.getWhoClicked();
                itemController.exchangeItemsForMoney(plugin, player, clickedItem, true);
            }
        }
    }


    private void openItemMenu(InventoryClickEvent event, ItemStack itemStack) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();

        player.openInventory(createItemOperationsInventory(player, itemStack, "Kup / Sprzedaj"));
    }

    private Inventory createItemOperationsInventory(Player player,
                                                    ItemStack itemStack,
                                                    String inventoryTitle){
        Inventory inventory = Bukkit.createInventory(null, PluginConfig.SHOP_INVENTORY_SIZE, inventoryTitle);

        setOperationsIconInInventory(inventory);
        setRemoveQuantityIconsInInventory(inventory);
        setItemIconInInventory(inventory, itemStack);
        setAddQuantityIconsInInventory(inventory);
        shopNavbarController.addNavbarToItemInventory(player, balanceManager, inventory);

        return inventory;
    }

    private void setOperationsIconInInventory(Inventory inventory){
        ItemStack buyButton = IShopUtility.createItemStack(Material.PAPER, ChatColor.GREEN + "Kup");
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_BUY_BUTTON_PLACE, buyButton);

        ItemStack sellButton = IShopUtility.createItemStack(Material.PAPER, ChatColor.RED + "Sprzedaj");
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_SELL_BUTTON_PLACE, sellButton);
    }

    private void setRemoveQuantityIconsInInventory(Inventory inventory){
        ItemStack remove1ItemStack =
                IShopUtility.createItemStack(Material.RED_STAINED_GLASS_PANE, "Zmniejsz o 1");
        ItemStack remove16ItemStack =
                IShopUtility.createItemStack(Material.RED_STAINED_GLASS_PANE, "Zmniejsz o 16");
        ItemStack remove64ItemStack =
                IShopUtility.createItemStack(Material.RED_STAINED_GLASS_PANE, "Zmniejsz o 64");

        remove1ItemStack.setAmount(1);
        remove16ItemStack.setAmount(16);
        remove64ItemStack.setAmount(64);

        inventory.setItem(PluginConfig.SHOP_OPERATIONS_MINUS_1_PLACE, remove1ItemStack);
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_MINUS_16_PLACE, remove16ItemStack);
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_MINUS_64_PLACE, remove64ItemStack);
    }

    private void setItemIconInInventory(Inventory inventory, ItemStack itemStack){
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_ITEM_PLACE, itemStack);
    }

    private void setAddQuantityIconsInInventory(Inventory inventory){
        ItemStack add1ItemStack =
                IShopUtility.createItemStack(Material.LIME_STAINED_GLASS_PANE, "Zwiększ o 1");
        ItemStack add16ItemStack =
                IShopUtility.createItemStack(Material.LIME_STAINED_GLASS_PANE, "Zwiększ o 16");
        ItemStack add64ItemStack =
                IShopUtility.createItemStack(Material.LIME_STAINED_GLASS_PANE, "Zwiększ o 64");

        add1ItemStack.setAmount(1);
        add16ItemStack.setAmount(16);
        add64ItemStack.setAmount(64);

        inventory.setItem(PluginConfig.SHOP_OPERATIONS_PLUS_1_PLACE, add1ItemStack);
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_PLUS_16_PLACE, add16ItemStack);
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_PLUS_64_PLACE, add64ItemStack);
    }

    private boolean isItemStackInCurrentlyOpenInventory(Inventory inventory, ItemStack itemStack) {
        return inventory.contains(itemStack);
    }

}
