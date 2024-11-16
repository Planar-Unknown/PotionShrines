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

import static com.dreu.potionshrines.PotionShrines.MODID;
import static net.minecraft.world.level.block.Blocks.*;

public class PSProcLists {
    private static final CompoundTag COMMON_TAG;
    private static final CompoundTag UNCOMMON_TAG;
    private static final CompoundTag RARE_TAG;
    private static final CompoundTag MYTHICAL_TAG;
    static {
        CompoundTag a = new CompoundTag();
        a.putString("LootTable", MODID + ":chests/dungeons/common");
        COMMON_TAG = (CompoundTag) new CompoundTag().put("BlockEntityTag", a);
        CompoundTag b = new CompoundTag();
        b.putString("LootTable", MODID + ":chests/dungeons/uncommon");
        UNCOMMON_TAG = (CompoundTag) new CompoundTag().put("BlockEntityTag", b);
        CompoundTag c = new CompoundTag();
        c.putString("LootTable", MODID + ":chests/dungeons/rare");
        RARE_TAG = (CompoundTag) new CompoundTag().put("BlockEntityTag", c);
        CompoundTag d = new CompoundTag();
        d.putString("LootTable", MODID + ":chests/dungeons/mythical");
        MYTHICAL_TAG = (CompoundTag) new CompoundTag().put("BlockEntityTag", d);
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
                new LootProcessor("common", 0.5F, 0.1F, 0.1F, 0.1F, 0.5F, COMMON_TAG, UNCOMMON_TAG, RARE_TAG, MYTHICAL_TAG)
        ))).getHolder().get();

    public static ProcessorRule simpleRule(RuleTest input, RuleTest condition, BlockState result){return new ProcessorRule(input, condition, result);}
}
