package pawel.cookier.ignaczak.economypack.shop_manager.commands.validation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.category_entity.model.Category;
import pawel.cookier.ignaczak.economypack.shop_manager.item_entity.model.Item;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

import java.util.Optional;

public class ShopCommandsValidationUtility implements pawel.cookier.ignaczak.economypack.shop_manager.commands.repository.IShopValidationUtility {

    private final Shop shop;
    private final ShopController shopController;

    public ShopCommandsValidationUtility(Shop shop, ShopController shopController) {
        this.shop = shop;
        this.shopController = shopController;
    }

    @Override
    public boolean doesCategoryExists(String[] args,
                                       int argumentIndex) {
        String categoryName = args[argumentIndex];
        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

        return optionalCategory.isPresent();
    }

    @Override
    public boolean isArgumentMaterial(Player player, String[] args, int argumentIndex) {
        if (Material.getMaterial(args[argumentIndex].toUpperCase()) != null) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono materiału: %s".formatted(args[argumentIndex]));

        return false;
    }

    @Override
    public boolean hasEnoughArgs(Player player, int argsQuantity, String messageIfNot, String[] args) {
        if (args.length == argsQuantity) {
            return true;
        }

        player.sendMessage(messageIfNot);
        return false;
    }

    @Override
    public boolean hasSameArgs(Player player, String[] args, String command) {
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

    @Override
    public boolean hasEnoughSpaceInShop(Player player, Inventory inventory) {
        for (int i = 0; i < PluginConfig.SHOP_INVENTORY_FIELDS_TO_FILL_UP; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                return true;
            }
        }

        player.sendMessage(ChatColor.RED + "Nie możesz dodać nowej kategorii. Osiągnąłeś limit.");
        return false;
    }

    @Override
    public boolean isDoubleParsedValueGreaterThanZero(Player player, String[] args, int doubleIndex) {
        double value = Double.parseDouble(args[doubleIndex]);

        if (value > 0) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Wartość musi być większa od 0. Podano: %s".formatted(value));
        return false;
    }

    @Override
    public boolean isItemWithPassedIdExisting(Player player, String[] args, int itemIdIndex) {
        int itemId = Integer.parseInt(args[itemIdIndex]);

        Optional<Item> optionalItem = shopController.findItemByItemId(shop, itemId);
        if (optionalItem.isPresent()) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono przedmiotu z id: %s".formatted(itemId));
        return false;
    }

    @Override
    public boolean canArgumentBeParsedToDouble(Player player, String[] args, int argumentIndex) {
        try {
            Double.parseDouble(args[argumentIndex]);
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Wprowadź liczbę. Wprowadzono: %s".formatted(args[argumentIndex]));
            return false;
        }
    }

    @Override
    public boolean canArgumentBeParsedToInteger(Player player, String[] args, int argumentIndex) {
        try {
            Integer.parseInt(args[argumentIndex]);
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Wprowadź liczbę całkowitą. Wprowadzono: %s".formatted(args[argumentIndex]));
            return false;
        }
    }

}
