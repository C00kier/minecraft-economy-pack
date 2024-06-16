package pawel.cookier.ignaczak.economypack.controllers;

import org.bukkit.entity.Player;

public interface IMoneyManagerController {
    void checkBalance(Player player, String[] args);
    void exchangeGold(Player player, String[] args);
    void payToPlayer(Player giver, String[] args);
    void addPlayerManually(Player player, String[] args);
}
