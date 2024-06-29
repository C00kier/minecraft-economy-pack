package pawel.cookier.ignaczak.economypack;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.check_manager.commands.CheckManagerCommands;
import pawel.cookier.ignaczak.economypack.check_manager.controller.CheckManagerController;
import pawel.cookier.ignaczak.economypack.check_manager.events.CheckEvents;
import pawel.cookier.ignaczak.economypack.gambling.commands.GamblingCommands;
import pawel.cookier.ignaczak.economypack.money_manager.commands.MoneyManagerCommands;
import pawel.cookier.ignaczak.economypack.plugin_manager.commands.PluginManagerCommands;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.gambling.controllers.GamblingController;
import pawel.cookier.ignaczak.economypack.money_manager.controllers.MoneyManagerController;
import pawel.cookier.ignaczak.economypack.plugin_manager.controllers.PluginManagerController;
import pawel.cookier.ignaczak.economypack.listeners.OnPlayerJoinListener;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.scoreboard.controllers.ScoreboardHandler;
import pawel.cookier.ignaczak.economypack.translation_manager.controllers.TranslationManager;
import pawel.cookier.ignaczak.economypack.utility.RandomUtility;

import java.util.*;

public final class EconomyPack extends JavaPlugin {
    private final Map<String, Long> userBalance = new HashMap<>();
    private final Random random = new Random();

    // managers
    private BalanceManager balanceManager;

    // commands
    private MoneyManagerCommands moneyManagerCommands;
    private GamblingCommands gamblingCommands;
    private PluginManagerCommands pluginManagerCommands;
    private CheckManagerCommands checkManagerCommands;

    @Override
    public void onEnable() {
        // Initialize managers
        Locale userLocale = Locale.forLanguageTag(PluginConfig.CURRENT_PLUGIN_LANGUAGE_TAG);
        TranslationManager translationManager = new TranslationManager(userLocale);

        this.balanceManager = new BalanceManager(this, userBalance);
        ScoreboardHandler scoreboardHandler = new ScoreboardHandler(balanceManager);

        // Initialize utilities
        RandomUtility randomUtility = new RandomUtility(random);

        //Initialize controllers
        GamblingController gamblingController = new GamblingController(
                randomUtility,
                balanceManager,
                scoreboardHandler,
                translationManager
        );
        MoneyManagerController moneyManagerController = new MoneyManagerController(
                balanceManager,
                scoreboardHandler,
                translationManager
        );
        PluginManagerController pluginManagerController = new PluginManagerController(
                translationManager
        );
        CheckManagerController checkManagerController = new CheckManagerController(balanceManager, scoreboardHandler);

        // Initialize listeners
        OnPlayerJoinListener onPlayerJoinListener = new OnPlayerJoinListener(balanceManager, scoreboardHandler);
        CheckEvents checkEvents = new CheckEvents(checkManagerController, this);

        getServer().getPluginManager().registerEvents(onPlayerJoinListener, this);
        getServer().getPluginManager().registerEvents(checkEvents, this);

        // Initialize commands
        this.moneyManagerCommands = new MoneyManagerCommands(moneyManagerController);
        this.gamblingCommands = new GamblingCommands(gamblingController);
        this.pluginManagerCommands = new PluginManagerCommands(pluginManagerController);
        this.checkManagerCommands = new CheckManagerCommands(checkManagerController, this);

        // Register commands
        registerCommands();
    }

    @Override
    public void onDisable() {
        balanceManager.saveBalances();
    }

    private void registerCommands() {
        // MONEY_MANAGER
        registerCommand(PluginConfig.BALANCE_COMMAND, moneyManagerCommands);
        registerCommand(PluginConfig.EXCHANGE_COMMAND, moneyManagerCommands);
        registerCommand(PluginConfig.PAY_COMMAND, moneyManagerCommands);
        registerCommand(PluginConfig.ADD_USER_MANUALLY_COMMAND, moneyManagerCommands);

        // GAMBLING
        registerCommand(PluginConfig.GAMBLE_COMMAND, gamblingCommands);
        registerCommand(PluginConfig.SLOTS_COMMAND, gamblingCommands);

        //TRANSLATION
        registerCommand(PluginConfig.TRANSLATION_COMMAND, pluginManagerCommands);

        //CHECKS
        registerCommand(PluginConfig.CHECK_COMMAND, checkManagerCommands);
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(commandName)).setExecutor(executor);
        Objects.requireNonNull(getCommand(commandName)).setTabCompleter((TabCompleter) executor);
    }
}