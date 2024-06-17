package pawel.cookier.ignaczak.economypack;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
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

    @Override
    public void onEnable() {
        // Initialize managers
        Locale userLocale = Locale.forLanguageTag(PluginConfig.CURRENT_PLUGIN_LANGUAGE_TAG);
        TranslationManager translationManager = new TranslationManager(userLocale);

        this.balanceManager = new BalanceManager(this, userBalance);
        ScoreboardHandler scoreboardHandler = new ScoreboardHandler(balanceManager);

        // Initialize utilities
        RandomUtility randomUtility = new RandomUtility(random);

        // Initialize listeners
        OnPlayerJoinListener onPlayerJoinListener = new OnPlayerJoinListener(balanceManager, scoreboardHandler);
        getServer().getPluginManager().registerEvents(onPlayerJoinListener, this);

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

        // Initialize commands
        this.moneyManagerCommands = new MoneyManagerCommands(moneyManagerController);
        this.gamblingCommands = new GamblingCommands(gamblingController);
        this.pluginManagerCommands = new PluginManagerCommands(pluginManagerController);

        // Register commands
        registerCommands();
    }

    @Override
    public void onDisable() {
        balanceManager.saveBalances();
    }

    private void registerCommands() {
        // MONEY_MANAGER
        registerCommand("balance", moneyManagerCommands);
        registerCommand("exchange", moneyManagerCommands);
        registerCommand("pay", moneyManagerCommands);
        registerCommand("new_money_user", moneyManagerCommands);

        // GAMBLING
        registerCommand("gamble", gamblingCommands);
        registerCommand("slots", gamblingCommands);

        //TRANSLATION
        registerCommand("translation", pluginManagerCommands);
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(commandName)).setExecutor(executor);
        Objects.requireNonNull(getCommand(commandName)).setTabCompleter((TabCompleter) executor);
    }
}