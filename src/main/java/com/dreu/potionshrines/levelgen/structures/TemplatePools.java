package com.dreu.potionshrines.levelgen.structures;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.registry.PSProcLists.DUNGEON_STONE_BRICK;
public class TemplatePools {
    public static final String SB = MODID + ":dungeons/stone_brick/";
    public static final Holder<StructureTemplatePool> UG_STONE_BRICK_START = Pools.register(new StructureTemplatePool(
            new ResourceLocation(SB + "ug_start"),
            new ResourceLocation("empty"),
            List.of(Pair.of(StructurePoolElement.single(SB + "ug_start", DUNGEON_STONE_BRICK), 1)),
            StructureTemplatePool.Projection.RIGID));

    public static void register(){
        Pools.register(new StructureTemplatePool(
                new ResourceLocation(SB + "hallways"),
                new ResourceLocation("empty"),
                List.of(
                        Pair.of(StructurePoolElement.single(SB + "drop_01", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_02", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_03", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_04", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_05", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_06", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_07", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_08", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "drop_09", DUNGEON_STONE_BRICK), 1),
                        
                        Pair.of(StructurePoolElement.single(SB + "spiral_01", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_02", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_03", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_04", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_05", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_06", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_07", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_08", DUNGEON_STONE_BRICK), 1),
                        Pair.of(StructurePoolElement.single(SB + "spiral_09", DUNGEON_STONE_BRICK), 1),

                        Pair.of(StructurePoolElement.single(SB + "straight_stair", DUNGEON_STONE_BRICK), 3),

                        Pair.of(StructurePoolElement.single(SB + "hallway_01", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_02", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_03", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_04", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_05", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_06", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_07", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_08", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_09", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_10", DUNGEON_STONE_BRICK), 2),
                        Pair.of(StructurePoolElement.single(SB + "hallway_11", DUNGEON_STONE_BRICK), 2),

                        Pair.of(StructurePoolElement.single(SB + "mini_bi", DUNGEON_STONE_BRICK), 6),
                        Pair.of(StructurePoolElement.single(SB + "mini_tri", DUNGEON_STONE_BRICK), 6),
                        Pair.of(StructurePoolElement.single(SB + "mini_quad", DUNGEON_STONE_BRICK), 6),
                        Pair.of(StructurePoolElement.single(SB + "mini_corner", DUNGEON_STONE_BRICK), 6),

                        Pair.of(StructurePoolElement.single(SB + "large_bridge", DUNGEON_STONE_BRICK), 3),
                        Pair.of(StructurePoolElement.single(SB + "large_bridge_collapsed", DUNGEON_STONE_BRICK), 3),

                        Pair.of(StructurePoolElement.single(SB + "large_corner", DUNGEON_STONE_BRICK), 4),
                        Pair.of(StructurePoolElement.single(SB + "large_drop", DUNGEON_STONE_BRICK), 4),
                        Pair.of(StructurePoolElement.single(SB + "large_quad", DUNGEON_STONE_BRICK), 4),
                        Pair.of(StructurePoolElement.single(SB + "large_tri", DUNGEON_STONE_BRICK), 4),
                        Pair.of(StructurePoolElement.single(SB + "large_straight", DUNGEON_STONE_BRICK), 3)
                ),
                StructureTemplatePool.Projection.RIGID));

        Pools.register(new StructureTemplatePool(
                new ResourceLocation(SB + "spawners"),
                new ResourceLocation("empty"),
                List.of(
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/zombie", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/skeleton", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/cave_spider", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/spider", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/drowned", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/slime", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/vindicator", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":spawners/witch", ProcessorLists.EMPTY), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(
                new ResourceLocation(MODID + ":dungeons/shrines"),
                new ResourceLocation("empty"),
                List.of(
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/decrepit_aoe", ProcessorLists.EMPTY), 12),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/decrepit_aoe_emerald", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/decrepit_aura", ProcessorLists.EMPTY), 12),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/decrepit_aura_emerald", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/decrepit_simple", ProcessorLists.EMPTY), 12),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/decrepit_simple_emerald", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/random_aoe", ProcessorLists.EMPTY), 12),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/random_aoe_emerald", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/random_aura", ProcessorLists.EMPTY), 12),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/random_aura_emerald", ProcessorLists.EMPTY), 1),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/random_simple", ProcessorLists.EMPTY), 12),
                        Pair.of(StructurePoolElement.single(MODID + ":shrines/random_simple_emerald", ProcessorLists.EMPTY), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
    }
}
