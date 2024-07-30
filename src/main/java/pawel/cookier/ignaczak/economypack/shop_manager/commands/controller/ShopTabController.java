package pawel.cookier.ignaczak.economypack.shop_manager.commands.controller;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;
import pawel.cookier.ignaczak.economypack.shop_manager.commands.repository.IShopTabController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ShopTabController implements IShopTabController {
    private final Shop shop;

    public ShopTabController(Shop shop) {
        this.shop = shop;
    }

    @Override
    public List<String> getListOfSuggestionsForCommands(String commandName, String[] args) {
        List<String> suggestions = new ArrayList<>();

        switch (commandName) {
            case PluginConfig.CALL_SHOP_COMMAND -> suggestions.add("");
            case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> suggestions.addAll(getAddCategorySuggestions(args));
            case PluginConfig.EDIT_SHOP_CATEGORY_ICON_COMMAND ->
                    suggestions.addAll(getEditCategoryItemStackTypeSuggestions(args));
            case PluginConfig.EDIT_SHOP_CATEGORY_NAME_COMMAND,
                    PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND ->
                    suggestions.addAll(getRemoveEditNameCategorySuggestions(args));
            case PluginConfig.ADD_SHOP_ITEM_COMMAND -> suggestions.addAll(getAddItemOnSuggestions(args));
            case PluginConfig.ADD_ITEM_FROM_HAND_TO_CATEGORY_COMMAND ->
                    suggestions.addAll(getAddItemFromHandSuggestions(args));
            case PluginConfig.REMOVE_ITEM_COMMAND -> suggestions.addAll(getRemoveItemSuggestions(args));
            case PluginConfig.EDIT_ITEM_SELL_PRICE_COMMAND -> suggestions.addAll(getEditItemSellPriceSuggestions(args));
            case PluginConfig.EDIT_ITEM_BUY_PRICE_COMMAND -> suggestions.addAll(getEditItemBuyPriceSuggestions(args));
        }

        return suggestions;
    }


    private List<String> getAddCategorySuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("<nazwa kategorii>");
        } else if (args.length == 2) {
            suggestions.addAll(autocompleteWithMaterialNames(1, args));
        }
        return suggestions;
    }

    private List<String> getEditCategoryItemStackTypeSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(autocompleteWithCategoryDisplayName(0, args));
        } else if (args.length == 2) {
            suggestions.addAll(autocompleteWithMaterialNames(1, args));
        }
        return suggestions;
    }

    private List<String> getRemoveEditNameCategorySuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(autocompleteWithCategoryDisplayName(0, args));
        } else if (args.length == 2) {
            suggestions.add("<nazwa kategorii>");
        }

        return suggestions;
    }

    private List<String> getAddItemOnSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(autocompleteWithCategoryDisplayName(0, args));
        } else if (args.length == 2) {
            suggestions.addAll(autocompleteWithMaterialNames(1, args));
        } else if (args.length == 3) {
            suggestions.add("<sell price>");
        } else if (args.length == 4) {
            suggestions.add("<buy price>");
        }
        return suggestions;
    }

    private List<String> getAddItemFromHandSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(autocompleteWithCategoryDisplayName(0, args));
        } else if (args.length == 2) {
            suggestions.add("<sell price>");
        } else if (args.length == 3) {
            suggestions.add("<buy price>");
        }
        return suggestions;
    }

    private List<String> getRemoveItemSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(autocompleteWithCategoryDisplayName(0, args));
        } else if (args.length == 2) {
            suggestions.add("<item_entity id>");
        }
        return suggestions;
    }

    private List<String> getEditItemSellPriceSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("<item_entity id>");
        } else if (args.length == 2) {
            suggestions.add("<sell price>");
        }
        return suggestions;
    }

    private List<String> getEditItemBuyPriceSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("<item_entity id>");
        } else if (args.length == 2) {
            suggestions.add("<buy price>");
        }
        return suggestions;
    }

    private List<String> autocompleteWithCategoryDisplayName(int argIndex, String[] args) {
        List<String> suggestions = new ArrayList<>(
                getItemNamesFromInventory(shop.getInventory()));

        if (!args[argIndex].isEmpty()) {
            return suggestions.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[argIndex].toLowerCase()))
                    .toList();
        }

        return suggestions;
    }

    private List<String> autocompleteWithMaterialNames(int argIndex, String[] args) {
        List<String> suggestions = new ArrayList<>(
                Arrays.stream(Material.values())
                        .map(Material::name)
                        .toList());

        if (!args[argIndex].isEmpty()) {
            return suggestions.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[argIndex].toLowerCase()))
                    .toList();
        }
        return suggestions;
    }


    private List<String> getItemNamesFromInventory(Inventory inventory){
        return Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .map(ItemStack::getItemMeta)
                .filter(Objects::nonNull)
                .map(ItemMeta::getDisplayName)
                .toList();
    }
}
