package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.modapi.KreisClientMod;
import de.rusticprism.kreisclient.utils.config.FileConfiguration;
import de.rusticprism.kreisclient.utils.config.YamlConfiguration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

public class BlockCounter extends KreisClientMod {
    static MinecraftClient mc = KreisClient.MC;
    public static BlockCounter Instance;
    private static int blockamount;
   public FileConfiguration config;

    public BlockCounter() {
        super("BlockCounter");
        Instance = this;
        config = new YamlConfiguration("Blockcounter.txt");
    }

    @Override
    public void setEnabled(boolean enabled) {
        config.set("Enabled", enabled);
    }

    @Override
    public boolean isEnabled() {
        return config.getBoolean("Enabled");
    }

    public static String getamount() {
        blockamount = 0;
        if(mc.player == null) return "0";
        mc.player.getInventory().main.forEach(itemStack -> {
            if (Item.BLOCK_ITEMS.containsValue(itemStack.getItem())) {
                blockamount = blockamount + itemStack.getCount();
            }
        });
        return String.valueOf(blockamount);
    }

    @Override
    public void render(MatrixStack matrices,float Xpostion, float Yposition,int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices,"§8Blocks §1" + getamount(),config.getFloat("Xpos"), config.getFloat("Ypos"),color);
    }

    @Override
    public void init(MinecraftClient client) {
        KreisClient.LOGGER.info("BlockCounter init");
    }
}
