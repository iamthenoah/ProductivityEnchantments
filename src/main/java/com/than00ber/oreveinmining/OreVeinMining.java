package com.than00ber.oreveinmining;

import com.than00ber.oreveinmining.enchantments.*;
import com.than00ber.oreveinmining.enchantments.types.*;
import com.than00ber.oreveinmining.events.BlockBreakHandler;
import com.than00ber.oreveinmining.events.RightClickHandler;
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
        MinecraftForge.EVENT_BUS.register(new RightClickHandler());
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        public static final CarverEnchantmentBase WOODCUTTING = new WoodcuttingEnchantment();
        public static final CarverEnchantmentBase CLUSTER = new ClusterEnchantment();
        public static final CarverEnchantmentBase DIGGING = new DiggingEnchantment();
        public static final CarverEnchantmentBase PLOWING = new PlowingEnchantment();
        public static final CarverEnchantmentBase FERTILITY = new FertilityEnchantment();
        public static final ChunkEnchantment CHUNK = new ChunkEnchantment();

        @SubscribeEvent
        public static void onEnchantmentRegistry(final RegistryEvent.Register<Enchantment> event) {
            event.getRegistry().registerAll(
                    WOODCUTTING.setRegistryName(MODID, "woodcutting"),
                    CLUSTER.setRegistryName(MODID, "cluster"),
                    DIGGING.setRegistryName(MODID, "digging"),
                    PLOWING.setRegistryName(MODID, "plowing"),
                    FERTILITY.setRegistryName(MODID, "fertility"),
                    CHUNK.setRegistryName(MODID, "chunk")
            );
        }
    }
}



//    public enum ToolRestrictionType {
//
//        SHOVEL(ToolType.SHOVEL, Tags.Blocks.DIRT, Tags.Blocks.SAND, Tags.Blocks.GRAVEL),
//        AXE(ToolType.AXE, Tags.Blocks.FENCES_WOODEN, Tags.Blocks.FENCE_GATES_WOODEN),
//        PICKAXE(ToolType.PICKAXE, Tags.Blocks.ORES, Tags.Blocks.ORES_NETHERITE_SCRAP, Tags.Blocks.ORES_REDSTONE);
//
//        public final ToolType TOOL_TYPE;
//        private final List<Tags.IOptionalNamedTag<Block>> VALID_BLOCKS;
//
//        public boolean canApplyToItem(ItemStack stack) {
//            return stack.getToolTypes().contains(TOOL_TYPE);
//        }
//
//        public boolean canBreakBlock(Block block) {
//            for (ResourceLocation tag : block.getTags())
//                if (VALID_BLOCKS.contains(tag)) return true;
//            return false;
//        }
//
//        @SafeVarargs
//        ToolRestrictionType(ToolType type, Tags.IOptionalNamedTag<Block>... tags) {
//            TOOL_TYPE = type;
//            VALID_BLOCKS = Arrays.asList(tags);
//        }
//    }