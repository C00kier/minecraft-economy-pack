package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopEventsController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.Objects;

public class ShopEventsController implements IShopEventsController {

    private final ShopCommandsController shopCommandsController;
    private final ShopController shopController;
    private final CategoryController categoryController;
    private final BalanceManager balanceManager;

    public ShopEventsController(ShopCommandsController shopCommandsController,
                                ShopController shopController,
                                CategoryController categoryController, BalanceManager balanceManager) {
        this.shopCommandsController = shopCommandsController;
        this.shopController = shopController;
        this.categoryController = categoryController;
        this.balanceManager = balanceManager;
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

        player.openInventory(createItemOperationsInventory(itemStack, "Buy item"));
    }

    @Override
    public void openSellItemMenu(InventoryClickEvent event, ItemStack itemStack) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();

        player.openInventory(createItemOperationsInventory(itemStack, "Sell item"));
    }

    private Inventory createItemOperationsInventory(ItemStack itemStack, String inventoryTitle){
        return Bukkit.createInventory(null, PluginConfig.SHOP_INVENTORY_SIZE, inventoryTitle);
    }


    private void switchToCategoryInventory(Player player,
                                           Category category) {
        player.closeInventory();

        int itemsInCategory = category.getListOfItems().size();
        int totalPages = (int) Math.ceil((double) itemsInCategory / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
        int pageToOpen = category.getCurrentPage();

        if (pageToOpen <= totalPages) {
            categoryController.setCategoryInventoryByPage(balanceManager, player, category, pageToOpen);
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
