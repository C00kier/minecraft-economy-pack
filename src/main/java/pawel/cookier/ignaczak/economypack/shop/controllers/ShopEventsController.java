package pawel.cookier.ignaczak.economypack.shop.controllers;

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
import java.util.Optional;

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
        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setCurrentPage(1);

            Player player = (Player) event.getWhoClicked();
            switchToCategoryInventoryBasedOnNameAndPageNumber(shop, player, categoryName, category.getCurrentPage());
        }
    }

    @Override
    public void nextButtonClickEvent(Shop shop, InventoryClickEvent event){
        Inventory inventory = event.getInventory();

        Optional<Category> optionalCategory = shopController.findCategoryByInventory(shop, inventory);
        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            String categoryName = categoryController.getCategoryNameByCategory(category);
            int totalPages = (int) Math.ceil((double) inventory.getSize() / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
            int currentPage = category.getCurrentPage();

            if(currentPage < totalPages){
                category.setCurrentPage(category.getCurrentPage() + 1);
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("strona do włączenia " + category.getCurrentPage());
                switchToCategoryInventoryBasedOnNameAndPageNumber(shop, player, categoryName, category.getCurrentPage());
            }
        }
    }

    private void switchToCategoryInventoryBasedOnNameAndPageNumber(Shop shop,
                                                                  Player player,
                                                                  String categoryName,
                                                                  int page) {
        player.closeInventory();

        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            categoryController.updateCategory(category);
            Inventory inventoryToOpen = category.getInventory();
            int inventorySize = inventoryToOpen.getSize();

            int totalPages = (int) Math.ceil((double) inventorySize / PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP);
            page = Math.max(1, Math.min(page, totalPages));

            int start = (page - 1) * PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP;
            int end = Math.min(start + PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP, inventorySize);

            if(page != 1){
                for (int i = 0; i < PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP; i++) {
                    inventoryToOpen.setItem(i, null);
                }
            }

            for (int i = start; i < end; i++) {
                inventoryToOpen.setItem(i - start, inventoryToOpen.getItem(i));
            }

            categoryController.addNavBarToInventory(player,
                    balanceManager,
                    inventoryToOpen,
                    PluginConfig.SHOP_NAVBAR_NEXT_BUTTON_MATERIAL,
                    PluginConfig.SHOP_NAVBAR_SEPARATOR_MATERIAL,
                    PluginConfig.SHOP_NAVBAR_PREVIOUS_BUTTON_MATERIAL);

            shopCommandsController.openInventory(player, inventoryToOpen);
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

}
