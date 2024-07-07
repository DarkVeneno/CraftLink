package pt.carlosalmeida.screen;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import pt.carlosalmeida.CraftLink;
import pt.carlosalmeida.CraftLinkClient;
import pt.carlosalmeida.config.CraftLinkConfig;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Search extends BaseOwoScreen<FlowLayout> {

    private final Screen parent;
    private static final String suggestionText = "Enter search or use '>' followed by URL";

    public static TextBoxComponent textBoxComponent = (TextBoxComponent) Components.textBox(Sizing.fill(60)).tooltip(Text.of(suggestionText));
    public static String lastSearch = Browser.chooseHomeURL();
    private boolean justOpened = true;

    public Search() {
        this.parent = null;
    }

    public Search(Screen parent) {
        this.parent = parent;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        boolean isInBrowser = (parent instanceof Browser);
        rootComponent.child(
                Containers.verticalFlow(Sizing.content() /**/, Sizing.content())
                        .child(
                                textBoxComponent
                        )
                        .padding(Insets.of(10)) //
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
        )
                .verticalAlignment(VerticalAlignment.CENTER)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .surface(isInBrowser ? Surface.blur(2f, 20f).and(Surface.VANILLA_TRANSLUCENT) : Surface.BLANK);
        textBoxComponent.setMaxLength(2083); //Max URL length
    }

    @Override
    public boolean shouldPause() {
        if(parent != null) {
            return parent.shouldPause();
        }
        return false;
    }

    @Override
    public void close() {
        CraftLinkClient.minecraft.setScreen(parent);
    }

    public static String encodePureURL(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static String encodeURL(String search) {
        return chooseQuery() + encodePureURL(search);
    }

    private static String chooseQuery() {
        if(CraftLinkConfig.query_url != null && !CraftLinkConfig.query_url.isEmpty())
            return CraftLinkConfig.query_url;
        return switch (CraftLinkConfig.default_search_engine) {
            case GOOGLE -> "https://www.google.com/search?q=";
            case BING -> "https://www.bing.com/search?q=";
            case YAHOO -> "https://search.yahoo.com/search?p=";
            case DUCKDUCKGO -> "https://duckduckgo.com/?q=";
            case ECOSIA -> "https://www.ecosia.org/search?q=";
            case ASK -> "https://www.ask.com/web?q=";
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        justOpened = false;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode != GLFW.GLFW_KEY_TAB && (!textBoxComponent.isFocused() || justOpened)) {
            keyPressed(GLFW.GLFW_KEY_TAB, 48, 0);
            justOpened = false;
        }
        String query = textBoxComponent.getText();
        if(keyCode == GLFW.GLFW_KEY_ENTER && !query.isEmpty()) {
            if(query.charAt(0) == '>') {
                String url = query.substring(1);
                lastSearch = url;
                CraftLinkClient.minecraft.setScreen(new Browser(
                        Text.literal("CraftLink Browser"),
                        url
                ));
                Browser.setURL(url);
            }else{
                String url = query;
                lastSearch = url;
                url = encodeURL(url);
                CraftLinkClient.minecraft.setScreen(new Browser(
                        Text.literal("CraftLink Browser"),
                        url
                ));
                Browser.setURL(url);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
