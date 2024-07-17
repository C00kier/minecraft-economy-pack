package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;

public interface IShopEventsController {
    boolean isItemStackInCurrentlyOpenInventory(Inventory inventory, ItemStack itemStack);

    boolean isShiftMouseClick(InventoryClickEvent event);

    boolean doesShopContainExistingCategoryByInventory(Shop shop, Inventory inventory);

    void exchangeAllItemStacksOfSameTypeForMoney(JavaPlugin plugin, Player player, ItemStack itemStack);

    void clickCategoryEvent(Shop shop, InventoryClickEvent event, ItemStack itemStack);

    void nextButtonClickEvent(Shop shop, InventoryClickEvent event);

    void previousButtonClickEvent(Shop shop, InventoryClickEvent event);

    void backButtonClickEvent(Shop shop, InventoryClickEvent event);
}
