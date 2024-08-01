package pawel.cookier.ignaczak.economypack.shop_manager.item_entity.controller;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.repository.IItemController;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;

import java.util.List;

public class ItemController implements IItemController {
    @Override
    public void updateItemSellPrice(JavaPlugin plugin, Player player, Item item, Double sellPrice) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            List<String> loreList = meta.getLore();
            if (loreList != null) {
                meta.getPersistentDataContainer().set(
                        new NamespacedKey(plugin, "sellPrice"), PersistentDataType.DOUBLE, sellPrice);

                loreList.set(1, ChatColor.RED + "Sell price: %s$".formatted(sellPrice));
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

        if (meta != null) {
            List<String> loreList = meta.getLore();
            if (loreList != null) {
                meta.getPersistentDataContainer().set(
                        new NamespacedKey(plugin, "buyPrice"), PersistentDataType.DOUBLE, buyPrice);

                loreList.set(0, ChatColor.GREEN + "Buy price: %s$".formatted(buyPrice));
                meta.setLore(loreList);
                itemStack.setItemMeta(meta);
                item.setBuyPrice(buyPrice);
                player.sendMessage(ChatColor.GREEN +
                        "Zmieniono cenę kupna na %s$".formatted(buyPrice));
            }

        }
    }

    @Override
    public Item createItem(JavaPlugin plugin,
                           ItemStack itemStack,
                           double sellPrice,
                           double buyPrice) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "sellPrice"), PersistentDataType.DOUBLE, sellPrice);
        itemMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "buyPrice"), PersistentDataType.DOUBLE, buyPrice);

        itemMeta.setLore(List.of(
                ChatColor.GREEN + "Buy price: %s$".formatted(buyPrice),
                ChatColor.RED + "Sell price: %s$".formatted(sellPrice)
        ));

        itemStack.setItemMeta(itemMeta);

        Item item = new Item(itemStack, sellPrice, buyPrice);
        makeItemStackToDisplayItemId(plugin, item);

        return item;
    }

    @Override
    public void makeItemStackToDisplayItemId(JavaPlugin plugin, Item item) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            List<String> loreList = meta.getLore();
            if (loreList != null) {
                loreList.add(ChatColor.GRAY + "Item id: %s".formatted(item.getId()));
                meta.setLore(loreList);
                meta.getPersistentDataContainer().set(
                        new NamespacedKey(plugin, "itemId"), PersistentDataType.INTEGER, item.getId());

                itemStack.setItemMeta(meta);
            }
        }
    }

    @Override
    public Integer getItemIdByItemStack(JavaPlugin plugin, ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "itemId");

        if (dataContainer.has(key, PersistentDataType.INTEGER)) {
            return dataContainer.get(key, PersistentDataType.INTEGER);
        } else {
            return null;
        }
    }
}
