package de.rusticprism.kreisclient.mixin.borderlesswindow;

import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.FullscreenOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.Window;
import net.minecraft.text.TranslatableText;
import org.apache.http.util.Args;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.w3c.dom.Text;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(FullscreenOption.class)
public abstract class FullScreenOptionMixin {
    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/DoubleOption;<init>(Ljava/lang/String;DDFLjava/util/function/Function;Ljava/util/function/BiConsumer;Ljava/util/function/BiFunction;)V"), method = "<init>(Lnet/minecraft/client/util/Window;Lnet/minecraft/client/util/Monitor;)V")
    private static void modifyDoubleOption(org.spongepowered.asm.mixin.injection.invoke.arg.Args args) {
        // Add one extra option at the end for Borderless Windowed
        double max = args.<Double>get(2) + 1.0;
        args.set(2, max);

        // Modify the getter/setters to modify Borderless Windowed settings when the last option is set
        Function<GameOptions, Double> getter = args.get(4);
        BiConsumer<GameOptions, Double> setter = args.get(5);
        BiFunction<GameOptions, DoubleOption, Text> desc = args.get(6);

        args.set(4, (Function<GameOptions, Double>) (opts) -> getter.apply(opts));
        args.set(5, (BiConsumer<GameOptions, Double>) (opts, val) -> {
            if (val == max) {
                setter.accept(opts, -1.0);
            } else {
                setter.accept(opts, val);
            }
        });
        args.set(6, (BiFunction<GameOptions, DoubleOption, Text>) (opts, val) -> {
            if (val.get(opts) == max) {
                return (Text) new TranslatableText("text.borderlessmining.videomodename");
            }
            return desc.apply(opts, val);
        });
    }
}
