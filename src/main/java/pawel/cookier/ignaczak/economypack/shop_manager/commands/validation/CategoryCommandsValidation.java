package pawel.cookier.ignaczak.economypack.shop_manager.commands.validation;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.repository.ICategoryCommandsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class CategoryCommandsValidation implements ICategoryCommandsValidation {

    private final ShopCommandsValidationUtility validationFunctions;

    public CategoryCommandsValidation(Shop shop, ShopController shopController) {
        this.validationFunctions = new ShopCommandsValidationUtility(shop, shopController);
    }

    @Override
    public boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać dokładnie dwa argumenty" +
                "/%s <nazwa kategorii> <item_entity jaki ma się wyświetlić w sklepie>"
                        .formatted(PluginConfig.ADD_SHOP_CATEGORY_COMMAND);

        if (validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && validationFunctions.isArgumentMaterial(player, args, 1)
                && validationFunctions.hasEnoughSpaceInShop(player, inventory)) {
            if (!validationFunctions.doesCategoryExists(args, 0)) {
                return true;
            } else {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Istnieje już kategoria o nazwie " + categoryName);
            }
        }
        return false;
    }

    @Override
    public boolean isRemoveCategoryValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać nazwę kategorii 2 razy " +
                "/%s <nazwa kategorii> <nazwa kategorii>"
                        .formatted(PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND);

        if (validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && validationFunctions.hasSameArgs(player, args, PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND)) {
            if (!validationFunctions.doesCategoryExists(args, 0)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEditCategoryNameValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać starą i nową nazwę kategorii " +
                "/%s <stara nazwa> <nowa nazwa>"
                        .formatted(PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND);

        if (validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && !validationFunctions.hasSameArgs(player, args, PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND)) {
            if (!validationFunctions.doesCategoryExists(args, 0)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEditCategoryItemStackValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać nazwę kategorii i materiału obiektu " +
                "/%s <nazwa kategorii> <nazwa materiału>"
                        .formatted(PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND);

        if (validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && validationFunctions.isArgumentMaterial(player, args, 1)) {
            if (!validationFunctions.doesCategoryExists(args, 0)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

}
