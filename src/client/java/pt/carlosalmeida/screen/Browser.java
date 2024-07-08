package pt.carlosalmeida.screen;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import eu.midnightdust.lib.config.MidnightConfig;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.render.*;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import pt.carlosalmeida.CraftLinkClient;
import pt.carlosalmeida.config.CraftLinkConfig;

public class Browser extends BaseOwoScreen<FlowLayout> {
    public static final int BROWSER_DRAW_OFFSET = 20;
    public static final String BROWSER_DEFAULT_HOME_URL = /*"https://darkveneno.github.io/CraftLink"*/ /*"https://www.google.com"*/ chooseHomeURL();
    public static final double BROWSER_DEFAULT_ZOOM_LEVEL = 0;
    public static final float BROWSER_DEFAULT_SCALE_FACTOR = CraftLinkConfig.scale_factor;

    public static float scaleFactor = BROWSER_DEFAULT_SCALE_FACTOR; //SCALES RESOLUTION!
    private static boolean browserRender = true;
    //private static boolean errorDecision = false;
    private boolean controlKeyPressed = false;
    private boolean shiftKeyPressed = false;
    public static String openURL = BROWSER_DEFAULT_HOME_URL;
    private double previousBrowserZoomLevel = BROWSER_DEFAULT_ZOOM_LEVEL;

    private static MCEFBrowser browser;
    private final MinecraftClient minecraft = MinecraftClient.getInstance();

    /*static ButtonComponent goButton = Components.button(Text.literal("Search"), buttonComponent -> {
        searchLastSearch();
    }).active(false);*/

    static FlowLayout helpPanel = (FlowLayout) Containers.verticalFlow(Sizing.content(), Sizing.content())
            .child(Components.label(Text.literal("You're holding CTRL + SHIFT. This hides the browser and shows this window.")).color(Color.ofRgb(0xffff00)).margins(Insets.both(0, 5)))
            .child(Components.label(Text.literal("If the page's URL hasn't loaded, press CTRL + SHIFT + S to search for it instead.")).margins(Insets.both(0, 5)))
            .child(Components.label(Text.literal("CTRL + S: Configuration Screen | CTRL + H: Home | CTRL + R: Refresh")).margins(Insets.both(0, 5)))
            .child(Components.label(Text.literal("CTRL + ↑: Upscale browser | CTRL + ↓: Downscale browser | CTRL + Space: Search")).margins(Insets.both(0, 5)))
            .child(Components.label(Text.literal("CTRL + Scroll Up: Zoom in | CTRL + Scroll Down: Zoom out")).margins(Insets.both(0, 5)))
            .child(Components.label(Text.literal("Up/downscaling changes the browser's resolution.")).margins(Insets.both(0, 5)))
            .child(Components.label(Text.literal("Zooming keeps the resolution and makes the elements bigger or smaller.")).margins(Insets.both(0, 5)))
            /*.child(
                    goButton.margins(Insets.of(5))
            )*/
            .padding(Insets.of(10)) //
            .surface(Surface.DARK_PANEL)
            .verticalAlignment(VerticalAlignment.CENTER)
            .horizontalAlignment(HorizontalAlignment.CENTER);

