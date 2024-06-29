package pawel.cookier.ignaczak.economypack.check_manager.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.check_manager.controller.CheckManagerController;

public class CheckEvents implements Listener {

    private final CheckManagerController checkManagerController;
    private final JavaPlugin plugin;

    public CheckEvents(CheckManagerController checkManagerController, JavaPlugin plugin) {
        this.checkManagerController = checkManagerController;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();

            if (item != null && item.hasItemMeta()) {
                long checkValue = checkManagerController.getCheckValue(item, plugin);

                if (checkValue != 0L) {
                    checkManagerController.exchangeCheckForMoney(event.getPlayer(), checkValue);
                    item.setAmount(item.getAmount() - 1);
                }
            }
        }
    }
}
