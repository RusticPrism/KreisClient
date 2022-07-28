package de.rusticprism.kreisclient.keybindings;

import com.google.common.collect.Lists;
import de.rusticprism.kreisclient.mixin.keybinding.KeyBindingAccessor;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.option.KeyBinding;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class KeyBindingRegistryImpl {
    private static final List<KeyBinding> moddedKeyBindings = new ReferenceArrayList<>(); // ArrayList with identity based comparisons for contains/remove/indexOf etc., required for correctly handling duplicate keybinds

    private KeyBindingRegistryImpl() {
    }

    private static Map<String, Integer> getCategoryMap() {
        return KeyBindingAccessor.fabric_getCategoryMap();
    }

    private static boolean hasCategory(String categoryTranslationKey) {
        return getCategoryMap().containsKey(categoryTranslationKey);
    }

    public static boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = getCategoryMap();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
        int largestInt = largest.orElse(0);
        map.put(categoryTranslationKey, largestInt + 1);
        return true;
    }

    public static KeyBinding registerKeyBinding(KeyBinding binding) {
        for (KeyBinding existingKeyBindings : moddedKeyBindings) {
            if (existingKeyBindings == binding) {
                throw null;
            } else if (existingKeyBindings.getTranslationKey().equals(binding.getTranslationKey())) {
                throw new RuntimeException("Attempted to register two key bindings with equal ID: " + binding.getTranslationKey() + "!");
            }
        }

        if (!hasCategory(binding.getCategory())) {
            addCategory(binding.getCategory());
        }

        moddedKeyBindings.add(binding);
        return binding;
    }

    /**
     * Processes the keybindings array for our modded ones by first removing existing modded keybindings and readding them,
     * we can make sure that there are no duplicates this way.
     */
    public static KeyBinding[] process(KeyBinding[] keysAll) {
        List<KeyBinding> newKeysAll = Lists.newArrayList(keysAll);
        newKeysAll.removeAll(moddedKeyBindings);
        newKeysAll.addAll(moddedKeyBindings);
        return newKeysAll.toArray(new KeyBinding[0]);
    }
}
