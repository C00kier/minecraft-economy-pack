package pawel.cookier.ignaczak.economypack.money_manager.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.scoreboard.controllers.ScoreboardHandler;
import pawel.cookier.ignaczak.economypack.translation_manager.controllers.TranslationManager;
import pawel.cookier.ignaczak.economypack.money_manager.repository.IMoneyManagerController;

import java.util.UUID;

public class MoneyManagerController implements IMoneyManagerController {
    private final BalanceManager balanceManager;
    private final ScoreboardHandler scoreboardHandler;
    private final TranslationManager translationManager;

    public MoneyManagerController(BalanceManager balanceManager,
                                  ScoreboardHandler scoreboardHandler,
                                  TranslationManager translationManager) {
        this.balanceManager = balanceManager;
        this.scoreboardHandler = scoreboardHandler;
        this.translationManager = translationManager;
    }

    public void exchangeGold(Player player, String[] args) {

        if (!validateExchangeGoldArgs(player, args)) return;

        int amountToExchange = Integer.parseInt(args[0]);
        Inventory inventory = player.getInventory();
        if (hasEnoughGoldInInventory(player, inventory, amountToExchange)) {
            processGoldExchange(player, amountToExchange, inventory);
        }
    }

    public void payToPlayer(Player giver, String[] args) {
        if (!validatePayToPlayerArgs(giver, args)) return;

        double amount = Double.parseDouble(args[0]);
        String receiverName = args[1];
        processPayment(giver, amount, receiverName);
    }

    private boolean validateExchangeGoldArgs(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.LIGHT_PURPLE
                    + translationManager.getMessage("message.moneymanager.lackOfExchangeAmount"));
            return false;
        }
        try {
            int amountToExchange = Integer.parseInt(args[0]);
            if (amountToExchange <= 0) {
                player.sendMessage(ChatColor.LIGHT_PURPLE
                        + translationManager.getMessage("message.moneymanager.minExchangeValue"));
                return false;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.LIGHT_PURPLE
                    + translationManager.getMessage("message.moneymanager.incorrectExchangeValue"));
            return false;
        }
        return true;
    }

    private void processGoldExchange(Player player, int amountToExchange, Inventory inventory) {
        UUID playerId = player.getUniqueId();
        int remainingToRemove = amountToExchange;

        int income = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (remainingToRemove <= 0) break;

            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.GOLD_INGOT) {
                int itemAmount = item.getAmount();
                double pricePerIngot = PluginConfig.PRICE_PER_GOLD_INGOT;
                double moneyToReceive;

                if (itemAmount > remainingToRemove) {
                    item.setAmount(itemAmount - remainingToRemove);
                    moneyToReceive = remainingToRemove * pricePerIngot;
                    remainingToRemove = 0;
                } else {
                    inventory.clear(i);
                    moneyToReceive = itemAmount * pricePerIngot;
                    remainingToRemove -= itemAmount;
                }

                income += moneyToReceive;
                balanceManager.addMoneyToPlayer(moneyToReceive, playerId);
                scoreboardHandler.updateMoney(player);
            }
        }
        player.sendMessage(ChatColor.GOLD
                + translationManager.getMessage("message.moneymanager.exchangeEarnings")
                .formatted(player.getName(), income));
    }

    private boolean validatePayToPlayerArgs(Player giver, String[] args) {
        if (args.length != 2) {
            giver.sendMessage(ChatColor.LIGHT_PURPLE
                    + translationManager.getMessage("message.moneymanager.invalidPayParameters"));
            return false;
        }
        try {
            double amount = Double.parseDouble(args[0]);
            if (amount <= 0) {
                giver.sendMessage(ChatColor.LIGHT_PURPLE
                        + translationManager.getMessage("message.moneymanager.minPayValue"));
                return false;
            }
        } catch (NumberFormatException e) {
            giver.sendMessage(ChatColor.LIGHT_PURPLE
                    + translationManager.getMessage("message.moneymanager.incorrectPayAmount"));
            return false;
        }
        return true;
    }

    private void processPayment(Player giver, double amount, String receiverName) {
        UUID giverId = giver.getUniqueId();
        double giverBalance = balanceManager.getBalance(giverId);

        if (giverBalance >= amount) {
            UUID receiverID = balanceManager.getPlayerUUID(receiverName);
            if (balanceManager.containsPlayer(receiverID)) {
                completePayment(giver, amount, receiverID);
            } else {
                giver.sendMessage(ChatColor.LIGHT_PURPLE
                        + translationManager.getMessage("message.moneymanager.payUserNotFound")
                        .formatted(receiverName));
            }
        } else {
            giver.sendMessage(ChatColor.LIGHT_PURPLE
                    + translationManager.getMessage("message.moneymanager.payNotEnoughMoney"));
        }
    }

    private void completePayment(Player giver, double amount, UUID receiverId) {
        UUID giverId = giver.getUniqueId();

        balanceManager.addMoneyToPlayer(amount, receiverId);
        balanceManager.removeMoneyFromPlayer(amount, giverId);

        Player receiver = Bukkit.getPlayer(receiverId);
        scoreboardHandler.updateMoney(receiver);
        scoreboardHandler.updateMoney(giver);

        //wiadomość wysłana do otrzymującego
        assert receiver != null;
        giver.sendMessage(ChatColor.GOLD
                + translationManager.getMessage("message.moneymanager.completePayment")
                .formatted(giver.getName(), receiver.getName(), amount));
    }

    private boolean hasEnoughGoldInInventory(Player player, Inventory inventory, int amountToRemove) {
        int totalGoldInInventory = calculateGoldAmountInInventory(inventory);
        if (amountToRemove > totalGoldInInventory) {
            player.sendMessage(ChatColor.LIGHT_PURPLE +
                    translationManager.getMessage("message.moneymanager.notEnoughGoldForExchange")
                            .formatted(amountToRemove));
            return false;
        }
        return true;
    }

    private int calculateGoldAmountInInventory(Inventory inventory) {
        int totalGoldIngots = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.GOLD_INGOT) {
                totalGoldIngots += item.getAmount();
            }
        }

        return totalGoldIngots;
    }
}
