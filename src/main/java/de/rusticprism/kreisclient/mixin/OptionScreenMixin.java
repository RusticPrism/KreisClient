package de.rusticprism.kreisclient.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OptionsScreen.class)
public class OptionScreenMixin extends Screen {

    protected OptionScreenMixin() {
        super(Text.translatable("options.title"));
    }

    @Redirect(method = "init",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/option/SimpleOption;createButton(Lnet/minecraft/client/option/GameOptions;III)Lnet/minecraft/client/gui/widget/ClickableWidget;"))
    public ClickableWidget onHigherFov(SimpleOption instance, GameOptions options, int x, int y, int width) {
        SimpleOption<Integer> option = options.getFov();
        int j = this.width / 2 - 155;
        int k = this.height / 6 - 12;
        return option.createButton(options, j, k, 150);
    }
}
