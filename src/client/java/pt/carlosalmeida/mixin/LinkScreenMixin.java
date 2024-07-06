package pt.carlosalmeida.mixin;

import com.google.common.collect.Sets;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pt.carlosalmeida.screen.Browser;
import pt.carlosalmeida.CraftLink;

@Mixin(Screen.class)
public class LinkScreenMixin {

    @Shadow
    protected MinecraftClient client;

    @Shadow
    private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");

    @Shadow
    private URI clickedLink;

    @Inject(at = @At("HEAD"), method = "handleTextClick", cancellable = true)
    public void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style == null) {
            cir.setReturnValue(false);
        }
        ClickEvent clickEvent = style.getClickEvent();
        if (Screen.hasShiftDown()) {
            if (style.getInsertion() != null) {
                this.insertText(style.getInsertion(), false);
            }
        } else if (clickEvent != null) {
            block24: {
                if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.client.options.getChatLinks().getValue().booleanValue()) {
                        cir.setReturnValue(false);
                    }
                    try {
                        URI uRI = new URI(clickEvent.getValue());
                        String string = uRI.getScheme();
                        if (string == null) {
                            throw new URISyntaxException(clickEvent.getValue(), "Missing protocol");
                        }
                        if (!ALLOWED_PROTOCOLS.contains(string.toLowerCase(Locale.ROOT))) {
                            throw new URISyntaxException(clickEvent.getValue(), "Unsupported protocol: " + string.toLowerCase(Locale.ROOT));
                        }
                        if (this.client.options.getChatLinksPrompt().getValue().booleanValue()) {
                            this.clickedLink = uRI;
                            //Previous code: this.client.setScreen(new ConfirmLinkScreen(this::confirmLink, clickEvent.getValue(), false));
                            String url = uRI.toURL().toString();
                            Browser.setURL(url);
                            this.client.setScreen(new Browser(Text.literal("Basic Browser"), url));
                            break block24;
                        }
                        this.openLink(uRI);
                    } catch (URISyntaxException uRISyntaxException) {
                        CraftLink.LOGGER.error("Can't open url for {}", (Object)clickEvent, (Object)uRISyntaxException);
                    } catch (MalformedURLException e) {
                        CraftLink.LOGGER.error("Opening in system browser. URL conversion failed.", e);
                    }
                } else if (clickEvent.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI uRI = new File(clickEvent.getValue()).toURI();
                    this.openLink(uRI);
                } else if (clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.insertText(SharedConstants.stripInvalidChars((String)clickEvent.getValue()), true);
                } else if (clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    String string2 = SharedConstants.stripInvalidChars((String)clickEvent.getValue());
                    if (string2.startsWith("/")) {
                        if (!this.client.player.networkHandler.sendCommand(string2.substring(1))) {
                            CraftLink.LOGGER.error("Not allowed to run command with signed argument from click event: '{}'", (Object)string2);
                        }
                    } else {
                        CraftLink.LOGGER.error("Failed to run command without '/' prefix from click event: '{}'", (Object)string2);
                    }
                } else if (clickEvent.getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD) {
                    this.client.keyboard.setClipboard(clickEvent.getValue());
                } else {
                    CraftLink.LOGGER.error("Don't know how to handle {}", (Object)clickEvent);
                }
            }
            cir.setReturnValue(true);
        }
        cir.setReturnValue(false);
    }

    @Shadow
    private void openLink(URI uRI) {
    }

    @Shadow
    protected void insertText(String insertion, boolean b) {
    }
}