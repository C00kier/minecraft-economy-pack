package pawel.cookier.ignaczak.economypack.shop_manager.events.repository;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface IItemInventoryEventsController {
    void minusItemQuantity64ButtonEvent(InventoryClickEvent event);

    void minusItemQuantity16ButtonEvent(InventoryClickEvent event);

    void minusItemQuantity1ButtonEvent(InventoryClickEvent event);

    void plusItemQuantity64ButtonEvent(InventoryClickEvent event);

    void plusItemQuantity16ButtonEvent(InventoryClickEvent event);

    void plusItemQuantity1ButtonEvent(InventoryClickEvent event);

    void buyButtonEvent(InventoryClickEvent event);

    void sellButtonEvent(InventoryClickEvent event);
}
