package pawel.cookier.ignaczak.economypack.shop_manager.navbar.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;

public interface IShopNavbarController {
    void addNavbarToCategoryInventory(Player player, BalanceManager balanceManager, Inventory inventory);

    void addNavbarToItemInventory(Player player, BalanceManager balanceManager, Inventory inventory);
}
