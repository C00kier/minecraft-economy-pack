package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import org.bukkit.entity.Player;

public interface ICategoryCommandsController {
    void registerCategoryOpCommands(Player player, String commandName, String[] args);
}
