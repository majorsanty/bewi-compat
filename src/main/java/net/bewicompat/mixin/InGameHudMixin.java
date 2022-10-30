package net.bewicompat.mixin;

import net.bewicompat.BewitchmentCompatUtils;
import net.dehydration.access.HudAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements HudAccess {

    @Shadow
    @Final
    @Mutable
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1), cancellable = true)
    private void renderStatusBarsMixin(MatrixStack matrices, @NotNull CallbackInfo info)
    {
        // for some reason hunger is renderer again with dehy?
        if(!BewitchmentCompatUtils.requiresSustenance(client.player))
            info.cancel();
    }
}