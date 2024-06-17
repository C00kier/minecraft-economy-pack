package pawel.cookier.ignaczak.economypack.gambling.repository;

import org.bukkit.entity.Player;

public interface IGamblingController {
    void runGamble(Player player, String[] args);
    void runSlots(Player player, String[] args);
}
