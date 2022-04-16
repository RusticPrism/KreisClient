package de.rusticprism.kreisclient.commands;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.PacketEvent;
import de.rusticprism.kreisclient.utils.TickUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerCommand implements KreisClientCommand {
    private static final List<String> ANTICHEAT_LIST = Arrays.asList("nocheatplus", "negativity", "warden", "horizon","illegalstack","coreprotect","exploitsx");
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 0) {
            MinecraftClient.getInstance().player.sendMessage(new LiteralText("Bitte benutze !server <pluins/tps>!"),false);
            return;
        }
        switch (args[0]) {
            case "plugins": {
                MinecraftClient.getInstance().player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0,"/"));
                break;
            }
            case "tps": {
                float tps = Math.round(TickUtil.INSTANCE.getTickRate());
                String TPS = "";
                if(tps > 17.00) {
                    TPS = "Tps: §a" + tps;
                }else if(tps > 10.00) {
                    TPS = "Tps: §e" + tps;
                }else TPS = "Tps: §4" + tps;
                MinecraftClient.getInstance().player.sendMessage(new LiteralText(TPS),false);
                break;
            }
            default: {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("Bitte benutze !server <pluins/tps>!"),false);
                break;
            }
        }
    }
    public static void onReadPacket(PacketEvent.Receive event) {
        try {
            if (event.packet instanceof CommandSuggestionsS2CPacket packet) {
                List<String> plugins = new ArrayList<>();
                Suggestions matches = packet.getSuggestions();

                if (matches == null) {
                   MinecraftClient.getInstance().player.sendMessage(new LiteralText("Error"),false);
                    return;
                }

                for (Suggestion yes : matches.getList()) {
                    String[] command = yes.getText().split(":");
                    if (command.length > 1) {
                        String pluginName = command[0].replace("/", "");

                        if (!plugins.contains(pluginName)) {
                            plugins.add(pluginName);
                        }
                    }
                }

                Collections.sort(plugins);
                for (int i = 0; i < plugins.size(); i++) {
                    plugins.set(i, formatName(plugins.get(i)));
                }

                if (!plugins.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    for(String str : plugins) {
                        String msg = "";
                        msg = str.replaceAll("\\(default\\)", Formatting.GRAY.toString());
                        msg = msg.replaceAll("\\(highlight\\)", Formatting.GREEN.toString());
                        msg = msg.replaceAll("\\(underline\\)", Formatting.UNDERLINE.toString());
                        builder.append(msg);
                        builder.append(", ");
                    }
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Plugins: " + builder),false);
                } else {
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Couldn't load Plugins"),false);
                }
            }

        } catch (Exception e) {
        }
    }
    private static String formatName(String name) {
        if (ANTICHEAT_LIST.contains(name)) {
            return String.format("%s%s(default)", Formatting.RED, name);
        }
        else if (name.contains("exploit") || name.contains("cheat") || name.contains("illegal")) {
            return String.format("%s%s(default)", Formatting.RED, name);
        }

        return String.format("(highlight)%s(default)", name);
    }
}
