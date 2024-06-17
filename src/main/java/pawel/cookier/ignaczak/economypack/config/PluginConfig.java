package pawel.cookier.ignaczak.economypack.config;

import pawel.cookier.ignaczak.economypack.gambling.models.GameType;

public final class PluginConfig {
    //commands
    public static final String GAMBLE_COMMAND = "gamble";
    public static final String SLOTS_COMMAND = "slots";
    public static final String BALANCE_COMMAND = "balance";
    public static final String EXCHANGE_COMMAND = "exchange";
    public static final String PAY_COMMAND = "pay";
    public static final String ADD_USER_MANUALLY_COMMAND = "new_money_user";
    public static final String TRANSLATION_COMMAND = "translation";

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
    public static final Long PRICE_PER_GOLD_INGOT = 20L;

    //gamble
    public static final int GAMBLE_CHANCE = 50;
    public static final Long MIN_GAMBLE_VALUE = 100L;
    public static final Long GAMBLE_MULTIPLIER = 1L;
    public static final GameType GAMBLE_GAME_TYPE = GameType.GAMBLE_GAME;

    //slots
    public static final Long MIN_SLOTS_VALUE = 100L;
    public static final Long SLOTS_MULTIPLIER = 8L;
    public static final GameType SLOTS_GAME_TYPE = GameType.SLOTS_GAME;
    public static final int SLOTS_MIN_NUMBER_VALUE = 1;
    public static final int SLOTS_MAX_NUMBER_VALUE = 5;
}
