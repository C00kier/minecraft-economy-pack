package pawel.cookier.ignaczak.economypack.balance_manager.repository;

public interface IBalanceManager {
    void loadBalances();
    void saveBalances();
    void setBalance(String playerName, Long amount);
    Long getBalance(String playerName);
    boolean containsPlayer(String playerName);
}
