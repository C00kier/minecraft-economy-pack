package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.Material;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopTabController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopTabController implements IShopTabController {
    @Override
    public List<String> addEditCategoryIconOnTabComplete(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("<nazwa kategorii>");
        } else if (args.length == 2) {
            suggestions.addAll(autocompleteWithMaterialNames(1, args));
        }
        return suggestions;
    }

    @Override
    public List<String> removeEditNameCategoryOnTabComplete(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(autocompleteWithInventoryDisplayName(0, args));
        } else if (args.length == 2) {
            suggestions.add("<nazwa kategorii>");
        }

        return suggestions;
    }

    private List<String> autocompleteWithInventoryDisplayName(int argIndex, String[] args) {
        List<String> suggestions = new ArrayList<>(
                IShopUtility.getItemNamesFromInventory(ShopConfig.SHOP_MAIN_INVENTORY));

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
}
