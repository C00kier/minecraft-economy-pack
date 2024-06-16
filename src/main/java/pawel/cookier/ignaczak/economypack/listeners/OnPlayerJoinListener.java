package pawel.cookier.ignaczak.economypack.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pawel.cookier.ignaczak.economypack.managers.BalanceManager;
import pawel.cookier.ignaczak.economypack.managers.ScoreboardHandler;

public class OnPlayerJoinListener implements Listener {

    private final BalanceManager balanceManager;
    private final ScoreboardHandler scoreboardHandler;

    public OnPlayerJoinListener(BalanceManager balanceManager, ScoreboardHandler scoreboardHandler) {
        this.balanceManager = balanceManager;
        this.scoreboardHandler = scoreboardHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        if (!balanceManager.containsPlayer(playerName)) {
            balanceManager.setBalance(playerName, 0L);
        }

        scoreboardHandler.updateMoney(event.getPlayer());
    }

}
