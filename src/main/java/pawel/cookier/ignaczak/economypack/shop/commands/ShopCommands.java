package pawel.cookier.ignaczak.economypack.shop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pawel.cookier.ignaczak.economypack.shop.controllers.MenuController;

public class ShopCommands implements CommandExecutor{

    private final MenuController menuController;

    public ShopCommands(MenuController menuController) {
        this.menuController = menuController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if(sender instanceof Player player && command.getName().equalsIgnoreCase("shop")){
            menuController.openInventoryMenu(player);
        }
        return true;
    }
}
