package pt.carlosalmeida.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pt.carlosalmeida.screen.Browser;
import pt.carlosalmeida.CraftLink;
import pt.carlosalmeida.CraftLinkClient;

@Mixin(ConfirmLinkScreen.class)
public class ConfirmLinkScreenMixin {
    @Inject(at = @At("HEAD"), method = "open", cancellable = true)
    private static void open(String url, Screen parent, boolean linkTrusted, CallbackInfo ci) {
        CraftLink.LOGGER.info("Open method called in ConfirmLinkScreen");
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.setScreen(new ConfirmLinkScreen(confirmed -> {
            if (confirmed) {
                //Previous code: Util.getOperatingSystem().open(url);
                Browser.setURL(url);
                CraftLinkClient.minecraft.setScreen(new Browser(Text.literal("Basic Browser"), url));
            }
            minecraftClient.setScreen(parent);
        }, url, linkTrusted));
        ci.cancel();
    }
}
