package pawel.cookier.ignaczak.economypack.shop.utility;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface IShopUtility {

    static ItemStack createItemStack(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setDisplayName(displayName);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        item.setItemMeta(meta);

        return item;
    }

    static List<String> getItemNamesFromInventory(Inventory inventory){
        return Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .map(ItemStack::getItemMeta)
                .filter(Objects::nonNull)
                .map(ItemMeta::getDisplayName)
                .toList();
    }

    static String extractDisplayNameFromMaterial(Material material){
        assert material != null;
        ItemStack itemStack = new ItemStack(material);
        return Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
    }

    static String formatMaterialName(String materialName) {
        String[] words = materialName.split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
        }
        return formattedName.toString().trim();
    }

}
