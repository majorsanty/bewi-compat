package net.bewicompat;

import dev.mrsterner.besmirchment.common.registry.BSMTransformations;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BewitchmentCompatUtils implements ModInitializer {
    public static final String MODID = "bewitchmentcompat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize()
    {
        // nothin to intilaize tbh
    }

    public static boolean requiresSustenance(PlayerEntity player)
    {
        // todo Lich food config someday
        return !isWerepyre(player) && !BewitchmentAPI.isVampire(player, true) && !isLich(player);
    }

    public static boolean isWerepyre(PlayerEntity player)
    {
        return FabricLoader.getInstance().isModLoaded("besmirchment") && BSMTransformations.isWerepyre(player, true);
    }

    public static boolean isLich(PlayerEntity player)
    {
        return FabricLoader.getInstance().isModLoaded("besmirchment") && BSMTransformations.isLich(player, false);
    }

}
