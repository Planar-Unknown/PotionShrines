package com.dreu.potionshrines.levelgen.structures;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.registry.PSTags.Biomes.HAS_UG_STONE_BRICK_DUNGEON;
@SuppressWarnings({"deprecation", "SameParameterValue"})
public class Structures {
    public static void registerSets() {
        BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY,
                new ResourceLocation(MODID, "ug_stone_brick_dungeon")), new StructureSet(UG_STONE_BRICK_DUNGEON,
                new RandomSpreadStructurePlacement(
                        /*Spacing*/ 20,
                        /*Separation*/ 19,
                        RandomSpreadType.TRIANGULAR,
                        /*Salt*/ 1464623449)));
    }

    public static final Holder<Structure> UG_STONE_BRICK_DUNGEON = BuiltinRegistries.register(BuiltinRegistries.STRUCTURES, ResourceKey.create(Registry.STRUCTURE_REGISTRY,
            new ResourceLocation(MODID, "ug_stone_brick_dungeon")),
            new JigsawStructure(new Structure.StructureSettings(
                    biomes(HAS_UG_STONE_BRICK_DUNGEON), Map.of(),
                    GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                    TerrainAdjustment.NONE),
                    TemplatePools.UG_STONE_BRICK_START,
                    /*Jigsaw Levels*/ 7,
                    between(15, 45),
                    /*Use Expansion Hack*/ false
            ));


    //_______________HELPERS_____________________
    private static HeightProvider between(int min, int max) {return UniformHeight.of(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max));}

    private static HolderSet<Biome> biomes(TagKey<Biome> key) {
        return BuiltinRegistries.BIOME.getOrCreateTag(key);
    }
}
