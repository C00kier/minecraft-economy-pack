package pawel.cookier.ignaczak.economypack.shop_manager.commands.repository;

import java.util.List;

public interface IShopTabController {
    List<String> getListOfSuggestionsForCommands(String commandName, String[] args);
}
