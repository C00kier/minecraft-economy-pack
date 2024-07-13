package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopCommandsController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;
import pawel.cookier.ignaczak.economypack.shop.validation.ShopCommandsValidation;

import java.util.ArrayList;
import java.util.List;

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
    public void openInventoryMenu(Player player) {
        player.openInventory(shop.getInventory());
    }

    @Override
    public boolean isShiftMouseClick(InventoryClickEvent event) {
        return event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
    }

    @Override
    public void addShopCategory(Player player, Inventory inventory, String[] args) {
        if (validation.isAddShopCategoryValid(player,inventory,args)) {
            int emptyIndex = inventory.firstEmpty();
            String displayName = args[0];
            Material material = Material.getMaterial(args[1]);

            ItemStack item = IShopUtility.createItemStack(material, displayName);
            inventory.setItem(emptyIndex, item);

            player.sendMessage(ChatColor.GREEN + "Dodano kategorię %s do sklepu".formatted(displayName));
        }
    }

    @Override
    public void removeCategoryFromShop(Player player, Inventory inventory, String[] args) {
        if (validation.isRemoveCategoryValid(player, inventory, args)) {
            String categoryName = args[0];
            ItemStack[] contents = inventory.getContents();
            List<ItemStack> itemsList = new ArrayList<>();

            for (int i = 0; i < PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP; i++) {
                ItemStack item = contents[i];
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && categoryName.equals(meta.getDisplayName())) {
                        inventory.setItem(i, null);
                        player.sendMessage(ChatColor.GREEN + "Usunięto kategorię %s".formatted(categoryName));
                        continue;
                    }
                    itemsList.add(item);
                }
            }
            IShopUtility.sortInventory(inventory, itemsList);
        }
    }

    @Override
    public void editShopCategoryName(Player player, Inventory inventory, String[] args) {
        if (validation.isEditCategoryNameValid(player, inventory, args)) {
            String oldName = args[0];
            String newName = args[1];
            ItemStack[] contents = inventory.getContents();

            for (int i = 0; i < PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP; i++) {
                ItemStack item = contents[i];
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && oldName.equals(meta.getDisplayName())) {
                        meta.setDisplayName(newName);
                        item.setItemMeta(meta);
                        player.sendMessage(ChatColor.GREEN + "Zmieniono nazwę %s na %s"
                                .formatted(oldName, newName));
                    }
                }
            }
        }
    }

    @Override
    public void editShopCategoryItemStack(Player player, Inventory inventory, String[] args) {
        if (validation.isEditCategoryItemStackValid(player, inventory, args)) {
            String categoryName = args[0];
            Material material = Material.getMaterial(args[1]);
            ItemStack[] contents = inventory.getContents();

            for (int i = 0; i < PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP; i++) {
                ItemStack item = contents[i];
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && categoryName.equals(meta.getDisplayName())) {
                        ItemStack newItem = IShopUtility.createItemStack(material, categoryName);
                        inventory.setItem(i, newItem);
                        player.sendMessage(ChatColor.GREEN + "Zmieniono ikonę");
                    }
                }
            }
        }
    }

}
