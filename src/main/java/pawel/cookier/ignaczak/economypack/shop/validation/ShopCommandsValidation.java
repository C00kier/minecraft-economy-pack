package pawel.cookier.ignaczak.economypack.shop.validation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop.controllers.ShopController;
import pawel.cookier.ignaczak.economypack.shop.models.Category;
import pawel.cookier.ignaczak.economypack.shop.models.Item;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopCommandsValidation;

import java.util.Optional;

public class ShopCommandsValidation implements IShopCommandsValidation {

    private final Shop shop;
    private final ShopController shopController;

    public ShopCommandsValidation(Shop shop, ShopController shopController) {
        this.shop = shop;
        this.shopController = shopController;
    }

    @Override
    public boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args) {
        if (hasAddCategoryEnoughArgs(player, args)
                && isArgumentMaterial(player, args, 1)
                && hasEnoughSpaceInShop(player, inventory)) {
            if (!categoryExists(args, 0)) {
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
        if (hasRemoveCategoryEnoughArgs(player, args)
                && hasSameArgs(player, args, PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND)) {
            if (!categoryExists(args, 0)) {
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
        if (hasEditCategoryNameEnoughArgs(player, args)
                && !hasSameArgs(player, args, PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND)) {
            if (!categoryExists(args, 0)) {
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
        if (hasEditCategoryItemStackEnoughArgs(player, args)
                && isArgumentMaterial(player, args, 1)) {
            if (!categoryExists(args, 0)) {
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
        if (hasCorrectQuantityOfArgsAddItem(player, args)
                && isArgumentMaterial(player, args, 1)
                && canArgumentBeParsedToDouble(player, args, 2)
                && canArgumentBeParsedToDouble(player, args, 3)
                && isDoubleParsedValueGreaterThanZero(player, args, 2)
                && isDoubleParsedValueGreaterThanZero(player, args, 3)) {
            if (!categoryExists(args, 0)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAddItemFromHandToCategoryValid(Player player, String[] args) {
        if (hasCorrectQuantityOfArgsAddFromHand(player, args)
                && canArgumentBeParsedToDouble(player, args, 1)
                && canArgumentBeParsedToDouble(player, args, 2)
                && isDoubleParsedValueGreaterThanZero(player, args, 1)
                && isDoubleParsedValueGreaterThanZero(player, args, 2)) {
            if (!categoryExists(args, 0)) {
                String categoryName = args[0];
                player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEditItemSellPriceValid(Player player, String[] args) {
        return hasEditItemSellCorrectArgsQuantity(player, args)
                && canArgumentBeParsedToInteger(player, args, 0)
                && canArgumentBeParsedToDouble(player, args, 1)
                && isDoubleParsedValueGreaterThanZero(player, args, 1)
                && isItemWithPassedIdExisting(player, args, 0);
    }

    @Override
    public boolean isEditItemBuyPriceValid(Player player, String[] args) {
        return hasEditItemBuyCorrectArgsQuantity(player, args)
                && canArgumentBeParsedToInteger(player, args, 0)
                && canArgumentBeParsedToDouble(player, args, 1)
                && isDoubleParsedValueGreaterThanZero(player, args, 1)
                && isItemWithPassedIdExisting(player, args, 0);
    }

    private boolean isDoubleParsedValueGreaterThanZero(Player player,String[] args, int doubleIndex){
        double value = Double.parseDouble(args[doubleIndex]);

        if(value > 0){
            return true;
        }

        player.sendMessage(ChatColor.RED + "Wartość musi być większa od 0. Podano: %s".formatted(value));
        return false;
    }

    private boolean hasEditItemSellCorrectArgsQuantity(Player player, String[] args) {
        if (args.length == 2) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać dokładnie 2 argumenty "
                + "/%s <id przedmiotu> <cena sprzedaży>".formatted(PluginConfig.EDIT_ITEM_SELL_PRICE));
        return false;
    }

    private boolean hasEditItemBuyCorrectArgsQuantity(Player player, String[] args) {
        if (args.length == 2) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać dokładnie 2 argumenty "
                + "/%s <id przedmiotu> <cena kupna>".formatted(PluginConfig.EDIT_ITEM_BUY_PRICE));
        return false;
    }

    private boolean isItemWithPassedIdExisting(Player player, String[] args, int itemIdIndex){
        int itemId = Integer.parseInt(args[itemIdIndex]);

        Optional<Item> optionalItem = shopController.findItemByItemId(shop, itemId);
        if(optionalItem.isPresent()){
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono przedmiotu z id: %s".formatted(itemId));
        return false;
    }

    private boolean hasCorrectQuantityOfArgsAddFromHand(Player player, String[] args) {
        if (args.length == 3) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać dokładnie 3 argumenty "
                + "/%s <nazwa kategorii> <cena sprzedaży> <cena kupna>"
                .formatted(PluginConfig.ADD_ITEM_FROM_HAND_TO_CATEGORY));
        return false;
    }

    private boolean hasCorrectQuantityOfArgsAddItem(Player player, String[] args) {
        if (args.length == 4) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Musisz podać dokładnie 4 argumenty "
                + "/%s <nazwa kategorii> <typ materiału> <cena sprzedaży> <cena kupna>".formatted(PluginConfig.ADD_SHOP_ITEM_COMMAND));
        return false;
    }

    private boolean categoryExists(String[] args,
                                   int argumentIndex) {
        String categoryName = args[argumentIndex];
        Optional<Category> optionalCategory = shopController.findCategoryByName(shop, categoryName);

        return optionalCategory.isPresent();
    }

    private boolean canArgumentBeParsedToDouble(Player player, String[] args, int argumentIndex) {
        try {
            Double.parseDouble(args[argumentIndex]);
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Wprowadź liczbę. Wprowadzono: %s".formatted(args[argumentIndex]));
            return false;
        }
    }

    private boolean canArgumentBeParsedToInteger(Player player, String[] args, int argumentIndex) {
        try {
            Integer.parseInt(args[argumentIndex]);
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Wprowadź liczbę całkowitą. Wprowadzono: %s".formatted(args[argumentIndex]));
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
