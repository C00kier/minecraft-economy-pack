package pawel.cookier.ignaczak.economypack.money_manager.controllers;

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

    public void checkBalance(Player player, String[] args) {
        String playerToCheck = getPlayerNameToCheck(player, args);

        if (balanceManager.containsPlayer(playerToCheck)) {
            Long balance = balanceManager.getBalance(playerToCheck);
            player.sendMessage(ChatColor.GOLD
                    + translationManager.getMessage("message.moneymanager.userBalance")
                    .formatted(playerToCheck, balance));
        } else {
            player.sendMessage(ChatColor.LIGHT_PURPLE +
                    translationManager.getMessage("message.moneymanager.balanceUserNotFound")
                            .formatted(playerToCheck));
        }
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

        long amount = Long.parseLong(args[0]);
        String receiverName = args[1];
        processPayment(giver, amount, receiverName);
    }

    public void addPlayerManually(Player player, String[] args) {
        if (args.length == 1) {
            String newPlayerName = args[0];
            addUserToMoneyBaseManually(player, newPlayerName);
        } else {
            player.sendMessage(ChatColor.LIGHT_PURPLE +
                    translationManager.getMessage("message.moneymanager.invalidParameter"));
        }
    }

    private void addUserToMoneyBaseManually(Player player, String playerName) {
        if (player.isOp()) {
            if (!balanceManager.containsPlayer(playerName)) {
                balanceManager.setBalance(playerName, 0L);
                player.sendMessage(ChatColor.GREEN +
                        translationManager.getMessage("message.moneymanager.userAdded").formatted(playerName));
            } else {
                player.sendMessage(ChatColor.LIGHT_PURPLE +
                        translationManager.getMessage("message.moneymanager.userAlreadyExists")
                                .formatted(playerName));
            }
        } else {
            player.sendMessage(ChatColor.LIGHT_PURPLE +
                    translationManager.getMessage("message.common.lackOfPermission"));
        }
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
        String playerName = player.getName();
        int remainingToRemove = amountToExchange;

        int income = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (remainingToRemove <= 0) break;

            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.GOLD_INGOT) {
                int itemAmount = item.getAmount();
                Long playerBalance = balanceManager.getBalance(playerName);
                long pricePerIngot = PluginConfig.PRICE_PER_GOLD_INGOT;
                long moneyToReceive;

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
                playerBalance += moneyToReceive;
                balanceManager.setBalance(playerName, playerBalance);
                scoreboardHandler.updateMoney(player);
            }
        }
        player.sendMessage(ChatColor.GOLD
                + translationManager.getMessage("message.moneymanager.exchangeEarnings")
                .formatted(playerName, income));
    }

    private boolean validatePayToPlayerArgs(Player giver, String[] args) {
        if (args.length != 2) {
            giver.sendMessage(ChatColor.LIGHT_PURPLE
                    + translationManager.getMessage("message.moneymanager.invalidPayParameters"));
            return false;
        }
        try {
            long amount = Long.parseLong(args[0]);
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

    private void processPayment(Player giver, long amount, String receiverName) {
        String giverName = giver.getName();
        long giverBalance = balanceManager.getBalance(giverName);

        if (giverBalance >= amount) {
            if (balanceManager.containsPlayer(receiverName)) {
                completePayment(giver, giverName, amount, receiverName);
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

    private void completePayment(Player giver, String giverName, long amount, String receiverName) {
        long giverBalance = balanceManager.getBalance(giverName) - amount;
        long receiverBalance = balanceManager.getBalance(receiverName) + amount;

        balanceManager.setBalance(giverName, giverBalance);
        balanceManager.setBalance(receiverName, receiverBalance);
        scoreboardHandler.updateMoney(giver);

        giver.sendMessage(ChatColor.GOLD
                + translationManager.getMessage("message.moneymanager.completePayment")
                .formatted(giverName, receiverName, amount));
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

    private String getPlayerNameToCheck(Player player, String[] args) {
        if (args.length == 1) {
            return args[0];
        }
        return player.getName();
    }
}
