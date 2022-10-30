package net.bewicompat.mixin;

import dev.mrsterner.besmirchment.common.registry.BSMTransformations;
import dev.mrsterner.besmirchment.common.transformation.LichAccessor;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.bewicompat.BewitchmentCompatUtils;
import net.dehydration.thirst.ThirstManager;
import net.environmentz.access.PlayerEnvAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.dehydration.access.ThirstManagerAccess;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ThirstManagerAccess, PlayerEnvAccess
{
    private ThirstManager thirstManager = new ThirstManager();
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci)
    {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        thirstManager.setThirst(BewitchmentCompatUtils.requiresSustenance(player));
        PlayerEnvAccess envAccess = ((PlayerEnvAccess) player);

        boolean affectedByCold = !BewitchmentAPI.isVampire(player, true) && !BewitchmentCompatUtils.isWerepyre(player) && !BewitchmentCompatUtils.isLich(player);
        envAccess.setColdEnvAffected(affectedByCold);
        if(!affectedByCold && 5 > envAccess.getPlayerTemperature())
            envAccess.setPlayerTemperature(5);

        boolean affectedByHot = !BewitchmentCompatUtils.isLich(player);
        ((PlayerEnvAccess) player).setColdEnvAffected(affectedByHot);

        if(!affectedByHot && envAccess.getPlayerTemperature() > 5)
            envAccess.setPlayerTemperature(5);

        if((BewitchmentCompatUtils.isWerepyre(player) && BSMTransformations.isWerepyre(player, false))
                || BewitchmentAPI.isWerewolf(player, false))
        {
            envAccess.setPlayerHeatProtectionAmount(-3);
        } else
            envAccess.setPlayerHeatProtectionAmount(0);

        // todo should be able to disable this, after i make a config?
        if(BewitchmentCompatUtils.isLich(player) && !player.world.isClient)
        {
            int souls = ((LichAccessor) player).getCachedSouls();
            HungerManager hunger = player.getHungerManager();
            // cannot run with 0 souls.
            if(souls >= 1)
            {
                // cannot regenerate with less than 3 souls
                hunger.setFoodLevel(souls >= 3 ? 20 : 17);
                // regeneration with saturation is only after 4
                hunger.setSaturationLevel(souls >= 4 ? 20 : 0);
            } else
                hunger.setFoodLevel(1);
        }

    }

}
