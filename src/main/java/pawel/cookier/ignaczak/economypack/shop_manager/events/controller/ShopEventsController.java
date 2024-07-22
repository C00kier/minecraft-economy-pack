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
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.controller.CategoryController;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.controller.ShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.IShopEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.navbar.controller.ShopNavbarController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.utility.IShopUtility;

import java.util.Objects;

public class ShopEventsController implements IShopEventsController {

    private final ShopCommandsController shopCommandsController;
    private final ShopController shopController;
    private final CategoryController categoryController;
    private final BalanceManager balanceManager;
    private final ShopNavbarController shopNavbarController;

    public ShopEventsController(ShopCommandsController shopCommandsController,
                                ShopController shopController,
                                CategoryController categoryController,
                                BalanceManager balanceManager,
                                ShopNavbarController shopNavbarController) {
        this.shopCommandsController = shopCommandsController;
        this.shopController = shopController;
        this.categoryController = categoryController;
        this.balanceManager = balanceManager;
        this.shopNavbarController = shopNavbarController;
    }

    @Override
    public boolean isItemStackInCurrentlyOpenInventory(Inventory inventory, ItemStack itemStack) {
        return inventory.contains(itemStack);
    }

    @Override
    public boolean isShiftMouseClick(InventoryClickEvent event) {
        return event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
    }

    @Override
    public boolean doesShopContainExistingCategoryByInventory(Shop shop, Inventory inventory) {
        return shop.getCategoryList()
                .stream()
                .anyMatch(category -> category.getInventory().equals(inventory));
    }

    @Override
    public void exchangeAllItemStacksOfSameTypeForMoney(JavaPlugin plugin, Player player, ItemStack itemStack) {
        Inventory inventory = player.getInventory();
        int amountOfItemInInventory = 0;
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            String itemName = IShopUtility.formatMaterialName(itemStack.getType().name());
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

    @Override
    public void clickCategoryEvent(Shop shop, InventoryClickEvent event, ItemStack itemStack) {
        String categoryName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();

        shopController.findCategoryByName(shop, categoryName).ifPresent(category -> {
            category.setCurrentPage(1);
            Player player = (Player) event.getWhoClicked();
            switchToCategoryInventory(player, category);
        });
    }

    @Override
    public void nextButtonClickEvent(Shop shop, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        shopController.findCategoryByInventory(shop, inventory).ifPresent(category -> {
            int itemsInCategory = category.getListOfItems().size();
            int totalPages = (int) Math.ceil((double) itemsInCategory / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
            int currentPage = category.getCurrentPage();

            if (currentPage < totalPages) {
                category.setCurrentPage(currentPage + 1);
                Player player = (Player) event.getWhoClicked();
                switchToCategoryInventory(player, category);
            }
        });
    }

    @Override
    public void previousButtonClickEvent(Shop shop, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        shopController.findCategoryByInventory(shop, inventory).ifPresent(category -> {
            int currentPage = category.getCurrentPage();

            if (currentPage > 1) {
                category.setCurrentPage(currentPage - 1);
                Player player = (Player) event.getWhoClicked();
                switchToCategoryInventory(player, category);
            }
        });
    }

    @Override
    public void backButtonClickEvent(Shop shop, InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        shopCommandsController.openInventory(player, shop.getInventory());
    }

    @Override
    public void openBuyItemMenu(InventoryClickEvent event, ItemStack itemStack) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();

        player.openInventory(createItemOperationsInventory(player, itemStack, "Buy item_entity"));
    }

    @Override
    public void openSellItemMenu(InventoryClickEvent event, ItemStack itemStack) {
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

    private void switchToCategoryInventory(Player player,
                                           Category category) {
        player.closeInventory();

        int itemsInCategory = category.getListOfItems().size();
        int totalPages = (int) Math.ceil((double) itemsInCategory / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
        int pageToOpen = category.getCurrentPage();

        if (pageToOpen <= totalPages) {
            categoryController.displayCategoryInventoryBasedByPage(balanceManager, player, category, pageToOpen);
        }

        shopCommandsController.openInventory(player, category.getInventory());
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

}
