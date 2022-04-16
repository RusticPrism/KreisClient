package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.mixin.NarratorOffMixin;
import de.rusticprism.kreisclient.mods.NarratorOffMod;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class NarratorOffCommand implements KreisClientCommand {
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("off")) {
                NarratorOffMod.instance.setEnabled(true);
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§8Toggled Narrator §1off§8!"),false);
            }else if(args[0].equalsIgnoreCase("on")) {
                NarratorOffMod.instance.setEnabled(false);
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§8Toggled Narrator §1on§8!"),false);
            }else MinecraftClient.getInstance().player.sendMessage(new LiteralText("§cBitte benutze " + Prefix.getPrefix() + "narrator <on/off>!"),false);
        }else MinecraftClient.getInstance().player.sendMessage(new LiteralText("§cBitte benutze " + Prefix.getPrefix() + "narrator <on/off>!"),false);
    }
}
