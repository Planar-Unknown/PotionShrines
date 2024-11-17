package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.levelgen.processors.LootProcessor;
import com.dreu.potionshrines.levelgen.processors.StateRetainProcessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static net.minecraft.world.level.block.Blocks.*;

public class PSProcLists {
    public static Map<String, float[]> lootProcessorsMap(){
        Map<String, float[]> map = new HashMap<>();
        map.put(MODID + ":chests/dungeons/common", new float[]{0.8F, 0.175F, 0.02F, 0.005F, 0.0F});
        map.put(MODID + ":chests/dungeons/uncommon", new float[]{0.4F, 0.35F, 0.175F, 0.065F, 0.01F});
        map.put(MODID + ":chests/dungeons/rare", new float[]{0.0F, 0.2F, 0.5F, 0.175F, 0.025F});
        map.put(MODID + ":chests/dungeons/mythical", new float[]{0.0F, 0.0F, 0.4F, 0.4F, 0.2F});
        map.put(MODID + ":chests/dungeons/common_guaranteed", new float[]{0.0F, 0.875F, 0.1F, 0.25F, 0.0F});
        map.put(MODID + ":chests/dungeons/uncommon_guaranteed", new float[]{0.0F, 0.585F, 0.29F, 0.11F, 0.015F});
        map.put(MODID + ":chests/dungeons/rare_guaranteed", new float[]{0.0F, 0.0F, 0.47F, 0.5F, 0.03F});
        map.put(MODID + ":chests/dungeons/mythical_guaranteed", new float[]{0.0F, 0.0F, 0.0F, 0.5F, 0.5F});
        return map;
    }

    public static final RuleTest Always = AlwaysTrueTest.INSTANCE;
    public static final DeferredRegister<StructureProcessorList> PROC_LISTS = DeferredRegister.create(Registry.PROCESSOR_LIST_REGISTRY, MODID);
    public static final Holder<StructureProcessorList> DUNGEON_STONE_BRICK = PROC_LISTS.register("dungeon_stone_brick",
        () -> new StructureProcessorList(ImmutableList.of(
                new RuleProcessor(ImmutableList.of(
                    simpleRule(new RandomBlockMatchTest(STONE_BRICKS, 0.2F), Always, CRACKED_STONE_BRICKS.defaultBlockState()),
                    simpleRule(new RandomBlockMatchTest(STONE_BRICKS, 0.2F), Always, MOSSY_STONE_BRICKS.defaultBlockState()),
                    simpleRule(new RandomBlockMatchTest(STONE_BRICK_WALL, 0.2F), Always, MOSSY_STONE_BRICK_WALL.defaultBlockState()))),
                new StateRetainProcessor(STONE_BRICK_STAIRS, MOSSY_STONE_BRICK_STAIRS, 0.2F),
                new StateRetainProcessor(STONE_BRICK_STAIRS, STONE_STAIRS, 0.05F),
                new StateRetainProcessor(STONE_BRICK_STAIRS, MOSSY_COBBLESTONE_STAIRS, 0.05F),
                new StateRetainProcessor(STONE_BRICK_SLAB, MOSSY_STONE_BRICK_SLAB, 0.2F),
                new StateRetainProcessor(STONE_BRICK_SLAB, STONE_SLAB, 0.2F),
                new StateRetainProcessor(STONE_BRICK_SLAB, MOSSY_COBBLESTONE_SLAB, 0.2F),
                new LootProcessor(lootProcessorsMap())
        ))).getHolder().get();

    public static ProcessorRule simpleRule(RuleTest input, RuleTest condition, BlockState result){return new ProcessorRule(input, condition, result);}
}
