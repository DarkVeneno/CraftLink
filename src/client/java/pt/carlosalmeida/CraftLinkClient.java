package pt.carlosalmeida;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import pt.carlosalmeida.screen.Browser;
import pt.carlosalmeida.screen.Search;

import static net.minecraft.server.command.CommandManager.*;

public class CraftLinkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick(client.getTickDelta()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("browser")
                .executes(context -> {
                    minecraft.setScreen(new Browser(
                            Text.literal("CraftLink Browser")
                    ));
                    context.getSource().sendFeedback(() -> Text.literal("Browser opened"), false);
                    return 1;
                })));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("browserSearchBar")
                .executes(context -> {
                    minecraft.setScreen(new Search());
                    context.getSource().sendFeedback(() -> Text.literal("Search bar opened"), false);
                    return 1;
                })));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("browserGo")
                .then(argument("URL", StringArgumentType.string())
                        .executes(context -> {
                            final String value = StringArgumentType.getString(context, "URL");
                            Browser.setURL(value);
                            context.getSource().sendFeedback(() -> Text.literal("Browser set to open URL \"%s\". Press B to open it. You can also do this using the search bar.".formatted(value)), false);
                            return 1;
                        }))));
    }

    public static final MinecraftClient minecraft = MinecraftClient.getInstance();

    // B key to open a CraftLink Browser screen (default)
    public static final KeyBinding KEY_MAPPING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.craftlink.browser", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B, "category.craftlink.browser"
    ));
    // Backspace to open CraftLink Search (default)
    public static final KeyBinding KEY_MAPPING_SEARCH = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.craftlink.search", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_BACKSPACE, "category.craftlink.browser"
    ));

    public static float popUpTimerScale = 0f;
    public static float popUpTimerZoom = 0f;
    public static float browserDecisionTimer = 0f;

    public void onTick(float tickDelta) {
        // Check if our key was pressed
        if (KEY_MAPPING.wasPressed() && !(minecraft.currentScreen instanceof Browser)) {
            //Display the web browser UI.
            minecraft.setScreen(new Browser(
                    Text.literal("CraftLink Browser")
            ));
        }
        if (KEY_MAPPING_SEARCH.wasPressed() && !(minecraft.currentScreen instanceof Search)) {
            //Display the search UI.
            minecraft.setScreen(new Search());
        }
        tickPopUpTimerScale(tickDelta);
        tickPopUpTimerZoom(tickDelta);
        tickDecisionTimer(tickDelta);
    }


    void tickPopUpTimerScale(float tickDelta) {
        if(popUpTimerScale < 0f) {
            popUpTimerScale = 0f;
        }else if (popUpTimerScale > 0){
            popUpTimerScale -= 2 * tickDelta;
        }
    }
    void tickPopUpTimerZoom(float tickDelta) {
        if(popUpTimerZoom < 0f) {
            popUpTimerZoom = 0f;
        }else if (popUpTimerZoom > 0){
            popUpTimerZoom -= 2 * tickDelta;
        }
    }
    void tickDecisionTimer(float tickDelta) {
        if(browserDecisionTimer < 0f) {
            browserDecisionTimer = 0f;
        }else if (browserDecisionTimer > 0){
            browserDecisionTimer -= tickDelta;
        }
    }
}