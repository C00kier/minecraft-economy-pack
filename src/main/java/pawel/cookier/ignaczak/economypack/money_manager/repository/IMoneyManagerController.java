package pawel.cookier.ignaczak.economypack.money_manager.repository;

import org.bukkit.entity.Player;

public interface IMoneyManagerController {
    void checkBalance(Player player, String[] args);
    void exchangeGold(Player player, String[] args);
    void payToPlayer(Player giver, String[] args);
    void addPlayerManually(Player player, String[] args);
}
