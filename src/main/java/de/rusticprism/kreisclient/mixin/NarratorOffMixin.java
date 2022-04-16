package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.mods.NarratorOffMod;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class NarratorOffMixin {
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
        public void onNarratorKeyPressed(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == this.client.getWindow().getHandle()) {
            if (NarratorManager.INSTANCE.isActive()) {
                boolean bl = this.client.currentScreen == null || !(this.client.currentScreen.getFocused() instanceof TextFieldWidget) || !((TextFieldWidget)this.client.currentScreen.getFocused()).isActive();
                if (action != 0 && key == InputUtil.GLFW_KEY_B && Screen.hasControlDown() && bl && NarratorOffMod.instance.isEnabled()) {
                    if(MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.getInventory().selectedSlot = 6;
                        ci.cancel();
                    }else ci.cancel();
                }
            }

        }
    }
}
