package pawel.cookier.ignaczak.economypack.shop_manager.events.controller;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pawel.cookier.ignaczak.economypack.shop_manager.events.repository.IShopInventoryEventsController;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.model.Shop;
import pawel.cookier.ignaczak.economypack.shop_manager.shop_entity.controller.ShopController;

import java.util.Objects;

public class ShopInventoryInventoryEventsController implements IShopInventoryEventsController {

    private final ShopController shopController;
    private final Shop shop;
    private final ShopEventsUtility utility;

    public ShopInventoryInventoryEventsController(ShopController shopController,
                                                  Shop shop, ShopEventsUtility utility) {
        this.shopController = shopController;
        this.shop = shop;
        this.utility = utility;
    }

    @Override
    public void onShopInventoryLeftClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.equals(shop.getInventory())) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            if (isShiftMouseClick(event)) return;

            clickCategoryEvent(shop, event, clickedItem);
        }
    }

    private void clickCategoryEvent(Shop shop, InventoryClickEvent event, ItemStack itemStack) {
        String categoryName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();

        shopController.findCategoryByName(shop, categoryName).ifPresent(category -> {
            category.setCurrentPage(1);
            Player player = (Player) event.getWhoClicked();
            utility.switchToCategoryInventory(player, category);
        });
    }

    private boolean isShiftMouseClick(InventoryClickEvent event) {
        return event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
    }

}
