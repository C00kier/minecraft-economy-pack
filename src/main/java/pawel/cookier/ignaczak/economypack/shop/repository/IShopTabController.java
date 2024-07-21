package pawel.cookier.ignaczak.economypack.shop.repository;

import java.util.List;

public interface IShopTabController {
    List<String> addCategoryOnTabComplete(String[] args);

    List<String> editCategoryItemStackTypeOnTabComplete(String[] args);

    List<String> removeEditNameCategoryOnTabComplete(String[] args);

    List<String> addItemOnTabComplete(String[] args);

    List<String> addItemFromHandOnTabComplete(String[] args);

    List<String> removeItemOnTabComplete(String[] args);

    List<String> editItemSellPriceOnTabComplete(String[] args);

    List<String> editItemBuyPriceOnTabComplete(String[] args);
}
