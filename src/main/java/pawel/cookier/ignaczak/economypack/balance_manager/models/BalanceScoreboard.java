package pawel.cookier.ignaczak.economypack.balance_manager.models;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

@Getter
public class BalanceScoreboard {
    private final Scoreboard board;
    private final Objective objective;

    public BalanceScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) {
            throw new IllegalStateException("ScoreboardManager is not available.");
        }
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective("Serwer", Criteria.DUMMY, ChatColor.DARK_RED + "Cókjer DevServer");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

}
