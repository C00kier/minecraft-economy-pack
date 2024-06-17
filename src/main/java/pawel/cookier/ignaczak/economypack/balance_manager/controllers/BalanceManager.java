package pawel.cookier.ignaczak.economypack.balance_manager.controllers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.balance_manager.repository.IBalanceManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

public class BalanceManager implements IBalanceManager {
    private final File configFile;
    private final FileConfiguration config;
    private final Map<String,Long> userBalance;
    private final JavaPlugin plugin;

    public BalanceManager(JavaPlugin plugin, Map<String, Long> userBalance) {
        this.userBalance = userBalance;
        this.plugin = plugin;

        configFile = new File(plugin.getDataFolder(), "balances.yml");
        if (!configFile.exists()) {
            if (configFile.getParentFile().mkdirs()) {
                plugin.getLogger().log(Level.INFO, "Created directories for balances.yml");
            } else {
                plugin.getLogger().log(Level.WARNING, "Failed to create directories for balances.yml");
            }
            try {
                if (configFile.createNewFile()) {
                    plugin.getLogger().log(Level.INFO, "Created balances.yml file");
                } else {
                    plugin.getLogger().log(Level.WARNING, "Failed to create balances.yml file");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error creating balances.yml file", e);
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadBalances();
    }

    @Override
    public void loadBalances() {
        for (String key : config.getKeys(false)) {
            long balance = config.getLong(key);
            userBalance.put(key, balance);
        }
    }

    @Override
    public void saveBalances() {
        for (Map.Entry<String, Long> entry : userBalance.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error saving balances to balances.yml file", e);
        }
    }

    @Override
    public void setBalance(String playerName, Long amount) {
        userBalance.put(playerName, amount);
        saveBalances();
    }

    @Override
    public Long getBalance(String playerName) {
        return userBalance.getOrDefault(playerName, 0L);
    }

    @Override
    public boolean containsPlayer(String playerName) {
        return userBalance.containsKey(playerName);
    }
}
