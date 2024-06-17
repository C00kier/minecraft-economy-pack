package pawel.cookier.ignaczak.economypack.gambling.controllers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.gambling.repository.IGamblingController;
import pawel.cookier.ignaczak.economypack.scoreboard.controllers.ScoreboardHandler;
import pawel.cookier.ignaczak.economypack.translation_manager.controllers.TranslationManager;
import pawel.cookier.ignaczak.economypack.gambling.models.Game;
import pawel.cookier.ignaczak.economypack.gambling.models.GameType;
import pawel.cookier.ignaczak.economypack.utility.RandomUtility;

public class GamblingController implements IGamblingController {
    private final RandomUtility randomUtility;
    private final BalanceManager balanceManager;
    private final ScoreboardHandler scoreboardHandler;
    private final TranslationManager translationManager;

    public GamblingController(RandomUtility randomUtility,
                              BalanceManager balanceManager,
                              ScoreboardHandler scoreboardHandler,
                              TranslationManager translationManager) {
        this.randomUtility = randomUtility;
        this.balanceManager = balanceManager;
        this.scoreboardHandler = scoreboardHandler;
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
            long gameValue;

            try {
                gameValue = Long.parseLong(args[0]);
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
                          Long gameValue,
                          Long gameMultiplier,
                          GameType gameType,
                          String moneyLockMessage) {

        String playerName = player.getName();
        Long currentBalance = balanceManager.getBalance(playerName);

        if (currentBalance >= gameValue) {
            boolean isWinning = isWinningGame(gameType, player);
            if (isWinning) {
                Long winValue = gameValue * gameMultiplier;
                currentBalance += (winValue);
                player.sendMessage(ChatColor.GREEN +
                        translationManager.getMessage("message.gambling.won").formatted(
                                playerName,
                                winValue));
            } else {
                currentBalance -= gameValue;
                player.sendMessage(ChatColor.DARK_RED +
                        translationManager.getMessage("message.gambling.lost").formatted(
                                playerName,
                                gameValue));
            }
            balanceManager.setBalance(playerName, currentBalance);
            scoreboardHandler.updateMoney(player);
            player.sendMessage(ChatColor.GOLD +
                    translationManager.getMessage("message.gambling.currentBalance").formatted(
                            playerName,
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
        return randomUtility.isTrueBasedOnChance(PluginConfig.GAMBLE_CHANCE);
    }

    private boolean isWinningSlotsGame(Player player) {
        int minNumber = PluginConfig.SLOTS_MIN_NUMBER_VALUE;
        int maxNumber = PluginConfig.SLOTS_MAX_NUMBER_VALUE + 1;
        int number1 = randomUtility.generateRandomNumber(minNumber, maxNumber);
        int number2 = randomUtility.generateRandomNumber(minNumber, maxNumber);
        int number3 = randomUtility.generateRandomNumber(minNumber, maxNumber);

        player.sendMessage(ChatColor.GOLD +
                translationManager.getMessage("message.gambling.slotsNumbers").formatted(
                        player.getName(),
                        number1,
                        number2,
                        number3));

        return (number1 == number2) && (number2 == number3);
    }
}
