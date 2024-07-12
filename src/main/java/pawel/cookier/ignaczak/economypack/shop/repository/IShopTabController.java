package pawel.cookier.ignaczak.economypack.shop.repository;

import java.util.List;

public interface IShopTabController {
    List<String> addCategoryOnTabComplete(String[] args);
    List<String> removeCategoryOnTabComplete(String[] args);
}
