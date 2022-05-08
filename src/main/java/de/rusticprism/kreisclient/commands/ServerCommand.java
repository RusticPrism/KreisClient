package de.rusticprism.kreisclient.commands;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.PacketEvent;
import de.rusticprism.kreisclient.utils.Prefix;
import de.rusticprism.kreisclient.utils.TickUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class ServerCommand extends KreisClientCommand {
    private static final List<String> ANTICHEAT_LIST = Arrays.asList("nocheatplus", "negativity", "warden", "horizon", "illegalstack", "coreprotect", "exploitsx");
    private static boolean valid = false;

    @Override
    public void performCommand(String command, String[] args) {
        if (args.length == 0) {
            warning("Bitte benutze "+ Prefix.getCommandPrefix() +"server <pluins/tps/info>!");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "plugins": {
                valid = true;
                MinecraftClient.getInstance().player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, "/"));
                break;
            }
            case "tps": {
                double tps = Math.round(TickUtil.INSTANCE.getTickRate() * 100.0) / 100.0;
                String TPS;
                if (tps > 17.00) {
                    TPS = "Die Server TPS ist: §a" + tps;
                } else if (tps > 10.00) {
                    TPS = "Die Server TPS ist: §e" + tps;
                } else TPS = "Die Server TPS ist: §4" + tps;
               info(TPS);
                break;
            }
            case "info" : {
                basicInfo();
                break;
            }
            default: {
                warning("Bitte benutze "+ Prefix.getCommandPrefix() +"server <pluins/tps/info>!");
                break;
            }
        }
    }

    public static void onReadPacket(PacketEvent.Receive event) {
            try {
                if (event.packet instanceof CommandSuggestionsS2CPacket packet && valid) {
                    List<String> plugins = new ArrayList<>();
                    Suggestions matches = packet.getSuggestions();

                    if (matches == null) {
                        warning("Error");
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
                    plugins.replaceAll(ServerCommand::formatName);

                    if (!plugins.isEmpty()) {
                        StringBuilder builder = new StringBuilder();
                        for (String str : plugins) {
                            String msg = "";
                            msg = str.replaceAll("\\(default\\)", Formatting.GRAY.toString());
                            msg = msg.replaceAll("\\(highlight\\)", Formatting.GREEN.toString());
                            msg = msg.replaceAll("\\(underline\\)", Formatting.UNDERLINE.toString());
                            builder.append(msg);
                            builder.append(", ");
                        }
                        info("Die Plugins des Servers sind: " + builder);
                        valid = false;
                    }else warning("Couldn't load plugins!");
                }

            } catch (Exception e) {
                warning("Couldn't load plugins because of:" + e.getMessage() + " !");
            }
    }

    private static String formatName(String name) {
        if (ANTICHEAT_LIST.contains(name)) {
            return String.format("%s%s(default)", Formatting.RED, name);
        } else if (name.contains("exploit") || name.contains("cheat") || name.contains("illegal")) {
            return String.format("%s%s(default)", Formatting.RED, name);
        }

        return String.format("(highlight)%s(default)", name);
    }

    private void basicInfo() {
            if (MinecraftClient.getInstance().isIntegratedServerRunning()) {
                IntegratedServer server = MinecraftClient.getInstance().getServer();

                info("Singleplayer");
                if (server != null) info("Version: " + MinecraftClient.getInstance().getGameVersion());

                return;
            }

            ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
            

            if (server == null) {
                info("Couldn't obtain any server information.");
                return;
            }

            String ipv4 = "";
            try {
                ipv4 = InetAddress.getByName(server.address).getHostAddress();
            } catch (UnknownHostException ignored) {
            }

            BaseText ipText;

            if (ipv4.isEmpty()) {
                ipText = new LiteralText(Formatting.DARK_GRAY + server.address);
                ipText.setStyle(ipText.getStyle()
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.COPY_TO_CLIPBOARD,
                                server.address
                        ))
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new LiteralText("Copy to clipboard")
                        ))
                );
            } else {
                ipText = new LiteralText(Formatting.DARK_GRAY + server.address);
                ipText.setStyle(ipText.getStyle()
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.COPY_TO_CLIPBOARD,
                                server.address
                        ))
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new LiteralText("Copy to clipboard")
                        ))
                );
                BaseText ipv4Text = new LiteralText(String.format("%s (%s)", Formatting.GRAY, ipv4));
                ipv4Text.setStyle(ipText.getStyle()
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.COPY_TO_CLIPBOARD,
                                ipv4
                        ))
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new LiteralText("Copy to clipboard")
                        ))
                );
                ipText.append(ipv4Text);
            }

            info("IP: " + ipText.asString() + " (" + ipv4 + ")");

            info("Port: " + ServerAddress.parse(server.address).getPort());

            info("Type: " + MinecraftClient.getInstance().player.getServerBrand());

            if (server.label == null) {
                info("Motd: unknown");
            } else info("Motd: " + server.label.getString());

            info("Version: " + server.version.getString());

            info("Protocol version: " + server.protocolVersion);

            info("Difficulty: " + MinecraftClient.getInstance().world.getDifficulty().getTranslatableName().getString());

            info("Day: " + MinecraftClient.getInstance().world.getTimeOfDay() / 24000L);

            info("Permission level: " + formatPerms());
        }
    public String formatPerms() {
        int p = 5;
        while (!MinecraftClient.getInstance().player.hasPermissionLevel(p) && p > 0) p--;

        return switch (p) {
            case 0 -> "0 (No Perms)";
            case 1 -> "1 (No Perms)";
            case 2 -> "2 (Player Command Access)";
            case 3 -> "3 (Server Command Access)";
            case 4 -> "4 (Operator)";
            default -> p + " (Unknown)";
        };
    }
}
