package de.rusticprism.kreisclient.keybindings;

import de.rusticprism.kreisclient.mixin.keybinding.KeyCodeAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindingHelper {
        private KeyBindingHelper() {
        }

        /**
         * Registers the keybinding and add the keybinding category if required.
         *
         * @param keyBinding the keybinding
         * @return the keybinding itself
         */
        public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
            return KeyBindingRegistryImpl.registerKeyBinding(keyBinding);
        }

        /**
         * Returns the configured KeyCode bound to the KeyBinding from the player's settings.
         *
         * @param keyBinding the keybinding
         * @return configured KeyCode
         */
        public static InputUtil.Key getBoundKeyOf(KeyBinding keyBinding) {
            return ((KeyCodeAccessor) keyBinding).fabric_getBoundKey();
        }
    }
