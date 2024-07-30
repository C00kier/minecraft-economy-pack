package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.ICategoryInventoryEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
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

    public CategoryInventoryEventsController(ShopEventsUtility utility,
                                             ShopEventsValidation validation,
                                             BalanceManager balanceManager,
                                             JavaPlugin plugin,
                                             ShopNavbarController shopNavbarController,
                                             Shop shop) {
        this.utility = utility;
        this.validation = validation;
        this.balanceManager = balanceManager;
        this.plugin = plugin;
        this.shopNavbarController = shopNavbarController;
        this.shop = shop;
    }

    @Override
    public void openBuyItemMenuLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && event.getClick() == ClickType.LEFT
                    && !validation.isClickedItemElementOfNavbar(clickedItem)) {
                openBuyItemMenu(event, clickedItem);
            }
        }
    }

    @Override
    public void openSellItemMenuRightClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (utility.doesShopContainExistingCategoryByInventory(shop, inventory)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null
                    && event.getClick() == ClickType.RIGHT
                    && !validation.isClickedItemElementOfNavbar(clickedItem)) {
                openSellItemMenu(event, clickedItem);
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
                exchangeAllItemStacksOfSameTypeForMoney(plugin, player, clickedItem);
            }
        }
    }

    private void exchangeAllItemStacksOfSameTypeForMoney(JavaPlugin plugin, Player player, ItemStack itemStack) {
        Inventory inventory = player.getInventory();
        int amountOfItemInInventory = 0;
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            String itemName = formatMaterialName(itemStack.getType().name());
            NamespacedKey key = new NamespacedKey(plugin, "sellPrice");
            Double sellPrice = meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
            if (sellPrice != null) {
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack inventoryItemStack = inventory.getItem(i);
                    if (inventoryItemStack != null
                            && isShopItemStackSameAsInventoryItemStack(itemStack, inventoryItemStack)) {
                        amountOfItemInInventory += inventoryItemStack.getAmount();
                        inventory.setItem(i, null);
                    }
                }
                double moneyToAdd = amountOfItemInInventory * sellPrice;
                balanceManager.addMoneyToPlayer(moneyToAdd, player.getUniqueId());

                if (amountOfItemInInventory != 0) {
                    player.sendMessage(ChatColor.GREEN +
                            "Sprzedałeś %s x [%s] za %s$".formatted(
                                    amountOfItemInInventory,
                                    itemName,
                                    moneyToAdd));
                }
            }
        }
    }

    private boolean isShopItemStackSameAsInventoryItemStack(ItemStack shopItemStack, ItemStack inventoryItemStack) {
        if (shopItemStack == null || inventoryItemStack == null) {
            return false;
        }

        ItemMeta shopMeta = shopItemStack.getItemMeta();
        ItemMeta inventoryMeta = inventoryItemStack.getItemMeta();

        if (shopMeta == null || inventoryMeta == null) {
            return false;
        }

        boolean isDisplayNameEqual = shopMeta.getDisplayName().equals(inventoryMeta.getDisplayName());
        boolean isTypeEqual = shopItemStack.getType() == inventoryItemStack.getType();
        boolean hasSameEnchants = shopMeta.getEnchants().equals(inventoryMeta.getEnchants());

        return isDisplayNameEqual && isTypeEqual && hasSameEnchants;
    }

    private void openBuyItemMenu(InventoryClickEvent event, ItemStack itemStack) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();

        player.openInventory(createItemOperationsInventory(player, itemStack, "Buy item_entity"));
    }

    private void openSellItemMenu(InventoryClickEvent event, ItemStack itemStack) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();

        player.openInventory(createItemOperationsInventory(player, itemStack, "Sell item_entity"));
    }

    private Inventory createItemOperationsInventory(Player player,
                                                    ItemStack itemStack,
                                                    String inventoryTitle){
        Inventory inventory = Bukkit.createInventory(null, PluginConfig.SHOP_INVENTORY_SIZE, inventoryTitle);

        setConfirmIconInInventory(inventory);
        setRemoveQuantityIconsInInventory(inventory);
        setItemIconInInventory(inventory, itemStack);
        setAddQuantityIconsInInventory(inventory);
        shopNavbarController.addNavbarToItemInventory(player, balanceManager, inventory);

        return inventory;
    }

    private void setConfirmIconInInventory(Inventory inventory){
        ItemStack itemStack = IShopUtility.createItemStack(Material.PAPER, ChatColor.AQUA + "Potwierdź");
        inventory.setItem(PluginConfig.SHOP_OPERATIONS_CONFIRM_PLACE, itemStack);
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

    private String formatMaterialName(String materialName) {
        String[] words = materialName.split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
        }
        return formattedName.toString().trim();
    }
}
