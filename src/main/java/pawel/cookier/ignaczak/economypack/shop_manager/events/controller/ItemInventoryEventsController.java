package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.IItemInventoryEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.events.validation.ShopEventsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.controller.ItemController;

import java.util.List;

public class ItemInventoryEventsController implements IItemInventoryEventsController {

    private final JavaPlugin plugin;
    private final ShopEventsValidation validation;
    private final ItemController itemController;

    public ItemInventoryEventsController(JavaPlugin plugin, ShopEventsValidation validation, ItemController itemController) {
        this.plugin = plugin;
        this.validation = validation;
        this.itemController = itemController;
    }

    @Override
    public void minusItemQuantity64ButtonEvent(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                "Zmniejsz o 64",
                Material.RED_STAINED_GLASS_PANE,
                64)) {
            updateItemStackQuantity(event, -64);
        }
    }

    @Override
    public void minusItemQuantity16ButtonEvent(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                "Zmniejsz o 16",
                Material.RED_STAINED_GLASS_PANE,
                16)) {
            updateItemStackQuantity(event, -16);
        }
    }

    @Override
    public void minusItemQuantity1ButtonEvent(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                "Zmniejsz o 1",
                Material.RED_STAINED_GLASS_PANE,
                1)) {
            updateItemStackQuantity(event, -1);
        }
    }

    @Override
    public void plusItemQuantity64ButtonEvent(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                "Zwiększ o 64",
                Material.LIME_STAINED_GLASS_PANE,
                64)) {
            updateItemStackQuantity(event, 64);
        }
    }

    @Override
    public void plusItemQuantity16ButtonEvent(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                "Zwiększ o 16",
                Material.LIME_STAINED_GLASS_PANE,
                16)) {
            updateItemStackQuantity(event, 16);
        }
    }

    @Override
    public void plusItemQuantity1ButtonEvent(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                "Zwiększ o 1",
                Material.LIME_STAINED_GLASS_PANE,
                1)) {
            updateItemStackQuantity(event, 1);
        }
    }

    @Override
    public void buyButtonEvent(InventoryClickEvent event) {
        if (!validation.isBuySellItemMenu(event)) return;

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                ChatColor.GREEN + "Kup",
                Material.PAPER,
                1)) {

            ItemStack itemStack = event.getInventory().getItem(PluginConfig.SHOP_OPERATIONS_ITEM_PLACE);
            if (itemStack == null) return;

            Player player = (Player) event.getWhoClicked();

            if (event.getClick() == ClickType.SHIFT_RIGHT) {
                itemController.buyItemsForMoney(plugin, player, itemStack, true);
            } else if (event.getClick() == ClickType.LEFT) {
                itemController.buyItemsForMoney(plugin, player, itemStack, false);
            }
        }
    }

    @Override
    public void sellButtonEvent(InventoryClickEvent event) {
        if (!validation.isBuySellItemMenu(event)) return;

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        if (validation.isClickedItemQuantityButton(
                clickedItem,
                ChatColor.RED + "Sprzedaj",
                Material.PAPER,
                1)) {

            ItemStack itemStack = event.getInventory().getItem(PluginConfig.SHOP_OPERATIONS_ITEM_PLACE);
            if (itemStack == null) return;

            Player player = (Player) event.getWhoClicked();

            if (event.getClick() == ClickType.SHIFT_RIGHT) {
                itemController.sellItemsForMoney(plugin, player, itemStack, true);
            } else if (event.getClick() == ClickType.LEFT) {
                itemController.sellItemsForMoney(plugin, player, itemStack, false);
            }
        }
    }


    private void updateItemStackQuantity(InventoryClickEvent event, int amount) {
        Inventory inventory = event.getInventory();
        ItemStack itemStack = inventory.getItem(PluginConfig.SHOP_OPERATIONS_ITEM_PLACE);

        if (itemStack != null) {
            int itemQuantity = itemStack.getAmount();
            int newQuantity = amount + itemQuantity;
            int maxItemStackQuantity = itemStack.getMaxStackSize();

            if (newQuantity > maxItemStackQuantity) newQuantity = maxItemStackQuantity;
            else if (newQuantity < 1) newQuantity = 1;

            updateItemStackPrice(event, itemStack, newQuantity);
        }

    }

    private void updateItemStackPrice(InventoryClickEvent event, ItemStack itemStack, int newQuantity) {
        String inventoryName = event.getView().getTitle();

        if (inventoryName.equalsIgnoreCase("Kup / Sprzedaj")) {
            updatePrice(itemStack, newQuantity);
        }
    }

    private void updatePrice(ItemStack itemStack, int newQuantity) {
        itemStack.setAmount(newQuantity);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            Double sellPrice = meta.getPersistentDataContainer().get(
                    new NamespacedKey(plugin, "sellPrice"), PersistentDataType.DOUBLE);
            Double buyPrice = meta.getPersistentDataContainer().get(
                    new NamespacedKey(plugin, "buyPrice"), PersistentDataType.DOUBLE);

            if (meta.getLore() != null && sellPrice != null && buyPrice != null) {
                List<String> lore = meta.getLore();
                lore.set(0, ChatColor.GREEN + "Buy price: " + buyPrice * newQuantity + "$");
                lore.set(1, ChatColor.RED + "Sell price: " + sellPrice * newQuantity + "$");
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            }
        }
    }

}
