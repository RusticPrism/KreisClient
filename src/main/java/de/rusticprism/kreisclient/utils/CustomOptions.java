package de.rusticprism.kreisclient.utils;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public abstract class CustomOptions {
    @Mutable
    @Shadow @Final private SimpleOption<Integer> fov;


    @Shadow
    public static Text getGenericValueText(Text prefix, int value) {
        return null;
    }
    @Shadow
    public static Text getGenericValueText(Text prefix,Text text) {
        return null;
    }

    @Inject(method = "<init>",at = @At("RETURN"))
    public void init(MinecraftClient client, File optionsFile, CallbackInfo ci) {
        this.fov = new SimpleOption("options.fov", SimpleOption.emptyTooltip(), (optionText, value) -> {
            Text var10000 = null;
            if(value instanceof Integer) {
                if((int) value == 1) {
                    var10000 = getGenericValueText(optionText, Text.translatable("JayJay720"));
                }else if((int) value == 179) {
                    var10000 = getGenericValueText(optionText, Text.translatable("180be"));
                }else var10000 = getGenericValueText(optionText, (Integer) value);
            }

            return var10000;
        }, new SimpleOption.ValidatingIntSliderCallbacks(30, 110), Codec.DOUBLE.xmap((value) -> {
            return (int)(value * 40.0 + 70.0);
        }, (value) -> {
            return ((double)value - 70.0) / 40.0;
        }), 70, (value) -> {
            MinecraftClient.getInstance().worldRenderer.scheduleTerrainUpdate();
        });
   }
}
