package pt.carlosalmeida.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class CraftLinkConfig extends MidnightConfig {
    @Comment(category = "home_url") public static Comment comment_text_explain_home_url;
    @Entry(category = "home_url") public static String home_url = "https://www.google.com";
    @Entry(category = "scale_factor", name = "Scale Factor Slider", isSlider = true, min = 1f, max = 25f, precision = 2) public static float scale_factor = 1f;
    @Comment(category = "query_url") public static Comment comment_text_explain_query_url;
    @Comment(category = "query_url") public static Comment comment_text_explain_query_url_2;
    @Entry(category = "query_url") public static String query_url = "https://www.google.com/search?q=";
}