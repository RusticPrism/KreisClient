package de.rusticprism.kreisclient.keys;

import de.rusticprism.kreisclient.KreisClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;

public class Perspectivekey {
    private static boolean held = false;
    public static void call(KeyBinding binding) {
        if (KreisClient.MC.player != null) {
            KreisClient.INSTANCE.perspectiveEnabled = binding.isPressed();

            if (KreisClient.INSTANCE.perspectiveEnabled && !held) {
                held = true;
                KreisClient.INSTANCE.cameraPitch = KreisClient.MC.player.getPitch();
                KreisClient.INSTANCE.cameraYaw = KreisClient.MC.player.getYaw();
                KreisClient.MC.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            if (!KreisClient.INSTANCE.perspectiveEnabled && held) {
                held = false;
                KreisClient.MC.options.setPerspective(Perspective.FIRST_PERSON);
            }

            if (KreisClient.INSTANCE.perspectiveEnabled && KreisClient.MC.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
                KreisClient.INSTANCE.perspectiveEnabled = false;
            }
        }
    }
}
