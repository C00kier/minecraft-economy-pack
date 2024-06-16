package pawel.cookier.ignaczak.economypack.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler implements IScoreboardHandler {
    private final Scoreboard board;
    private final Objective objective;
    private final BalanceManager balanceManager;

    public ScoreboardHandler(BalanceManager balanceManager) {
        this.balanceManager = balanceManager;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) {
            throw new IllegalStateException("ScoreboardManager is not available.");
        }
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective("Serwer", Criteria.DUMMY, ChatColor.DARK_RED + "Cókjer DevServer");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public void updateMoney(Player player) {
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        String playerName = player.getName();
        Long balance = balanceManager.getBalance(playerName);

        Score user = objective.getScore(ChatColor.LIGHT_PURPLE + "Gracz: " + playerName);
        Score moneyScore = objective.getScore(ChatColor.GOLD + "%s$".formatted(balance));

        user.setScore(2);
        moneyScore.setScore(1);

        player.setScoreboard(board);
    }
}
