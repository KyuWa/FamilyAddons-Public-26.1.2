package org.kyowa.familyaddons.mixin.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.kyowa.familyaddons.storage.StorageOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Reproduces ChatTriggers' "guiClosed" (fired for the previous screen at the start of
 * displayGuiScreen) and "guiOpened" (fired for the new screen, including null) triggers.
 */
@Mixin(Minecraft.class)
public class MinecraftScreenMixin {

    @Shadow
    public Screen screen;

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void familystorage$guiClosed(Screen newScreen, CallbackInfo ci) {
        if (this.screen != null) {
            StorageOverlay.onGuiClosed(this.screen);
        }
    }

    @Inject(method = "setScreen", at = @At("TAIL"))
    private void familystorage$guiOpened(Screen newScreen, CallbackInfo ci) {
        StorageOverlay.onGuiOpened(this.screen);
    }
}
