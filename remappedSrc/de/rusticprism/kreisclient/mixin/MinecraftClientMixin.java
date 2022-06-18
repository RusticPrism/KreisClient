package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.render.SplashOverlay;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.resource.ResourceReloadLogger;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> {

    @Shadow @Final private ReloadableResourceManagerImpl resourceManager;

    @Shadow @Final private static CompletableFuture<Unit> COMPLETED_UNIT_FUTURE;

    @Shadow @Final private ResourcePackManager resourcePackManager;

    @Shadow protected abstract void handleResourceReloadException(Throwable throwable);

    @Shadow protected abstract void checkGameData();

    @Shadow @Final private ResourceReloadLogger resourceReloadLogger;

    public MinecraftClientMixin(String string) {
        super(string);
    }

    @Inject(method = "updateWindowTitle",at = @At("HEAD"),cancellable = true)
    public void setWindowTitle(CallbackInfo ci) {
        ci.cancel();
        MinecraftClient.getInstance().getWindow().setTitle("KreisClient " + KreisClient.version + "  Minecraft: 1.18.2");
        MinecraftClient.getInstance().options.getNarrator().setValue(NarratorMode.OFF);
    }
    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"),index = 0)
    private Overlay setOverlay(Overlay overlay) {
        List<ResourcePack> list = this.resourcePackManager.createResourcePacks();
        return new SplashOverlay(MinecraftClient.getInstance(), this.resourceManager.reload(Util.getMainWorkerExecutor(), this, COMPLETED_UNIT_FUTURE, list), (throwable) -> Util.ifPresentOrElse(throwable, this::handleResourceReloadException, () -> {
            if (SharedConstants.isDevelopment) {
                this.checkGameData();
            }

            this.resourceReloadLogger.finish();
        }), false);
    }
    @ModifyArg(method = "reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"))
    private Overlay setTexturepackOverlay(Overlay overlay) {
        List<ResourcePack> list = this.resourcePackManager.createResourcePacks();
        return new SplashOverlay(MinecraftClient.getInstance(), this.resourceManager.reload(Util.getMainWorkerExecutor(), this, COMPLETED_UNIT_FUTURE, list), (throwable) -> Util.ifPresentOrElse(throwable, this::handleResourceReloadException, () -> {
            if (SharedConstants.isDevelopment) {
                this.checkGameData();
            }

            this.resourceReloadLogger.finish();
        }), false);
    }
}