    SystemToast systemToastScale = new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION, Text.literal("150%"), Text.literal("Scale factor"));
    SystemToast systemToastZoom = new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION, Text.literal("---"), Text.literal("Zoom level"));

    private String util_scaleText() { return  (scaleFactor * 100) + "%" + (scaleFactor == BROWSER_DEFAULT_SCALE_FACTOR ? " (Default)" : ""); }
    private String util_zoomText() { return  browser.getZoomLevel() + (browser.getZoomLevel() == BROWSER_DEFAULT_ZOOM_LEVEL ? " (Default)" : ""); }

    private void refreshScaleLabel() { systemToastScale.setContent(Text.literal(util_scaleText()), Text.literal("Scale factor")); }
    private void refreshZoomLabel() { systemToastZoom.setContent(Text.literal(util_zoomText()), Text.literal("Zoom level")); }

    public Browser(Text title) {
        super(title);
    }

    public Browser(Text title, String moddedOpenURL) {
        super(title);
        openURL = moddedOpenURL;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        subBuild(rootComponent);
    }

    public static String chooseHomeURL() {
        if(CraftLinkConfig.home_url != null && !CraftLinkConfig.home_url.isEmpty())
            return CraftLinkConfig.home_url;
        return switch (CraftLinkConfig.default_home_url) {
            case GOOGLE -> "https://www.google.com";
            case BING -> "https://www.bing.com";
            case YAHOO -> "https://www.yahoo.com";
            case DUCKDUCKGO -> "https://www.duckduckgo.com";
            case ECOSIA -> "https://www.ecosia.org";
            case ASK -> "https://www.ask.com";
        };
    }

    public static void searchLastSearch() {
        setURL(Search.encodeURL(Search.lastSearch));
        CraftLinkClient.minecraft.setScreen(new Browser(Text.literal("CraftLink Browser")));
    }

    /*public static boolean URLExists(String targetUrl) {
        targetUrl = fixURL(targetUrl);
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) new
                    URL(targetUrl).openConnection();
            urlConnection.setRequestMethod("HEAD");
            urlConnection.setConnectTimeout(300000);
            urlConnection.setReadTimeout(300000);
            int code = urlConnection.getResponseCode();
            CraftLink.LOGGER.info(String.valueOf(code));
            return (code >= 200 && code < 300);
        } catch (Exception e) {
            System.out.println("Exception => " + e.getMessage());
            return false;
        }
    }


    private static String fixURL(String url) {
        if (!url.toLowerCase().matches("^\\w+://.*")) {
            url = "http://" + url;
        }
        return url;
    }*/

    void subBuild(FlowLayout rootComponent) {
        rootComponent.child(
                        helpPanel
                )
                .verticalAlignment(VerticalAlignment.CENTER)
                .horizontalAlignment(HorizontalAlignment.CENTER);
    }

    public static void setURL(String newURL) {
        if(browser != null) {
            browser.loadURL(newURL); //Actually load the new URL
            //errorDecision = false;
            //CraftLinkClient.browserDecisionTimer = 3f;
            setBrowserActive(true);
        }
    }

    @Override
    protected void init() {
        super.init();
        if (browser == null) {
            boolean transparent = false;
            browser = MCEF.createBrowser(openURL, transparent);
            resizeBrowser();
            setBrowserActive(true);
            setURL(openURL);
            browser.setZoomLevel(BROWSER_DEFAULT_ZOOM_LEVEL);
        }
    }

    private static void setBrowserActive(boolean activity) {
        browserRender = activity;
        //goButton.active(!activity);
        if(activity) {
            helpPanel.sizing(Sizing.fixed(20), Sizing.fixed(20));
        }else{
            helpPanel.sizing(Sizing.content(), Sizing.content());
        }
    }

    private int mouseX(double x) {
        return (int) ((x - BROWSER_DRAW_OFFSET) * minecraft.getWindow().getScaleFactor() / scaleFactor);
    }

    private int mouseY(double y) {
        return (int) ((y - BROWSER_DRAW_OFFSET) * minecraft.getWindow().getScaleFactor() / scaleFactor);
    }

    private int scaleX(double x) {
        return (int) ((x - BROWSER_DRAW_OFFSET * 2) * minecraft.getWindow().getScaleFactor() / scaleFactor);
    }

    private int scaleY(double y) {
        return (int) ((y - BROWSER_DRAW_OFFSET * 2) * minecraft.getWindow().getScaleFactor() / scaleFactor);
    }

    private void resizeBrowser() {
        if (width > 100 && height > 100) {
            browser.resize(scaleX(width), scaleY(height));
        }
    }

    @Override
    public void resize(MinecraftClient minecraft, int i, int j) {
        super.resize(minecraft, i, j);
        resizeBrowser();
    }

    @Override
    public void close() {
        //openURL = browser.getURL();
        super.close();
    }

    /*public int rationalBranchedOpacity(float time) { //Time ranges from 0 to 10. Function ranges from 0 to 255.
        //It's called rational cuz it's a rational function (duh) between x = 9 and x = 0.
        //double branchMax = 4.6; //Value of ((1/(0.25*x-2.5))+5) when x = 0. Maximum value.
        double inverseBranchMax = 0.21739130434; //= 1 / branchMax = 1 / 4.6
        if(time >= 9f) {
            return 255;
        }else{
            double x = 10 - time;
            return (int) (((1/(0.25*x-2.5))+5) * inverseBranchMax * 255);
        }
    }*/

    @Override
    public void render(DrawContext guiGraphics, int i, int j, float f) {
        /*if(!errorDecision && CraftLinkClient.browserDecisionTimer == 0f) { //If a decision hasn't been made, and it's time for the browser to decide the validity of the URL, then do this.
            setBrowserActive(URLExists(browser.getURL()));
            errorDecision = true;
        }*/
        setBrowserActive(!(controlKeyPressed && shiftKeyPressed));
        super.render(guiGraphics, i, j, f);
        if(browserRender) {
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
            RenderSystem.setShaderTexture(0, browser.getRenderer().getTextureID());
            Tessellator t = Tessellator.getInstance();
            BufferBuilder buffer = t.getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            buffer.vertex(BROWSER_DRAW_OFFSET, height - BROWSER_DRAW_OFFSET, 0).texture(0.0f, 1.0f).color(255, 255, 255, 255).next();
            buffer.vertex(width - BROWSER_DRAW_OFFSET, height - BROWSER_DRAW_OFFSET, 0).texture(1.0f, 1.0f).color(255, 255, 255, 255).next();
            buffer.vertex(width - BROWSER_DRAW_OFFSET, BROWSER_DRAW_OFFSET, 0).texture(1.0f, 0.0f).color(255, 255, 255, 255).next();
            buffer.vertex(BROWSER_DRAW_OFFSET, BROWSER_DRAW_OFFSET, 0).texture(0.0f, 0.0f).color(255, 255, 255, 255).next();
            t.draw();
            RenderSystem.setShaderTexture(0, 0);
            RenderSystem.enableDepthTest();
        }
        if(CraftLinkClient.popUpTimerScale > 0) systemToastScale.draw(guiGraphics, new ToastManager(CraftLinkClient.minecraft), 400L);
        if(CraftLinkClient.popUpTimerZoom > 0) systemToastZoom.draw(guiGraphics, new ToastManager(CraftLinkClient.minecraft), 400L);
        if(controlKeyPressed && CraftLinkConfig.help_text_overlay) {
            String helpString = "CTRL + SHIFT: Help++ | CTRL + Space: Search | CTRL + R: Refresh | CTRL + H: Home | CTRL + Scroll: Zoom";
            MultilineText multilineText = MultilineText.create(textRenderer, Text.literal(helpString), width - 20);
            multilineText.drawCenterWithShadow(guiGraphics, width / 2, height - 30, 12, 0x00FF00);
        }
        if(browser.isLoading() && CraftLinkConfig.loading_text_overlay) {
            String helpString = "Loading...";
            MultilineText multilineText = MultilineText.create(textRenderer, Text.literal(helpString), width - 20);
            multilineText.drawCenterWithShadow(guiGraphics, 25, 5, 12, 0x32a852);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        browser.sendMousePress(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        browser.sendMouseRelease(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        browser.sendMouseMove(mouseX(mouseX), mouseY(mouseY));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(controlKeyPressed && !shiftKeyPressed) {
            if (delta > 0) {
                browser.setZoomLevel(browser.getZoomLevel() + 1);
            } else {
                browser.setZoomLevel(browser.getZoomLevel() - 1);
            }
        }
        checkZoomChangeAndHandle();
        browser.sendMouseWheel(mouseX(mouseX), mouseY(mouseY), delta, 0);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT_CONTROL:
                controlKeyPressed = true;
                return super.keyPressed(keyCode, scanCode, modifiers);
            case GLFW.GLFW_KEY_LEFT_SHIFT:
                shiftKeyPressed = true;
                return super.keyPressed(keyCode, scanCode, modifiers);
            case GLFW.GLFW_KEY_H:
                if(controlKeyPressed) {
                    setURL(BROWSER_DEFAULT_HOME_URL);
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
                break;
            case GLFW.GLFW_KEY_R:
                if(controlKeyPressed) {
                    browser.reload();
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
                break;
            case GLFW.GLFW_KEY_UP:
                if(controlKeyPressed) {
                    scaleFactor += 0.25f;
                    refreshScaleLabel();
                    CraftLinkClient.popUpTimerScale = 10f;
                    CraftLinkClient.popUpTimerZoom = 0f;
                    resizeBrowser();
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
                break;
            case GLFW.GLFW_KEY_DOWN:
                if(controlKeyPressed) {
                    scaleFactor -= 0.25f;
                    if(scaleFactor < 1f)
                        scaleFactor = 1f;
                    refreshScaleLabel();
                    CraftLinkClient.popUpTimerScale = 10f;
                    CraftLinkClient.popUpTimerZoom = 0f;
                    resizeBrowser();
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
                break;
            case GLFW.GLFW_KEY_SPACE:
                if(controlKeyPressed) {
                    CraftLinkClient.minecraft.setScreen(new Search(this));
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
                break;
            case GLFW.GLFW_KEY_S:
                if(controlKeyPressed) {
                    if(shiftKeyPressed) {
                        searchLastSearch();
                        return super.keyPressed(keyCode, scanCode, modifiers);
                    }
                    CraftLinkClient.minecraft.setScreen(MidnightConfig.getScreen(this, "craftlink"));
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
                break;
        }
        browser.sendKeyPress(keyCode, scanCode, modifiers);
        checkZoomChangeAndHandle();
        browser.setFocus(true);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void checkZoomChangeAndHandle() {
        double bZoom = browser.getZoomLevel();
        if(bZoom != previousBrowserZoomLevel) {
            previousBrowserZoomLevel = bZoom;
            refreshZoomLabel();
            CraftLinkClient.popUpTimerZoom = 10f;
            CraftLinkClient.popUpTimerScale = 0f;
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL) {
            controlKeyPressed = false;
        }
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) {
            shiftKeyPressed = false;
        }
        browser.sendKeyRelease(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (codePoint == (char) 0) return false;
        browser.sendKeyTyped(codePoint, modifiers);
        browser.setFocus(true);
        return super.charTyped(codePoint, modifiers);
    }
}
