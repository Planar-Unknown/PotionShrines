package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.blocks.shrine.DecrepitShrineBlock;
import com.dreu.potionshrines.blocks.shrine.ShrineBaseBlock;
import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBlock;
import com.dreu.potionshrines.blocks.shrine.aura.AuraShrineBlock;
import com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.blocks.shrine.ShrineBaseBlock.*;
import static com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineBlock.LIGHT_LEVEL;
import static com.dreu.potionshrines.config.General.SHRINE_INDESTRUCTIBLE;

public class PSBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

        public static final RegistryObject<Block> SIMPLE_SHRINE = BLOCKS.register("simple_shrine",
                () -> new SimpleShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .lightLevel((blockstate) -> blockstate.getValue(LIGHT_LEVEL))
                        .emissiveRendering((blockState, blockGetter, blockPos) -> true)));
        public static final RegistryObject<Block> SIMPLE_SHRINE_BASE = BLOCKS.register("simple_shrine_base",
                () -> new ShrineBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(SHRINE_INDESTRUCTIBLE ? -1 : 5),
                        SIMPLE_SHRINE.get(),
                        SIMPLE_TOP_SHAPE,
                        SIMPLE_BOTTOM_SHAPE,
                        SIMPLE_COLLISION_SHAPE));
        public static final RegistryObject<Block> SIMPLE_SHRINE_DECREPIT = BLOCKS.register("simple_shrine_decrepit",
                () -> new DecrepitShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5),
                        SIMPLE_BOTTOM_SHAPE,
                        SIMPLE_TOP_SHAPE));

        public static final RegistryObject<Block> AOE_SHRINE = BLOCKS.register("aoe_shrine",
                () -> new AoEShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .lightLevel((blockstate) -> blockstate.getValue(LIGHT_LEVEL))
                        .emissiveRendering((blockState, blockGetter, blockPos) -> true)));
        public static final RegistryObject<Block> AOE_SHRINE_BASE = BLOCKS.register("aoe_shrine_base",
                () -> new ShrineBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(SHRINE_INDESTRUCTIBLE ? -1 : 5),
                        AOE_SHRINE.get(),
                        AOE_TOP_SHAPE,
                        AOE_BOTTOM_SHAPE,
                        AURA_COLLISION_SHAPE));
        public static final RegistryObject<Block> AOE_SHRINE_DECREPIT = BLOCKS.register("aoe_shrine_decrepit",
                () -> new DecrepitShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5),
                        AOE_BOTTOM_SHAPE,
                        AOE_TOP_SHAPE));

        public static final RegistryObject<Block> AURA_SHRINE = BLOCKS.register("aura_shrine",
                () -> new AuraShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .lightLevel((blockstate) -> blockstate.getValue(LIGHT_LEVEL))
                        .emissiveRendering((blockState, blockGetter, blockPos) -> true)));
        public static final RegistryObject<Block> AURA_SHRINE_BASE = BLOCKS.register("aura_shrine_base",
                () -> new ShrineBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(SHRINE_INDESTRUCTIBLE ? -1 : 5),
                        AURA_SHRINE.get(),
                        AURA_TOP_SHAPE,
                        AURA_BOTTOM_SHAPE,
                        AURA_COLLISION_SHAPE));
        public static final RegistryObject<Block> AURA_SHRINE_DECREPIT = BLOCKS.register("aura_shrine_decrepit",
                () -> new DecrepitShrineBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(5),
                        AURA_BOTTOM_SHAPE,
                        AURA_TOP_SHAPE));
//        @SuppressWarnings("SameParameterValue")
//        private static <T extends Block> RegistryObject<T> registerVanillaBlockAndItem(String name, Supplier<T> block, CreativeModeTab tab) {
//                RegistryObject<T> registeredBlock = VANILLA_BLOCKS.register(name, block);
//                VANILLA_ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), new Item.Properties().tab(tab)));
//                return registeredBlock;
//        }
}
