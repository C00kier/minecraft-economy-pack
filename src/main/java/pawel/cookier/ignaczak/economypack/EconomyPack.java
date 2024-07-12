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
import pawel.cookier.ignaczak.economypack.balance_manager.events.BalanceManagerEvents;
import pawel.cookier.ignaczak.economypack.balance_manager.controllers.BalanceManager;
import pawel.cookier.ignaczak.economypack.scoreboard.controllers.ScoreboardHandler;
import pawel.cookier.ignaczak.economypack.shop.commands.ShopCommands;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopController;
import pawel.cookier.ignaczak.economypack.shop.events.ShopEvents;
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
    private ShopCommands shopCommands;

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
        ShopController shopController = new ShopController();

        // Initialize events
        BalanceManagerEvents balanceManagerEvents = new BalanceManagerEvents(balanceManager, scoreboardHandler);
        ShopEvents shopEvents = new ShopEvents(shopController);
        getServer().getPluginManager().registerEvents(balanceManagerEvents, this);
        getServer().getPluginManager().registerEvents(shopEvents, this);

        // Initialize commands
        this.moneyManagerCommands = new MoneyManagerCommands(moneyManagerController);
        this.gamblingCommands = new GamblingCommands(gamblingController);
        this.pluginManagerCommands = new PluginManagerCommands(pluginManagerController);
        this.shopCommands = new ShopCommands(shopController);

        // Register commands
        registerCommands();
    }

    @Override
    public void onDisable() {
        balanceManager.saveBalances();
    }

    private void registerCommands() {
        // MONEY_MANAGER
        registerCommandWithTabCompleter("balance", moneyManagerCommands);
        registerCommandWithTabCompleter("exchange", moneyManagerCommands);
        registerCommandWithTabCompleter("pay", moneyManagerCommands);
        registerCommandWithTabCompleter("new_money_user", moneyManagerCommands);

        // GAMBLING
        registerCommandWithTabCompleter("gamble", gamblingCommands);
        registerCommandWithTabCompleter("slots", gamblingCommands);

        //TRANSLATION
        registerCommandWithTabCompleter("translation", pluginManagerCommands);

        //SHOP MANAGER
        registerCommandWithTabCompleter(PluginConfig.ADD_SHOP_CATEGORY_COMMAND, shopCommands);
        registerCommandWithTabCompleter(PluginConfig.CALL_SHOP_COMMAND, shopCommands);

    }

    private void registerCommandWithTabCompleter(String commandName, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(commandName)).setExecutor(executor);
        Objects.requireNonNull(getCommand(commandName)).setTabCompleter((TabCompleter) executor);
    }
}