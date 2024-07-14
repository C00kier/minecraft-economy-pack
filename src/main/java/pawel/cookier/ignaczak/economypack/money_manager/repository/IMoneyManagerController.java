package pawel.cookier.ignaczak.economypack.money_manager.repository;

import org.bukkit.entity.Player;

public interface IMoneyManagerController {
    void exchangeGold(Player player, String[] args);
    void payToPlayer(Player giver, String[] args);
}
