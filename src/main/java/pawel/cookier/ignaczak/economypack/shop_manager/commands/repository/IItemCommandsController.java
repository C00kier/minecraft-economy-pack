package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface IItemCommandsController {
    void registerItemOpCommands(JavaPlugin plugin, Player player, String commandName, String[] args);
}
