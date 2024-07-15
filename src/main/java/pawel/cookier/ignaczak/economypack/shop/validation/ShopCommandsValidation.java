package pawel.cookier.ignaczak.economypack.shop.validation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.CategoryController;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopController;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopCommandsValidation;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.List;
import java.util.Optional;

public class ShopCommandsValidation implements IShopCommandsValidation {

    private final Shop shop;
    private final ShopController shopController;
    private final CategoryController categoryController;

    public ShopCommandsValidation(Shop shop, ShopController shopController, CategoryController categoryController) {
        this.shop = shop;
        this.shopController = shopController;
        this.categoryController = categoryController;
    }

    @Override
    public boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args) {
        if (hasAddCategoryEnoughArgs(player, args)
                && isArgumentMaterial(player, args, 1)
                && hasEnoughSpaceInShop(player, inventory)) {
            if (doesCategoryNotExist(inventory, args)) {
                return true;
            } else {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Istnieje już kategoria o nazwie " + categoryName);
            }
        }
        return false;
    }

    @Override
    public boolean isRemoveCategoryValid(Player player, Inventory inventory, String[] args) {
        if (hasRemoveCategoryEnoughArgs(player, args)
                && hasSameArgs(player, args, PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND)) {
            if (doesCategoryNotExist(inventory, args)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEditCategoryNameValid(Player player, Inventory inventory, String[] args) {
        if (hasEditCategoryNameEnoughArgs(player, args)
                && !hasSameArgs(player, args, PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND)) {
            if (doesCategoryNotExist(inventory, args)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEditCategoryItemStackValid(Player player, Inventory inventory, String[] args) {
        if (hasEditCategoryItemStackEnoughArgs(player, args)
                && isArgumentMaterial(player, args, 1)) {
            if (doesCategoryNotExist(inventory, args)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAddShopItemValid(Player player, String[] args) {
        return hasCorrectQuantityOfArgsAddItem(player, args)
                && categoryExists(player, args, 0)
                && isArgumentMaterial(player, args, 1)
                && canArgumentBeParsedToDouble(player, args, 2)
                && canArgumentBeParsedToDouble(player, args, 3);
    }

    private boolean hasCorrectQuantityOfArgsAddItem(Player player, String[] args){
        if(args.length == 4){
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać dokładnie 4 argumenty "
        + "/%s <nazwa kategorii> <typ materiału> <cena sprzedaży> <cena kupna>".formatted(PluginConfig.ADD_SHOP_ITEM_COMMAND));
        return false;
    }

    private boolean categoryExists(Player player, String[] args, int argumentIndex){
        String categoryName = args[argumentIndex];
        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

        if(optionalCategory.isPresent()){
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii %s".formatted(categoryName));
        return false;
    }

    private boolean canArgumentBeParsedToDouble(Player player, String[] args, int argumentIndex){
        try {
            Double.parseDouble(args[argumentIndex]);
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Wprowadź liczbę. Wprowadzono: %s".formatted(args[argumentIndex]));
            return false;
        }
    }

    private boolean hasEnoughSpaceInShop(Player player, Inventory inventory) {
        for (int i = 0; i < PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                return true;
            }
        }

        player.sendMessage(ChatColor.RED + "Nie możesz dodać nowej kategorii. Osiągnąłeś limit.");
        return false;
    }

    private boolean hasAddCategoryEnoughArgs(Player player, String[] args) {
        if (args.length == 2) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać dokładnie dwa argumenty" +
                "/%s <nazwa kategorii> <item jaki ma się wyświetlić w sklepie>"
                        .formatted(PluginConfig.ADD_SHOP_CATEGORY_COMMAND));

        return false;
    }

    private boolean hasRemoveCategoryEnoughArgs(Player player, String[] args) {
        if (args.length == 2) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać nazwę kategorii 2 razy " +
                "/%s <nazwa kategorii> <nazwa kategorii>"
                        .formatted(PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND));

        return false;
    }

    private boolean hasEditCategoryNameEnoughArgs(Player player, String[] args) {
        if (args.length == 2) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać starą i nową nazwę kategorii " +
                "/%s <stara nazwa> <nowa nazwa>"
                        .formatted(PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND));

        return false;
    }

    private boolean hasEditCategoryItemStackEnoughArgs(Player player, String[] args) {
        if (args.length == 2) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać nazwę kategorii i materiału obiektu " +
                "/%s <nazwa kategorii> <nazwa materiału>"
                        .formatted(PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND));

        return false;
    }

    private boolean isArgumentMaterial(Player player, String[] args, int argumentIndex) {
        if (Material.getMaterial(args[argumentIndex].toUpperCase()) != null) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono materiału: %s".formatted(args[argumentIndex]));

        return false;
    }

    private boolean doesCategoryNotExist(Inventory inventory, String[] args) {
        String categoryName = args[0];
        List<String> itemNamesArray = IShopUtility.getItemNamesFromInventory(inventory);
        return !itemNamesArray.contains(categoryName);
    }

    private boolean hasSameArgs(Player player, String[] args, String command) {
        if (args[0].equalsIgnoreCase(args[1])) {
            if (command.equals(PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND)) {
                player.sendMessage(ChatColor.RED + "Kategoria ma już nazwę %s".formatted(args[0]));
            }
            return true;
        }

        if (command.equals(PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND)) {
            player.sendMessage(ChatColor.RED + "Nazwa %s nie jest taka sama jak %s".formatted(args[0], args[1]));
        }
        return false;
    }

}
