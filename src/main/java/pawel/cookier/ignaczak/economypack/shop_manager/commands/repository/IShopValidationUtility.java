package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IShopValidationUtility {
    boolean hasEnoughArgs(Player player, int argsQuantity, String messageIfNot, String[] args);

    boolean isArgumentMaterial(Player player, String[] args, int argumentIndex);

    boolean doesCategoryExists(String[] args, int argumentIndex);

    boolean hasSameArgs(Player player, String[] args, String command);

    boolean hasEnoughSpaceInShop(Player player, Inventory inventory);

    boolean isDoubleParsedValueGreaterThanZero(Player player, String[] args, int doubleIndex);

    boolean isItemWithPassedIdExisting(Player player, String[] args, int itemIdIndex);

    boolean canArgumentBeParsedToDouble(Player player, String[] args, int argumentIndex);

    boolean canArgumentBeParsedToInteger(Player player, String[] args, int argumentIndex);
}
