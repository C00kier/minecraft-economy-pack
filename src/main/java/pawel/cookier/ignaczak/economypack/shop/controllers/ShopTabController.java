package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.Material;
import pawel.cookier.ignaczak.economypack.shop.models.Shop;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopTabController;
import pawel.cookier.ignaczak.economypack.shop.utility.IShopUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopTabController implements IShopTabController {
    private final Shop shop;

    public ShopTabController(Shop shop) {
        this.shop = shop;
    }

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
                IShopUtility.getItemNamesFromInventory(shop.getInventory()));

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
