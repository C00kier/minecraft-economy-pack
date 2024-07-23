package pawel.cookier.ignaczak.economypack.shop_manager.commands.controller;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.controller.CategoryController;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.repository.IItemCommandsController;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.validation.ItemCommandsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.controller.ItemController;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

import java.util.List;
import java.util.Optional;

public class ItemCommandsController implements IItemCommandsController {

    private final Shop shop;
    private final ItemCommandsValidation validation;
    private final CategoryController categoryController;
    private final ShopController shopController;
    private final ItemController itemController;

    public ItemCommandsController(Shop shop,
                                  CategoryController categoryController,
                                  ShopController shopController,
                                  ItemController itemController) {
        this.shop = shop;
        this.categoryController = categoryController;
        this.shopController = shopController;
        this.itemController = itemController;
        this.validation = new ItemCommandsValidation(shop, shopController);
    }

    @Override
    public void registerItemOpCommands(JavaPlugin plugin, Player player, String commandName, String[] args) {
        if (player.isOp()) {
            switch (commandName) {
                case PluginConfig.ADD_SHOP_ITEM_COMMAND -> addShopItem(plugin, player, args);
                case PluginConfig.ADD_ITEM_FROM_HAND_TO_CATEGORY_COMMAND ->
                        addItemFromHandToCategory(plugin, player, args);
                case PluginConfig.REMOVE_ITEM_COMMAND -> removeItemFromCategory(player, args);
                case PluginConfig.EDIT_ITEM_SELL_PRICE_COMMAND -> editItemSellPrice(plugin, player, args);
                case PluginConfig.EDIT_ITEM_BUY_PRICE_COMMAND -> editItemBuyPrice(plugin, player, args);
            }
        }
    }

    private void addShopItem(JavaPlugin plugin, Player player, String[] args) {
        if (validation.isAddShopItemValid(player, args)) {

            String categoryName = args[0];
            shopController.findCategoryByName(shop, categoryName).ifPresent(category -> {

                Material material = Material.getMaterial(args[1]);
                if (material != null) {
                    double sellPrice = Double.parseDouble(args[2]);
                    double buyPrice = Double.parseDouble(args[3]);
                    ItemStack itemStack = new ItemStack(material);
                    Item item = createItem(plugin, itemStack, sellPrice, buyPrice);

                    categoryController.addItemToCategory(category, item);
                    player.sendMessage(
                            ChatColor.GREEN + "Dodano %s do kategorii %s".formatted(args[1], categoryName));
                }
            });
        }
    }

    private void addItemFromHandToCategory(JavaPlugin plugin, Player player, String[] args) {
        if (validation.isAddItemFromHandToCategoryValid(player, args)) {
            ItemStack itemStack = new ItemStack(player.getInventory().getItemInMainHand());
            shopController.findCategoryByName(shop, args[0]).ifPresent(category -> {
                itemStack.setAmount(1);
                ItemMeta meta = itemStack.getItemMeta();

                double sellPrice = Double.parseDouble(args[1]);
                double buyPrice = Double.parseDouble(args[2]);

                Item item = createItem(plugin, itemStack, sellPrice, buyPrice);
                categoryController.addItemToCategory(category, item);

                if (meta != null) {
                    player.sendMessage(ChatColor.GREEN + "Dodano %s do kategorii %s".formatted(
                            meta.getDisplayName(), args[0]));
                }
            });
        }
    }

    private void removeItemFromCategory(Player player, String[] args) {
        if (validation.isRemoveItemFromCategoryValid(player, args)) {
            String categoryName = args[0];
            int itemId = Integer.parseInt(args[1]);

            Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);
            if (optionalCategory.isPresent()) {
                Optional<Item> optionalItem = shopController.findItemByItemId(shop, itemId);
                if (optionalItem.isPresent()) {
                    Category category = optionalCategory.get();
                    Item item = optionalItem.get();

                    if (category.getListOfItems().contains(item)) {
                        categoryController.removeItemFromCategory(category, item);
                        player.sendMessage(ChatColor.GREEN +
                                "Usunięto item_entity o id %s z kategorii %s".formatted(itemId, categoryName));
                    } else {
                        player.sendMessage(ChatColor.RED +
                                "Nie znaleziono przedmiotu o id %s w kategorii %s".formatted(item, categoryName));
                    }
                }
            }
        }
    }

    private void editItemSellPrice(JavaPlugin plugin, Player player, String[] args) {
        if (validation.isEditItemSellPriceValid(player, args)) {
            int itemId = Integer.parseInt(args[0]);
            Double newSellPrice = Double.parseDouble(args[1]);

            shopController.findItemByItemId(shop, itemId).ifPresent(item ->
                    itemController.updateItemSellPrice(plugin, player, item, newSellPrice));
        }
    }

    private void editItemBuyPrice(JavaPlugin plugin, Player player, String[] args) {
        if (validation.isEditItemBuyPriceValid(player, args)) {
            int itemId = Integer.parseInt(args[0]);
            Double newSellPrice = Double.parseDouble(args[1]);

            shopController.findItemByItemId(shop, itemId).ifPresent(item ->
                    itemController.updateItemBuyPrice(plugin, player, item, newSellPrice));
        }
    }

    private Item createItem(JavaPlugin plugin,
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
        makeItemStackToDisplayItemId(item);

        return item;
    }

    private void makeItemStackToDisplayItemId(Item item) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            List<String> loreList = meta.getLore();
            if (loreList != null) {
                loreList.add(ChatColor.GRAY + "Item id: %s".formatted(item.getId()));
                meta.setLore(loreList);
                itemStack.setItemMeta(meta);
            }
        }
    }
}
