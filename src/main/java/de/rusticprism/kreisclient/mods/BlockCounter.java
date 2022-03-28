package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.modapi.KreisClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;

import java.awt.*;

public class BlockCounter extends KreisClientMod {
    static MinecraftClient mc = KreisClient.MC;
    public double positionx;
    public double positiony;
    private static int blockamount;

    public BlockCounter(double x,double y) {
        super("BlockCounter");
        this.positionx = x;
        this.positiony = y;
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
    public void render(MatrixStack matrices,double Xpostion, double Yposition) {
        MinecraftClient.getInstance().textRenderer.draw(matrices,new LiteralText("Test").asOrderedText(),100,100, 0xAAAE4B36);
        KreisClient.LOGGER.info("render");
    }

    @Override
    public void init(MinecraftClient client) {
        KreisClient.LOGGER.info("BlockCounter init");
    }
}
