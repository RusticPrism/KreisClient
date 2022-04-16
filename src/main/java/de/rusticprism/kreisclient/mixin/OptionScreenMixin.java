package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.utils.CustomOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OptionsScreen.class)
public class OptionScreenMixin extends Screen {

    protected OptionScreenMixin() {
        super(new TranslatableText("options.title"));
    }

    @Redirect(method = "init",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/option/Option;createButton(Lnet/minecraft/client/option/GameOptions;III)Lnet/minecraft/client/gui/widget/ClickableWidget;"))
    public ClickableWidget onHigherFov(Option instance, GameOptions gameOptions, int i, int i1, int i2) {
        Option option = CustomOptions.FOV;
        int j = this.width / 2 - 155;
        int k = this.height / 6 - 12;
        return option.createButton(gameOptions, j, k, 150);
    }
}
