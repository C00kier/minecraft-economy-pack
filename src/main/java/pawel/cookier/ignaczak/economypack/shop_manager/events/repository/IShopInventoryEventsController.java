package pawel.cookier.ignaczak.economypack.shop_manager.events.repository;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface IShopInventoryEventsController {
    void onShopInventoryLeftClickEvent(InventoryClickEvent event);
}
