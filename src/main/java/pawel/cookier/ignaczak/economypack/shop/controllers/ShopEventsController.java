package pawel.cookier.ignaczak.economypack.shop.controllers;

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
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopEventsController;

import java.util.Objects;
import java.util.Optional;

public class ShopEventsController implements IShopEventsController {

    private final ShopCommandsController shopCommandsController;
    private final ShopController shopController;
    private final BalanceManager balanceManager;

    public ShopEventsController(ShopCommandsController shopCommandsController,
                                ShopController shopController,
                                BalanceManager balanceManager) {
        this.shopCommandsController = shopCommandsController;
        this.shopController = shopController;
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
    public void switchBetweenInventoriesBasedOnItemStack(Shop shop, Player player, ItemStack itemStack) {
        player.closeInventory();

        String displayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, displayName);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Inventory inventoryToOpen = category.getInventory();

            shopCommandsController.openInventory(player, inventoryToOpen);
        }
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
            NamespacedKey key = new NamespacedKey(plugin, "sellPrice");
            Double sellPrice = meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);

            if(sellPrice != null){
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack inventoryItemStack = inventory.getItem(i);
                    if (inventoryItemStack != null && itemStack.isSimilar(inventoryItemStack)) {
                        amountOfItemInInventory += inventoryItemStack.getAmount();
                        inventory.setItem(i, null);
                    }
                }
                double moneyToAdd = amountOfItemInInventory * sellPrice;
                balanceManager.addMoneyToPlayer(moneyToAdd, player.getUniqueId());
            }
        }
    }

}
