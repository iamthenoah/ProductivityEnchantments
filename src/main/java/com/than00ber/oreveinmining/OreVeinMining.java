package com.than00ber.oreveinmining;

import com.than00ber.oreveinmining.enchantments.ClusterEnchantment;
import com.than00ber.oreveinmining.event.BlockBreakHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod(OreVeinMining.MODID)
public class OreVeinMining {

    public static final String MODID = "oreveinmining";

    public OreVeinMining() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        MinecraftForge.EVENT_BUS.register(new BlockBreakHandler());
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        public static final Enchantment CLUSTER_ENCHANTMENT = new ClusterEnchantment();

        @SubscribeEvent
        public static void onEnchantmentRegistry(final RegistryEvent.Register<Enchantment> event) {
            event.getRegistry().register(CLUSTER_ENCHANTMENT.setRegistryName(MODID, "cluster"));
        }
    }
}
