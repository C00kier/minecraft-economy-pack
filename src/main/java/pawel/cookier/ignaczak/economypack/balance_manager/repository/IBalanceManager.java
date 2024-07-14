package pawel.cookier.ignaczak.economypack.balance_manager.repository;

import java.util.UUID;

public interface IBalanceManager {
    void loadBalances();
    void saveBalances();
    void setBalance(UUID playerId, Double amount);
    Double getBalance(UUID playerId);
    boolean containsPlayer(UUID playerId);
    void addMoneyToPlayer(Double amount, UUID playerId);
    void removeMoneyFromPlayer(Double amount, UUID playerId);
    UUID getPlayerUUID(String playerName);
}
