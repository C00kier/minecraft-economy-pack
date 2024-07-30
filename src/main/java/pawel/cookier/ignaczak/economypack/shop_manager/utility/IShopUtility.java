package pawel.cookier.ignaczak.economypack.shop_manager.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


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

}
