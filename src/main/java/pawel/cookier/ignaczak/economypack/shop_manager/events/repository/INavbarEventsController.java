package pawel.cookier.ignaczak.economypack.shop_manager.events.repository;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface INavbarEventsController {
    void nextPageButtonLeftClickEvent(InventoryClickEvent event);

    void previousPageButtonLeftClickEvent(InventoryClickEvent event);

    void returnToShopButtonLeftClickEvent(InventoryClickEvent event);

    void returnToCategoryButtonLeftClickEvent(InventoryClickEvent event);
}
