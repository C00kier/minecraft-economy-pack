package pawel.cookier.ignaczak.economypack.shop.validation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopCommandsValidation;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.List;

public class ShopCommandsValidation implements IShopCommandsValidation {

    @Override
    public boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args) {
        if (hasAddCategoryEnoughArgs(player, args)
                && isSecondArgumentMaterial(player, args)
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
                && isSecondArgumentMaterial(player, args)) {
            if (doesCategoryNotExist(inventory, args)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean hasEnoughSpaceInShop(Player player, Inventory inventory) {
        for (int i = 0; i < ShopConfig.SHOP_FIELDS_TO_FILL_UP; i++) {
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

    private boolean isSecondArgumentMaterial(Player player, String[] args) {
        if (Material.getMaterial(args[1].toUpperCase()) != null) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono materiału: %s".formatted(args[1]));

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
