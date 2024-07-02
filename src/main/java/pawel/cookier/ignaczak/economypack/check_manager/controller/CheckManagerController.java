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

import java.util.Objects;

public class CheckManagerController implements ICheckManagerController {

    private final BalanceManager balanceManager;
    private final ScoreboardHandler scoreboardHandler;

    public CheckManagerController(BalanceManager balanceManager, ScoreboardHandler scoreboardHandler) {
        this.balanceManager = balanceManager;
        this.scoreboardHandler = scoreboardHandler;
    }

    @Override
    public void addCheckToPlayerInventory(Player player, JavaPlugin plugin, String[] args) {
        boolean isValid = createCheckValidation(player, args);

        if (isValid) {
            long checkValue = Long.parseLong(args[0]);
            ItemStack checkItem = createCheckItem(plugin, checkValue);

            if (validateInventorySpace(player, checkItem, plugin)) {
                player.getInventory().addItem(checkItem);
                player.sendMessage(ChatColor.GREEN + "Dodano czek o wartości %s$ do ekwipunku".formatted(checkValue));

                // Remove money from account
                String playerName = player.getName();
                Long playerBalance = balanceManager.getBalance(playerName);
                balanceManager.setBalance(playerName, playerBalance - checkValue);
                scoreboardHandler.updateMoney(player);
            }
        }
    }

    public void exchangeCheckForMoney(Player player, ItemStack item, JavaPlugin plugin) {
        long checkValue = getCheckValue(item, plugin);
        // Your logic to add money to account
        String playerName = player.getName();
        long currentBalance = balanceManager.getBalance(playerName);
        balanceManager.setBalance(playerName, currentBalance + checkValue);
        scoreboardHandler.updateMoney(player);
        player.sendMessage(ChatColor.GREEN + "Wymieniono czek na %s$".formatted(checkValue));
        //
    }

    private long getCheckValue(ItemStack item, JavaPlugin plugin) {
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

    private boolean validateInventorySpace(Player player, ItemStack itemToAdd, JavaPlugin plugin) {
        Inventory playerInventory = player.getInventory();
        int totalAmount = itemToAdd.getAmount();
        ItemMeta itemMeta = itemToAdd.getItemMeta();
        int maxStackSize = itemToAdd.getMaxStackSize();

        for (int i = 0; i < PluginConfig.BASIC_BACKPACKSIZE; i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item == null) {
                return true;
            }

            ItemMeta currentMeta = item.getItemMeta();
            if (compareItemMeta(currentMeta, itemMeta, plugin) && item.getAmount() < maxStackSize) {
                int availableSpace = maxStackSize - item.getAmount();
                totalAmount -= availableSpace;

                if (totalAmount <= 0) {
                    return true;
                }
            }
        }

        player.sendMessage(ChatColor.RED + "Nie masz wystarczająco dużo miejsca w ekwipunku");
        return false;
    }

    private boolean compareItemMeta(ItemMeta meta1, ItemMeta meta2, JavaPlugin plugin) {
        if (meta1 == null || meta2 == null) {
            return false;
        }

        NamespacedKey key = new NamespacedKey(plugin, "check_value");
        Long value1 = meta1.getPersistentDataContainer().get(key, PersistentDataType.LONG);
        Long value2 = meta2.getPersistentDataContainer().get(key, PersistentDataType.LONG);

        return Objects.equals(value1, value2);
    }

    private boolean validatePlayerAccount(Player player, String[] args) {
        long checkValue = Long.parseLong(args[0]);
        long accountValue = balanceManager.getBalance(player.getName());

        if (accountValue < checkValue) {
            player.sendMessage(ChatColor.RED + "Nie masz wystarczającej ilości pieniędzy");
            return false;
        }
        return true;
    }

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
