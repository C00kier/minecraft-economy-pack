package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop.models.Item;
import pawel.cookier.ignaczak.economypack.shop.repository.IItemController;

import java.util.List;

public class ItemController implements IItemController {
    @Override
    public void updateItemSellPrice(JavaPlugin plugin, Player player, Item item, Double sellPrice) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if(meta != null){
            List<String> loreList = meta.getLore();
            if(loreList != null){
                meta.getPersistentDataContainer().set(
                        new NamespacedKey(plugin, "sellPrice"), PersistentDataType.DOUBLE, sellPrice);

                loreList.set(1,ChatColor.RED + "Sell price: %s$".formatted(sellPrice));
                meta.setLore(loreList);
                itemStack.setItemMeta(meta);
                item.setSellPrice(sellPrice);
                player.sendMessage(ChatColor.GREEN +
                        "Zmieniono cenę sprzedaży na %s$".formatted(sellPrice));
            }
        }
    }

    @Override
    public void updateItemBuyPrice(JavaPlugin plugin, Player player, Item item, Double buyPrice) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if(meta != null){
            List<String> loreList = meta.getLore();
            if(loreList != null){
                meta.getPersistentDataContainer().set(
                        new NamespacedKey(plugin, "buyPrice"), PersistentDataType.DOUBLE, buyPrice);

                loreList.set(0,ChatColor.GREEN + "Buy price: %s$".formatted(buyPrice));
                meta.setLore(loreList);
                itemStack.setItemMeta(meta);
                item.setBuyPrice(buyPrice);
                player.sendMessage(ChatColor.GREEN +
                        "Zmieniono cenę kupna na %s$".formatted(buyPrice));
            }

        }
    }

}
