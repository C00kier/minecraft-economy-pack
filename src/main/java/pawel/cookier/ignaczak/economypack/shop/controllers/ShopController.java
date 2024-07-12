package pawel.cookier.ignaczak.economypack.shop.controllers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pawel.cookier.ignaczak.economypack.config.PluginConfig;
import pawel.cookier.ignaczak.economypack.config.ShopConfig;
import pawel.cookier.ignaczak.economypack.shop.repository.IShopController;
import pawel.cookier.ignaczak.economypack.utility.IShopUtility;

import java.util.ArrayList;
import java.util.List;

public class ShopController implements IShopController {
    private final Inventory shopMain;

    public ShopController() {
        this.shopMain = ShopConfig.SHOP_MAIN_INVENTORY;
    }

    @Override
    public void openInventoryMenu(Player player) {
        player.openInventory(shopMain);
    }

    @Override
    public boolean isShiftMouseClick(InventoryClickEvent event) {
        return event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
    }

    @Override
    public void addShopCategory(Player player, Inventory inventory, String[] args) {
        if (player.isOp()) {
            if (isAddShopCategoryValid(player, inventory, args)) {
                int emptyIndex = inventory.firstEmpty();
                String displayName = args[0];
                Material material = Material.getMaterial(args[1]);

                ItemStack item = IShopUtility.createItemStack(material, displayName);
                inventory.setItem(emptyIndex, item);

                player.sendMessage(ChatColor.GREEN + "Dodano %s do sklepu".formatted(displayName));
            }
        }
    }

    @Override
    public void removeCategoryFromShop(Player player, Inventory inventory, String[] args) {
        if(player.isOp()){
            if(!doesCategoryNotExist(player, inventory, args,false)
            && hasCorrectNumberOfArgs(player,args, PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND)
            && hasSameArgs(player,args)){

                String categoryName = args[0];
                ItemStack[] contents = inventory.getContents();
                List<ItemStack> itemsList = new ArrayList<>();

                for (int i = 0; i < ShopConfig.SHOP_FIELDS_TO_FILL_UP; i++) {
                    ItemStack item = contents[i];
                    if (item != null) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null && categoryName.equals(meta.getDisplayName())) {
                            inventory.setItem(i, null);
                            player.sendMessage(ChatColor.GREEN + "Usunięto kategorię %s".formatted(categoryName));
                            continue;
                        }
                        itemsList.add(item);
                    }
                }

                IShopUtility.sortInventory(inventory, itemsList);
            }
        }
    }



    private boolean isAddShopCategoryValid(Player player, Inventory inventory, String[] args) {
        return hasCorrectNumberOfArgs(player, args, PluginConfig.ADD_SHOP_CATEGORY_COMMAND)
                && areArgsCorrectType(player, args)
                && doesCategoryNotExist(player, inventory, args,true)
                && hasEnoughSpaceInShop(player, inventory);
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

    private boolean hasCorrectNumberOfArgs(Player player, String[] args, String command) {
        if (args.length == 2) {
            return true;
        }

        switch (command){
            case PluginConfig.ADD_SHOP_CATEGORY_COMMAND -> player
                    .sendMessage(ChatColor.RED + "Musisz podać dokładnie dwa argumenty" +
                    "/%s <nazwa kategorii> <typ materiału jaki ma się wyświetlić w sklepie>"
                            .formatted(PluginConfig.ADD_SHOP_CATEGORY_COMMAND));
            case PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND -> player
                    .sendMessage(ChatColor.RED + "Musisz podać nazwę kategorii 2 razy " +
                    "/%s <nazwa kategorii> <nazwa kategorii>"
                            .formatted(PluginConfig.REMOVE_SHOP_CATEGORY_COMMAND));
        }

        return false;
    }

    private boolean areArgsCorrectType(Player player, String[] args) {
        if (Material.getMaterial(args[1].toUpperCase()) != null) {
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nie znaleziono materiału: %s".formatted(args[1]));

        return false;
    }

    private boolean doesCategoryNotExist(Player player, Inventory inventory, String[] args, boolean isAddingCategory) {
        String categoryName = args[0];

        List<String> itemNamesArray = IShopUtility.getItemNamesFromInventory(inventory);

        if (itemNamesArray.contains(categoryName)) {
            if(isAddingCategory){
                player.sendMessage(ChatColor.RED + "Istnieje już kategoria o nazwie " + categoryName);
            }
            return false;
        }

        if(!isAddingCategory){
            player.sendMessage(ChatColor.RED + "Nie znaleziono kategorii " + categoryName);
        }
        return true;
    }

    private boolean hasSameArgs(Player player, String[] args){
        if(args[0].equalsIgnoreCase(args[1])){
            return true;
        }

        player.sendMessage(ChatColor.RED + "Nazwa %s nie jest taka sama jak %s".formatted(args[0], args[1]));
        return false;
    }



}
