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
import pawel.cookier.ignaczak.economypack.shop.commands.ShopCommands;
import pawel.cookier.ignaczak.economypack.shop.controllers.*;
import pawel.cookier.ignaczak.economypack.shop.events.ShopEvents;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.validation.ShopCommandsValidation;
import pawel.cookier.ignaczak.economypack.translation_manager.controllers.TranslationManager;
import pawel.cookier.ignaczak.economypack.gambling.utility.GamblingUtility;

import java.util.*;

public final class EconomyPack extends JavaPlugin {
    private final Map<UUID, Double> userBalance = new HashMap<>();
    private final Random random = new Random();
    private final Shop shop = new Shop();

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

        // Initialize utilities
        GamblingUtility gamblingUtility = new GamblingUtility(random);

        //Initialize controllers
        GamblingController gamblingController = new GamblingController(
                gamblingUtility,
                balanceManager,
                translationManager
        );
        MoneyManagerController moneyManagerController = new MoneyManagerController(
                balanceManager,
                translationManager
        );
        PluginManagerController pluginManagerController = new PluginManagerController(
                translationManager
        );

        //shop
        ShopController shopController = new ShopController();
        CategoryController categoryController = new CategoryController();
        ShopCommandsValidation shopCommandsValidation = new ShopCommandsValidation(
                shop,
                shopController,
                categoryController);
        ShopCommandsController shopCommandsController = new ShopCommandsController(
                shop,
                shopCommandsValidation,
                categoryController,
                shopController);
        ShopTabController shopTabController = new ShopTabController(shop);
        ShopEventsController shopEventsController = new ShopEventsController(
                shopCommandsController,
                shopController,
                balanceManager);

        // Initialize events
        BalanceManagerEvents balanceManagerEvents = new BalanceManagerEvents(balanceManager);
        ShopEvents shopEvents = new ShopEvents(shop, shopEventsController, this);
        getServer().getPluginManager().registerEvents(balanceManagerEvents, this);
        getServer().getPluginManager().registerEvents(shopEvents, this);

        // Initialize commands
        this.moneyManagerCommands = new MoneyManagerCommands(moneyManagerController);
        this.gamblingCommands = new GamblingCommands(gamblingController);
        this.pluginManagerCommands = new PluginManagerCommands(pluginManagerController);
        this.shopCommands = new ShopCommands(this, shop, shopCommandsController, shopTabController);

        // Register commands
        registerCommands();
    }

    @Override
    public void onDisable() {
        balanceManager.saveBalances();
    }

    private void registerCommands() {
        // MONEY_MANAGER
        registerCommandWithTabCompleter("exchange", moneyManagerCommands);
        registerCommandWithTabCompleter("pay", moneyManagerCommands);

        // GAMBLING
        registerCommandWithTabCompleter("gamble", gamblingCommands);
        registerCommandWithTabCompleter("slots", gamblingCommands);

        //TRANSLATION
        registerCommandWithTabCompleter("translation", pluginManagerCommands);

        //SHOP MANAGER
        registerCommandWithTabCompleter(PluginConfig.ADD_SHOP_CATEGORY_COMMAND, shopCommands);
        registerCommandWithTabCompleter(PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND, shopCommands);
        registerCommandWithTabCompleter(PluginConfig.CALL_SHOP_COMMAND, shopCommands);
        registerCommandWithTabCompleter(PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND, shopCommands);
        registerCommandWithTabCompleter(PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND, shopCommands);
        registerCommandWithTabCompleter(PluginConfig.ADD_SHOP_ITEM_COMMAND, shopCommands);
    }

    private void registerCommandWithTabCompleter(String commandName, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(commandName)).setExecutor(executor);
        Objects.requireNonNull(getCommand(commandName)).setTabCompleter((TabCompleter) executor);
    }
}