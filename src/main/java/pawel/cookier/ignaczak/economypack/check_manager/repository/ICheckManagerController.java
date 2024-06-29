package pawel.cookier.ignaczak.economypack.check_manager.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public interface ICheckManagerController {
    void addCheckToPlayerInventory(Player player, JavaPlugin plugin, String[] args);
    void exchangeCheckForMoney(Player player, JavaPlugin plugin, ItemStack item);
}
