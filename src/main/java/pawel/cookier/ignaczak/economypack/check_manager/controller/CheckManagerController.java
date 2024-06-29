package pawel.cookier.ignaczak.economypack.check_manager.controller;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.check_manager.repository.ICheckManagerController;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.scoreboard.controllers.ScoreboardHandler;


public class CheckManagerController implements ICheckManagerController {

    //Part to integrate with your money system
    private final BalanceManager balanceManager;
    private final ScoreboardHandler scoreboardHandler;

    public CheckManagerController(BalanceManager balanceManager, ScoreboardHandler scoreboardHandler) {
        this.balanceManager = balanceManager;
        this.scoreboardHandler = scoreboardHandler;
    }

    //

    @Override
    public void addCheckToPlayerInventory(Player player, JavaPlugin plugin, String[] args) {
        boolean isValid = createCheckValidation(player, args);

        if (isValid) {
            long checkValue = Long.parseLong(args[0]);
            ItemStack checkItem = createCheckItem(plugin, checkValue);
            player.getInventory().addItem(checkItem);
            player.sendMessage(ChatColor.GREEN + "Dodano czek o wartości %s$ do ekwipunku".formatted(checkValue));

            //remove money from account
            String playerName = player.getName();
            Long playerBalance = balanceManager.getBalance(playerName);
            balanceManager.setBalance(playerName,playerBalance - checkValue);
            scoreboardHandler.updateMoney(player);
            //
        }

    }

    @Override
    public void exchangeCheckForMoney(Player player, Long checkValue) {
        // your logic to add money to account
        String playerName = player.getName();
        long currentBalance = balanceManager.getBalance(playerName);
        balanceManager.setBalance(playerName, currentBalance + checkValue);
        scoreboardHandler.updateMoney(player);
        player.sendMessage(ChatColor.GREEN + "Wymieniono czek na %s$".formatted(checkValue));
        //
    }

    @Override
    public long getCheckValue(ItemStack item, JavaPlugin plugin) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                NamespacedKey key = new NamespacedKey(plugin, "check_value");
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                    Long checkValue = meta.getPersistentDataContainer().get(key, PersistentDataType.LONG);
                    if (checkValue != null) {
                        return checkValue;
                    }
                }
            }
        }
        return 0L;
    }

    private boolean createCheckValidation(Player player, String[] args) {
        return validateArgsLength(player, args)
                && validateArgsValue(player, args)
                && validateInventorySpace(player)
                && validatePlayerAccount(player, args);
    }

    private boolean validateArgsLength(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(
                    ChatColor.RED + "Aby skorzystać z komendy musisz podać wartość czeku (np. /%s 100)"
                            .formatted(PluginConfig.CHECK_COMMAND));
            return false;
        }
        return true;
    }

    private boolean validateArgsValue(Player player, String[] args) {
        try {
            long checkValue = Long.parseLong(args[0]);
            if (checkValue <= 0L) {
                player.sendMessage(ChatColor.RED + "Wartość czeku musi być większa od 0");
                return false;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(
                    ChatColor.RED + "Wartość czeku musi być liczbą całkowitą (Wprowadzono: %s)".formatted(args[0]));
            return false;
        }
        return true;
    }

    private boolean validateInventorySpace(Player player) {
        Inventory playerInventory = player.getInventory();

        if (playerInventory.firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "Nie masz wystarczająco dużo miejsca w ekwipunku");
            return false;
        }
        return true;
    }

    //Part to integrate with your money system
    private boolean validatePlayerAccount(Player player, String[] args){
        long checkValue = Long.parseLong(args[0]);
        long accountValue = balanceManager.getBalance(player.getName());

        if(accountValue < checkValue){
            player.sendMessage(ChatColor.RED + "Nie masz wystarczającej ilości pieniędzy");
            return false;
        }
        return true;
    }
    //

    private ItemStack createCheckItem(JavaPlugin plugin, Long value) {
        ItemStack checkItem = new ItemStack(Material.PAPER);
        ItemMeta meta = checkItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Czek na " + value + "$");
            meta.getPersistentDataContainer()
                    .set(new NamespacedKey(plugin, "check_value"), PersistentDataType.LONG, value);
            checkItem.setItemMeta(meta);
        }

        return checkItem;
    }

}
