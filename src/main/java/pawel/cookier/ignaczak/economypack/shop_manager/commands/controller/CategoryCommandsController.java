package pawel.cookier.ignaczak.economypack.shop_manager.commands.controller;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.controller.CategoryController;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.repository.ICategoryCommandsController;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.validation.CategoryCommandsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;
import pawel.cookier.ignaczak.economypack.shop_manager.utility.IShopUtility;

public class CategoryCommandsController implements ICategoryCommandsController {
    private final Shop shop;
    private final CategoryCommandsValidation validation;
    private final CategoryController categoryController;
    private final ShopController shopController;

    public CategoryCommandsController(Shop shop,
                                      CategoryController categoryController,
                                      ShopController shopController) {
        this.shop = shop;
        this.categoryController = categoryController;
        this.shopController = shopController;
        this.validation = new CategoryCommandsValidation(shop, shopController);
    }

    @Override
    public void registerCategoryOpCommands(Player player, String commandName, String[] args) {
        if (player.isOp()) {
            switch (commandName) {
                case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> addShopCategory(player, args);
                case PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND -> removeCategoryFromShop(player, args);
                case PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND -> editShopCategoryName(player, args);
                case PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND -> editShopCategoryItemStack(player, args);
            }
        }
    }

    private void addShopCategory(Player player, String[] args) {
        if (validation.isAddShopCategoryValid(player, shop.getInventory(), args)) {
            String displayName = args[0];
            Material material = Material.getMaterial(args[1]);

            ItemStack item = IShopUtility.createItemStack(material, displayName);
            Category category = new Category(item);

            shopController.addCategoryToShop(shop, category);
            player.sendMessage(ChatColor.GREEN + "Dodano kategorię %s do sklepu".formatted(displayName));
        }
    }

    private void removeCategoryFromShop(Player player, String[] args) {
        if (validation.isRemoveCategoryValid(player, args)) {
            String categoryName = args[0];

            shopController.findCategoryByName(shop, categoryName).ifPresent(category -> {
                shopController.removeCategoryFromShop(shop, category);
                player.sendMessage(ChatColor.GREEN + "Usunięto kategorię %s".formatted(categoryName));
            });
        }
    }

    private void editShopCategoryName(Player player, String[] args) {
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

    private void editShopCategoryItemStack(Player player, String[] args) {
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
}
