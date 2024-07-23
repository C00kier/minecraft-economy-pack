package pawel.cookier.ignaczak.economypack.shop_manager.commands.validation;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.repository.IItemCommandsValidation;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;

public class ItemCommandsValidation implements IItemCommandsValidation {

    private final ShopCommandsValidationUtility validationFunctions;

    public ItemCommandsValidation(Shop shop, ShopController shopController) {
        this.validationFunctions = new ShopCommandsValidationUtility(shop, shopController);
    }

    @Override
    public boolean isAddShopItemValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać dokładnie 4 argumenty "
                + "/%s <nazwa kategorii> <typ materiału> <cena sprzedaży> <cena kupna>"
                .formatted(PluginConfig.ADD_SHOP_ITEM_COMMAND);

        if (validationFunctions.hasEnoughArgs(player, 4, messageForIncorrectArgsQuantity, args)
                && validationFunctions.isArgumentMaterial(player, args, 1)
                && validationFunctions.canArgumentBeParsedToDouble(player, args, 2)
                && validationFunctions.canArgumentBeParsedToDouble(player, args, 3)
                && validationFunctions.isDoubleParsedValueGreaterThanZero(player, args, 2)
                && validationFunctions.isDoubleParsedValueGreaterThanZero(player, args, 3)) {
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
    public boolean isAddItemFromHandToCategoryValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać dokładnie 3 argumenty "
                + "/%s <nazwa kategorii> <cena sprzedaży> <cena kupna>"
                .formatted(PluginConfig.ADD_ITEM_FROM_HAND_TO_CATEGORY_COMMAND);

        if (validationFunctions.hasEnoughArgs(player, 3, messageForIncorrectArgsQuantity, args)
                && validationFunctions.canArgumentBeParsedToDouble(player, args, 1)
                && validationFunctions.canArgumentBeParsedToDouble(player, args, 2)
                && validationFunctions.isDoubleParsedValueGreaterThanZero(player, args, 1)
                && validationFunctions.isDoubleParsedValueGreaterThanZero(player, args, 2)) {
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
    public boolean isEditItemSellPriceValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać dokładnie 2 argumenty "
                + "/%s <id przedmiotu> <cena sprzedaży>".formatted(PluginConfig.EDIT_ITEM_SELL_PRICE_COMMAND);

        return validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && validationFunctions.canArgumentBeParsedToInteger(player, args, 0)
                && validationFunctions.canArgumentBeParsedToDouble(player, args, 1)
                && validationFunctions.isDoubleParsedValueGreaterThanZero(player, args, 1)
                && validationFunctions.isItemWithPassedIdExisting(player, args, 0);
    }

    @Override
    public boolean isEditItemBuyPriceValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać dokładnie 2 argumenty "
                + "/%s <id przedmiotu> <cena kupna>".formatted(PluginConfig.EDIT_ITEM_BUY_PRICE_COMMAND);

        return validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && validationFunctions.canArgumentBeParsedToInteger(player, args, 0)
                && validationFunctions.canArgumentBeParsedToDouble(player, args, 1)
                && validationFunctions.isDoubleParsedValueGreaterThanZero(player, args, 1)
                && validationFunctions.isItemWithPassedIdExisting(player, args, 0);
    }

    @Override
    public boolean isRemoveItemFromCategoryValid(Player player, String[] args) {
        String messageForIncorrectArgsQuantity = ChatColor.RED + "Musisz podać dokładnie 2 argumenty "
                + "/%s <nazwa kategorii> <item_entity id>".formatted(PluginConfig.REMOVE_ITEM_COMMAND);

        return validationFunctions.hasEnoughArgs(player, 2, messageForIncorrectArgsQuantity, args)
                && validationFunctions.doesCategoryExists(args, 0)
                && validationFunctions.canArgumentBeParsedToInteger(player, args, 1)
                && validationFunctions.isItemWithPassedIdExisting(player, args, 1);
    }

}
