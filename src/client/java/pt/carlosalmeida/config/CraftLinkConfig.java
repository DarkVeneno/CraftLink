package pt.carlosalmeida.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class CraftLinkConfig extends MidnightConfig {
    /* Wiki shortcut: https://www.midnightdust.eu/wiki/midnightlib/*/
    @Comment(category = "home_url") public static Comment comment_text_explain_home_url;
    @Entry(category = "home_url") public static String home_url = "";
    @Entry(category = "home_url") public static SearchEngineEnumChoice default_home_url = SearchEngineEnumChoice.GOOGLE;   // Example for an enum option
    @Entry(category = "scale_factor", name = "Scale Factor Slider", isSlider = true, min = 1f, max = 25f, precision = 2) public static float scale_factor = 1f;
    @Comment(category = "query_url") public static Comment comment_text_explain_query_url;
    @Comment(category = "query_url") public static Comment comment_text_explain_query_url_2;
    @Entry(category = "query_url") public static String query_url = "";
    @Entry(category = "query_url") public static SearchEngineEnumChoice default_search_engine = SearchEngineEnumChoice.GOOGLE;   // Example for an enum option
    public enum SearchEngineEnumChoice {                               // Enums allow the user to cycle through predefined options
        GOOGLE, BING, YAHOO, DUCKDUCKGO, ECOSIA, ASK
    }
}