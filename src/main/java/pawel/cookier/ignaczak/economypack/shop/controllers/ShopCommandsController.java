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
    private final CategoryController categoryController;
    private final ShopController shopController;

    public ShopCommandsController(Shop shop,
                                  ShopCommandsValidation validation,
                                  CategoryController categoryController,
                                  ShopController shopController) {
        this.shop = shop;
        this.validation = validation;
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
        if (validation.isRemoveCategoryValid(player, shop.getInventory(), args)) {
            String categoryName = args[0];
            Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

            if (optionalCategory.isPresent()) {
                Category categoryToRemove = optionalCategory.get();
                shopController.removeCategoryFromShop(shop, categoryToRemove);
                player.sendMessage(ChatColor.GREEN + "Usunięto kategorię %s".formatted(categoryName));
            }
        }
    }

    @Override
    public void editShopCategoryName(Player player, String[] args) {
        if (validation.isEditCategoryNameValid(player, shop.getInventory(), args)) {
            String oldName = args[0];

            Optional<Category> category = shopController.findCategoryByName(shop, oldName);
            if (category.isPresent()) {
                String newName = args[1];
                Category categoryToEdit = category.get();

                categoryController.editCategoryItemStackName(categoryToEdit, newName);
                shopController.updateShopInventory(shop);

                player.sendMessage(ChatColor.GREEN + "Zmieniono nazwę %s na %s"
                        .formatted(oldName, newName));
            }
        }
    }

    @Override
    public void editShopCategoryItemStack(Player player, String[] args) {
        if (validation.isEditCategoryItemStackValid(player, shop.getInventory(), args)) {
            String categoryName = args[0];
            Optional<Category> category = shopController.findCategoryByName(shop, categoryName);

            if (category.isPresent()) {
                Category categoryToEdit = category.get();

                Material material = Material.getMaterial(args[1]);
                ItemStack newItem = IShopUtility.createItemStack(material, categoryName);

                categoryController.editCategoryItemStackMaterial(categoryToEdit, newItem);
                shopController.updateShopInventory(shop);
                player.sendMessage(ChatColor.GREEN + "Zmieniono obiekt");
            }
        }
    }

    @Override
    public void addShopItem(JavaPlugin plugin, Player player, String[] args) {
        if (validation.isAddShopItemValid(player, args)) {

            String categoryName = args[0];
            Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                Material material = Material.getMaterial(args[1]);
                double sellPrice = Double.parseDouble(args[2]);
                double buyPrice = Double.parseDouble(args[3]);

                Item item = createItem(plugin, material, sellPrice, buyPrice);
                categoryController.addItemToCategory(category, item);
                player.sendMessage(
                        ChatColor.GREEN + "Dodano %s do kategorii %s".formatted(args[1], categoryName));
            }
        }
    }

    private Item createItem(JavaPlugin plugin, Material material, double sellPrice, double buyPrice) {
        ItemStack itemStack = new ItemStack(material);
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

        return new Item(itemStack, sellPrice, buyPrice);
    }

}
