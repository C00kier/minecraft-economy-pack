package pawel.cookier.ignaczak.economypack.shop.models;

import org.bukkit.inventory.ItemStack;

public record InventoryItem (ItemStack item, int row, int column) {
}
