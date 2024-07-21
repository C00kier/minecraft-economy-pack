package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;
import pawel.cookier.ignaczak.economypack.shop.validation.ShopCommandsValidation;

import java.util.List;
import java.util.Optional;

public class ShopCommandsController implements IShopCommandsController {
    private final Shop shop;
    private final ShopCommandsValidation validation;
    private final ItemController itemController;
    private final CategoryController categoryController;
    private final ShopController shopController;

    public ShopCommandsController(Shop shop,
                                  ShopCommandsValidation validation,
                                  ItemController itemController, CategoryController categoryController,
                                  ShopController shopController) {
        this.shop = shop;
        this.validation = validation;
        this.itemController = itemController;
        this.categoryController = categoryController;
        this.shopController = shopController;
    }

    @Override
    public void openInventory(Player player, Inventory inventory) {
        player.openInventory(inventory);
    }

    @Override
    public void addShopCategory(Player player, String[] args) {
        if (validation.isAddShopCategoryValid(player, shop.getInventory(), args)) {
            String displayName = args[0];
            Material material = Material.getMaterial(args[1]);

            ItemStack item = IShopUtility.createItemStack(material, displayName);
            Category category = new Category(item);

            shopController.addCategoryToShop(shop, category);
            player.sendMessage(ChatColor.GREEN + "Dodano kategorię %s do sklepu".formatted(displayName));
        }
    }

    @Override
    public void removeCategoryFromShop(Player player, String[] args) {
        if (validation.isRemoveCategoryValid(player, args)) {
            String categoryName = args[0];

            shopController.findCategoryByName(shop, categoryName).ifPresent(category -> {
                shopController.removeCategoryFromShop(shop, category);
                player.sendMessage(ChatColor.GREEN + "Usunięto kategorię %s".formatted(categoryName));
            });
        }
    }

    @Override
    public void editShopCategoryName(Player player, String[] args) {
        if (validation.isEditCategoryNameValid(player, args)) {
            String oldName = args[0];

            shopController.findCategoryByName(shop, oldName).ifPresent(category -> {
                String newName = args[1];
                categoryController.editCategoryItemStackName(category, newName);
                shopController.updateShopInventory(shop);

                player.sendMessage(ChatColor.GREEN + "Zmieniono nazwę %s na %s"
                        .formatted(oldName, newName));
            });
        }
    }

    @Override
    public void editShopCategoryItemStack(Player player, String[] args) {
        if (validation.isEditCategoryItemStackValid(player, args)) {
            String categoryName = args[0];

            shopController.findCategoryByName(shop, categoryName).ifPresent(category -> {

                Material material = Material.getMaterial(args[1]);
                ItemStack newItem = IShopUtility.createItemStack(material, categoryName);

                categoryController.editCategoryItemStackMaterial(category, newItem);
                shopController.updateShopInventory(shop);
                player.sendMessage(ChatColor.GREEN + "Zmieniono obiekt");
            });
        }
    }

    @Override
    public void addShopItem(JavaPlugin plugin, Player player, String[] args) {
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

    @Override
    public void addItemFromHandToCategory(JavaPlugin plugin, Player player, String[] args) {
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

    @Override
    public void removeItemFromCategory(JavaPlugin plugin, Player player, String[] args) {
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
                                "Usunięto item o id %s z kategorii %s".formatted(itemId, categoryName));
                    }else {
                        player.sendMessage(ChatColor.RED +
                                "Nie znaleziono przedmiotu o id %s w kategorii %s". formatted(item, categoryName));
                    }
                }
            }
        }
    }

    @Override
    public void editItemSellPrice(JavaPlugin plugin, Player player, String[] args) {
        if (validation.isEditItemSellPriceValid(player, args)) {
            int itemId = Integer.parseInt(args[0]);
            Double newSellPrice = Double.parseDouble(args[1]);

            shopController.findItemByItemId(shop, itemId).ifPresent(item ->
                    itemController.updateItemSellPrice(plugin, player, item, newSellPrice));
        }
    }

    @Override
    public void editItemBuyPrice(JavaPlugin plugin, Player player, String[] args) {
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
