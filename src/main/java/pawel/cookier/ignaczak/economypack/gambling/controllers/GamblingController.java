package pawel.cookier.ignaczak.economypack.gambling.controllers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.gambling.repository.IGamblingController;
import pawel.cookier.ignaczak.economypack.translation_manager.controllers.TranslationManager;
import pawel.cookier.ignaczak.economypack.gambling.models.Game;
import pawel.cookier.ignaczak.economypack.gambling.models.GameType;
import pawel.cookier.ignaczak.economypack.gambling.utility.GamblingUtility;

import java.util.UUID;

public class GamblingController implements IGamblingController {
    private final GamblingUtility gamblingUtility;
    private final BalanceManager balanceManager;
    private final TranslationManager translationManager;

    public GamblingController(GamblingUtility gamblingUtility,
                              BalanceManager balanceManager,
                              TranslationManager translationManager) {
        this.gamblingUtility = gamblingUtility;
        this.balanceManager = balanceManager;
        this.translationManager = translationManager;
    }

    public void runGamble(Player player, String[] args) {
        Game gambleGame = new Game(
                PluginConfig.GAMBLE_GAME_TYPE,
                PluginConfig.GAMBLE_MULTIPLIER,
                PluginConfig.MIN_GAMBLE_VALUE
        );

        runGame(player, gambleGame, args);
    }

    public void runSlots(Player player, String[] args) {
        Game slotsGame = new Game(
                PluginConfig.SLOTS_GAME_TYPE,
                PluginConfig.SLOTS_MULTIPLIER,
                PluginConfig.MIN_SLOTS_VALUE
        );

        runGame(player, slotsGame, args);
    }

    private void runGame(Player player,
                         Game game,
                         String[] args) {

        String minValueMessage = ChatColor.LIGHT_PURPLE +
                translationManager.getMessage("message.gambling.minBetValue").formatted(
                        player.getName(),
                        game.minGameValue());

        if (args.length != 0) {
            double gameValue;

            try {
                gameValue = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.LIGHT_PURPLE +
                        translationManager.getMessage("message.gambling.incorrectBetValue"));
                return;
            }

            if (gameValue < game.minGameValue()) {
                player.sendMessage(minValueMessage);
                return;
            }

            String notEnoughMoneyMessage =
                    ChatColor.LIGHT_PURPLE +
                            translationManager.getMessage("message.gambling.notEnoughMoney").formatted(
                                    player.getName());

            playGame(player, gameValue, game.gameMultiplier(), game.gameType(), notEnoughMoneyMessage);
        } else {
            playGame(player, game.minGameValue(), game.gameMultiplier(), game.gameType(), minValueMessage);
        }
    }

    private void playGame(Player player,
                          Double gameValue,
                          Double gameMultiplier,
                          GameType gameType,
                          String moneyLockMessage) {

        UUID playerId = player.getUniqueId();
        Double currentBalance = balanceManager.getBalance(playerId);

        if (currentBalance >= gameValue) {
            boolean isWinning = isWinningGame(gameType, player);
            if (isWinning) {
                Double winValue = gameValue * gameMultiplier;
                currentBalance += winValue;
                balanceManager.addMoneyToPlayer(winValue, playerId);
                player.sendMessage(ChatColor.GREEN +
                        translationManager.getMessage("message.gambling.won").formatted(
                                player.getName(),
                                winValue));
            } else {
                currentBalance -= gameValue;
                balanceManager.removeMoneyFromPlayer(gameValue, playerId);
                player.sendMessage(ChatColor.DARK_RED +
                        translationManager.getMessage("message.gambling.lost").formatted(
                                player.getName(),
                                gameValue));
            }

            player.sendMessage(ChatColor.GOLD +
                    translationManager.getMessage("message.gambling.currentBalance").formatted(
                            player.getName(),
                            currentBalance));
        } else {
            player.sendMessage(moneyLockMessage);
        }
    }

    private boolean isWinningGame(GameType gameType, Player player) {
        switch (gameType) {
            case GAMBLE_GAME -> {
                return isWinningGambleGame();
            }
            case SLOTS_GAME -> {
                return isWinningSlotsGame(player);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isWinningGambleGame() {
        return gamblingUtility.isTrueBasedOnChance(PluginConfig.GAMBLE_CHANCE);
    }

    private boolean isWinningSlotsGame(Player player) {
        int minNumber = PluginConfig.SLOTS_MIN_NUMBER_VALUE;
        int maxNumber = PluginConfig.SLOTS_MAX_NUMBER_VALUE + 1;
        int number1 = gamblingUtility.generateRandomNumber(minNumber, maxNumber);
        int number2 = gamblingUtility.generateRandomNumber(minNumber, maxNumber);
        int number3 = gamblingUtility.generateRandomNumber(minNumber, maxNumber);

        player.sendMessage(ChatColor.GOLD +
                translationManager.getMessage("message.gambling.slotsNumbers").formatted(
                        player.getName(),
                        number1,
                        number2,
                        number3));

        return (number1 == number2) && (number2 == number3);
    }
}
