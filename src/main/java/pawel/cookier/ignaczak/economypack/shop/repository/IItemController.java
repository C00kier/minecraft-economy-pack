package pawel.cookier.ignaczak.economypack.shop.repository;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop.models.Item;

public interface IItemController {
    void updateItemSellPrice(JavaPlugin plugin, Player player, Item item, Double sellPrice);

    void updateItemBuyPrice(JavaPlugin plugin, Player player, Item item, Double buyPrice);
}
