package net.bewicompat.mixin;

import net.bewicompat.BewitchmentCompatUtils;
import net.dehydration.thirst.ThirstHudRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// In case Thirst mod isn't in
@Pseudo
@Mixin(ThirstHudRender.class)
public class ThirstHudRenderMixin
{
    @Inject(method = "renderThirstHud", at = @At(value = "HEAD"), cancellable = true)
    private static void renderHud(MatrixStack matrices, MinecraftClient client, PlayerEntity playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha, float otherFlashAlpha, CallbackInfo ci)
    {
        // should prevent bubbles being slightly higher?
        if(!BewitchmentCompatUtils.requiresSustenance(playerEntity))
            ci.cancel();
    }

}
