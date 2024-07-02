package pawel.cookier.ignaczak.economypack.check_manager.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.check_manager.controller.CheckManagerController;

import java.util.Objects;

public class CheckEvents implements Listener {

    private final CheckManagerController checkManagerController;
    private final JavaPlugin plugin;

    public CheckEvents(CheckManagerController checkManagerController, JavaPlugin plugin) {
        this.checkManagerController = checkManagerController;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != null && event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.PAPER) {
            return;
        }

        if (item.hasItemMeta()
                && Objects.requireNonNull(item.getItemMeta())
                .getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "check_value"), PersistentDataType.LONG)) {
            event.setCancelled(true);
            checkManagerController.exchangeCheckForMoney(player, item, plugin);

            int newAmount = item.getAmount() - 1;
            if (newAmount > 0) {
                item.setAmount(newAmount);
            } else {
                item.setAmount(0);
                player.getInventory().remove(item);
            }
        }
    }
}
