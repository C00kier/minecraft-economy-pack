package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;
import pawel.cookier.ignaczak.economypack.shop.validation.ShopCommandsValidation;

import java.util.Objects;
import java.util.Optional;

public class ShopCommandsController implements IShopCommandsController {
    private final Shop shop;
    private final ShopCommandsValidation validation;
    private final CategoryController categoryController;
    private final ShopController shopController;

    public ShopCommandsController(Shop shop) {
        this.shop = shop;
        this.validation = new ShopCommandsValidation();
        this.categoryController = new CategoryController();
        this.shopController = new ShopController();
    }

    @Override
    public void openInventory(Player player, Inventory inventory) {
        player.openInventory(inventory);
    }

    @Override
    public boolean isShiftMouseClick(InventoryClickEvent event) {
        return event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
    }

    @Override
    public void addShopCategory(Player player, Inventory inventory, String[] args) {
        if (validation.isAddShopCategoryValid(player, inventory, args)) {
            String displayName = args[0];
            Material material = Material.getMaterial(args[1]);

            ItemStack item = IShopUtility.createItemStack(material, displayName);
            Category category = new Category(item);

            shopController.addCategoryToShop(shop, category);
            player.sendMessage(ChatColor.GREEN + "Dodano kategorię %s do sklepu".formatted(displayName));
        }
    }

    @Override
    public void removeCategoryFromShop(Player player, Inventory inventory, String[] args) {
        if (validation.isRemoveCategoryValid(player, inventory, args)) {
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
    public void editShopCategoryName(Player player, Inventory inventory, String[] args) {
        if (validation.isEditCategoryNameValid(player, inventory, args)) {
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
    public void editShopCategoryItemStack(Player player, Inventory inventory, String[] args) {
        if (validation.isEditCategoryItemStackValid(player, inventory, args)) {
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
    public void switchBetweenInventoriesBasedOnItemStack(Player player, ItemStack itemStack) {
        player.closeInventory();

        String displayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, displayName);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Inventory inventoryToOpen = category.getInventory();

            openInventory(player, inventoryToOpen);
        }
    }

}
