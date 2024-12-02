package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.levelgen.processors.LootProcessor;
import com.dreu.potionshrines.levelgen.processors.RetainStatesProcessor;
import com.dreu.potionshrines.levelgen.processors.rules.StateRetainRule;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.registries.DeferredRegister;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.levelgen.processors.LootProcessor.LOOT_PROCESSOR_RULES;
import static net.minecraft.world.level.block.Blocks.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class PSProcLists {


    public static final RuleTest Always = AlwaysTrueTest.INSTANCE;
    public static final DeferredRegister<StructureProcessorList> PROC_LISTS = DeferredRegister.create(Registry.PROCESSOR_LIST_REGISTRY, MODID);
    public static final Holder<StructureProcessorList> DUNGEON_STONE_BRICK = PROC_LISTS.register("dungeon_stone_brick",
        () -> new StructureProcessorList(ImmutableList.of(
                new LootProcessor(LOOT_PROCESSOR_RULES),
                new RuleProcessor(ImmutableList.of(
                        simpleRule(new RandomBlockMatchTest(STONE_BRICKS, 0.2F), Always, CRACKED_STONE_BRICKS.defaultBlockState()),
                        simpleRule(new RandomBlockMatchTest(STONE_BRICKS, 0.2F), Always, MOSSY_STONE_BRICKS.defaultBlockState()),
                        simpleRule(new RandomBlockMatchTest(STONE_BRICK_WALL, 0.2F), Always, MOSSY_STONE_BRICK_WALL.defaultBlockState()))),
                new RetainStatesProcessor(
                        new StateRetainRule(STONE_BRICK_STAIRS, MOSSY_STONE_BRICK_STAIRS, 0.2F),
                        new StateRetainRule(STONE_BRICK_STAIRS, STONE_STAIRS, 0.05F),
                        new StateRetainRule(STONE_BRICK_STAIRS, MOSSY_COBBLESTONE_STAIRS, 0.05F),
                        new StateRetainRule(STONE_BRICK_SLAB, MOSSY_STONE_BRICK_SLAB, 0.2F),
                        new StateRetainRule(STONE_BRICK_SLAB, STONE_SLAB, 0.2F),
                        new StateRetainRule(STONE_BRICK_SLAB, MOSSY_COBBLESTONE_SLAB, 0.2F))
        ))).getHolder().get();

    public static ProcessorRule simpleRule(RuleTest input, RuleTest condition, BlockState output){return new ProcessorRule(input, condition, output);}
}
