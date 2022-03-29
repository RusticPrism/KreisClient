package de.rusticprism.kreisclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CustomOptions {
    public static final DoubleOption FOV = new DoubleOption("kreisclient.options.fov", 1, 179, 1.0f, gameOptions -> gameOptions.fov, (gameOptions, fov) -> {
        gameOptions.fov = fov;
        MinecraftClient.getInstance().worldRenderer.scheduleTerrainUpdate();
    }, (gameOptions, option) -> {
        double d = option.get(gameOptions);
        if (d == 70.0) {
            return getGenericLabel(new TranslatableText("options.fov.min"));
        }
        if (d == 110) {
            return getGenericLabel(new TranslatableText("options.fov.max"));
        }
        if(d == 179) {
            return getGenericLabel(new LiteralText("180be"));
        }
        if(d == 1) {
            return getGenericLabel(new LiteralText("JayJay"));
        }
        return getGenericLabel((int)d);
    });
    private static Text key = new TranslatableText("kreisclient.options.fov");

    public CustomOptions(String key) {
        this.key = new TranslatableText(key);
    }

    protected static Text getGenericLabel(Text value) {
        return new TranslatableText("options.generic_value", getDisplayPrefix(), value);
    }

    protected static Text getGenericLabel(int value) {
        return getGenericLabel(new LiteralText(Integer.toString(value)));
    }
    protected static Text getDisplayPrefix() {
        return key;
    }
}
