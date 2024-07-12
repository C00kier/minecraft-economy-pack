package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.Material;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopTabController;
import pawel.cookier.ignaczak.economypack.utility.IShopUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopTabController implements IShopTabController {
    @Override
    public List<String> addCategoryOnTabComplete(String[] args){
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("<nazwa kategorii>");
        } else if (args.length == 2) {
            suggestions.addAll(
                    Arrays.stream(Material.values())
                            .map(Material::name)
                            .toList());

            if (!args[1].isEmpty()) {
                return suggestions.stream()
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
            }
        }
        return suggestions;
    }

    @Override
    public List<String> removeCategoryOnTabComplete(String[] args){
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(
                    IShopUtility.getItemNamesFromInventory(ShopConfig.SHOP_MAIN_INVENTORY)
            );

            if (!args[0].isEmpty()) {
                return suggestions.stream()
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
        } else if (args.length == 2) {
            suggestions.add("<nazwa kategorii>");
        }

        return suggestions;
    }
}
