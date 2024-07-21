package pawel.cookier.ignaczak.economypack.config;

import org.bukkit.Material;
import pawel.cookier.ignaczak.economypack.gambling.models.GameType;

public final class PluginConfig {
    //commands
    public static final String GAMBLE_COMMAND = "gamble";
    public static final String SLOTS_COMMAND = "slots";
    public static final String EXCHANGE_COMMAND = "exchange";
    public static final String PAY_COMMAND = "pay";
    public static final String TRANSLATION_COMMAND = "translation";
    public static final String CALL_SHOP_COMMAND = "shop";
    public static final String ADD_SHOP_CATEGORY_COMMAND = "add_shop_category";
    public static final String REMOVE_SHOP_CATEGORY_COMMAND = "remove_shop_category";
    public static final String EDIT_SHOP_CATEGORY_NAME_COMMAND = "edit_shop_category_name";
    public static final String EDIT_SHOP_CATEGORY_ICON_COMMAND = "edit_shop_category_icon";
    public static final String ADD_SHOP_ITEM_COMMAND = "add_shop_item";
    public static final String ADD_ITEM_FROM_HAND_TO_CATEGORY_COMMAND = "add_item_from_hand";
    public static final String REMOVE_ITEM_COMMAND = "remove_item";
    public static final String EDIT_ITEM_SELL_PRICE_COMMAND = "edit_item_sell_price";
    public static final String EDIT_ITEM_BUY_PRICE_COMMAND = "edit_item_buy_price";


    //translation
    public static final String TRANSLATION_TAG_POLISH = "pl";
    public static final String TRANSLATION_TAG_ENGLISH = "en";
    public static final String TRANSLATION_TAG_SPANISH = "es";
    public static final String TRANSLATION_TAG_FRENCH = "fr";
    public static final String TRANSLATION_TAG_GERMAN = "de";
    public static final String TRANSLATION_POLISH = "polish";
    public static final String TRANSLATION_ENGLISH = "english";
    public static final String TRANSLATION_SPANISH = "spanish";
    public static final String TRANSLATION_FRENCH = "french";
    public static final String TRANSLATION_GERMAN = "german";
    public static final String CURRENT_PLUGIN_LANGUAGE_TAG = "pl";

    //money manager
    public static final Double PRICE_PER_GOLD_INGOT = 20d;

    //shop
    public final static int SHOP_INVENTORY_SIZE = 45;
    public final static int SHOP_INVENTORY_FIELDS_TO_FILL_UP = 36; //9 slots for navbar
    public final static int SHOP_NAVBAR_BALANCE_PLACE = 36;
    public final static int SHOP_NAVBAR_PREVIOUS_PAGE_BUTTON_PLACE = 39;
    public final static int SHOP_NAVBAR_BUTTON_SEPARATOR_PLACE = 40;
    public final static int SHOP_NAVBAR_NEXT_PAGE_BUTTON_PLACE = 41;
    public final static int SHOP_NAVBAR_RETURN_PLACE = 44;
    public final static int[] EMPTY_NAVBAR_FIELDS = {37,38,42,43};
    public final static Material SHOP_NAVBAR_NEXT_BUTTON_MATERIAL = Material.SPRUCE_BUTTON;
    public final static Material SHOP_NAVBAR_SEPARATOR_MATERIAL = Material.SMOOTH_STONE;
    public final static Material SHOP_NAVBAR_PREVIOUS_BUTTON_MATERIAL = Material.OAK_BUTTON;



    //gamble
    public static final int GAMBLE_CHANCE = 50;
    public static final Double MIN_GAMBLE_VALUE = 100d;
    public static final Double GAMBLE_MULTIPLIER = 1d;
    public static final GameType GAMBLE_GAME_TYPE = GameType.GAMBLE_GAME;

    //slots
    public static final Double MIN_SLOTS_VALUE = 100d;
    public static final Double SLOTS_MULTIPLIER = 8d;
    public static final GameType SLOTS_GAME_TYPE = GameType.SLOTS_GAME;
    public static final int SLOTS_MIN_NUMBER_VALUE = 1;
    public static final int SLOTS_MAX_NUMBER_VALUE = 5;


}
