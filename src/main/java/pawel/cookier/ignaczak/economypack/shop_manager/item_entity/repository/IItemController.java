package pawel.cookier.ignaczak.economypack.shop_manager.item_entity.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;

public interface IItemController {
    void updateItemSellPrice(JavaPlugin plugin, Player player, Item item, Double sellPrice);

    void updateItemBuyPrice(JavaPlugin plugin, Player player, Item item, Double buyPrice);

    Item createItem(JavaPlugin plugin,
                    ItemStack itemStack,
                    double sellPrice,
                    double buyPrice);

    void makeItemStackToDisplayItemId(JavaPlugin plugin, Item item);

    Integer getItemIdByItemStack(JavaPlugin plugin, ItemStack itemStack);
}
