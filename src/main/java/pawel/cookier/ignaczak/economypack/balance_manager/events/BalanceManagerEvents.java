package pawel.cookier.ignaczak.economypack.balance_manager.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;

import java.util.UUID;

public class BalanceManagerEvents implements Listener {

    private final BalanceManager balanceManager;

    public BalanceManagerEvents(BalanceManager balanceManager) {
        this.balanceManager = balanceManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (!balanceManager.containsPlayer(playerUUID)) {
            balanceManager.setBalance(playerUUID, 0d);
        }
    }

}
