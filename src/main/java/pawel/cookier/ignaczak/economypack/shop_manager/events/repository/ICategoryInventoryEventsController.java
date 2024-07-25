package pawel.cookier.ignaczak.economypack.shop_manager.events.repository;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ICategoryInventoryEventsController {
    void openBuyItemMenuLeftClickEvent(InventoryClickEvent event);

    void openSellItemMenuRightClickEvent(InventoryClickEvent event);

    void sellAllItemsOfCertainTypeShiftRightClickEvent(InventoryClickEvent event);
}
