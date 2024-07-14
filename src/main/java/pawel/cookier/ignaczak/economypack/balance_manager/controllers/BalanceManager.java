package pawel.cookier.ignaczak.economypack.balance_manager.controllers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.balance_manager.repository.IBalanceManager;
import pawel.cookier.ignaczak.economypack.scoreboard.controllers.ScoreboardHandler;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class BalanceManager implements IBalanceManager {
    private final File configFile;
    private final FileConfiguration config;
    private final Map<UUID, Double> userBalance;
    private final JavaPlugin plugin;
    private final ScoreboardHandler scoreboardHandler;

    public BalanceManager(JavaPlugin plugin, Map<UUID, Double> userBalance, ScoreboardHandler scoreboardHandler) {
        this.userBalance = userBalance;
        this.plugin = plugin;

        configFile = new File(plugin.getDataFolder(), "balances.yml");
        this.scoreboardHandler = scoreboardHandler;
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
            UUID playerId = UUID.fromString(key);
            double balance = config.getDouble(key);
            userBalance.put(playerId, balance);
        }
    }

    @Override
    public void saveBalances() {
        for (Map.Entry<UUID, Double> entry : userBalance.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error saving balances to balances.yml file", e);
        }
    }

    @Override
    public void setBalance(UUID playerId, Double amount) {
        userBalance.put(playerId, amount);
        scoreboardHandler.updateMoney(Bukkit.getPlayer(playerId));
        saveBalances();
    }

    @Override
    public Double getBalance(UUID playerId) {
        return userBalance.getOrDefault(playerId, 0d);
    }

    @Override
    public boolean containsPlayer(UUID playerId) {
        return userBalance.containsKey(playerId);
    }

    @Override
    public void addMoneyToPlayer(Double amount, UUID playerId) {
        double currentBalance = getBalance(playerId);
        setBalance(playerId, currentBalance + amount);
    }

    @Override
    public void removeMoneyFromPlayer(Double amount, UUID playerId) {
        double currentBalance = getBalance(playerId);
        if(amount <= currentBalance){
            setBalance(playerId, currentBalance - amount);
        }
    }

    @Override
    public UUID getPlayerUUID(String playerName) {
        return Objects.requireNonNull(Bukkit.getPlayerExact(playerName)).getUniqueId();
    }
}
