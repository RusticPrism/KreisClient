package de.rusticprism.kreisclient.mixin.keybinding;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface KeyCodeAccessor {
    @Accessor("boundKey")
    InputUtil.Key fabric_getBoundKey();
}
