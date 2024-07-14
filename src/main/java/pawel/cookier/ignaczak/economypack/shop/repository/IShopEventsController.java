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
    void switchBetweenInventoriesBasedOnItemStack(Shop shop, Player player, ItemStack itemStack);
    boolean doesShopContainExistingCategoryByInventory(Shop shop, Inventory inventory);
    void exchangeAllItemStacksOfSameTypeForMoney(JavaPlugin plugin, Player player, ItemStack itemStack);
}
