package com.dreu.potionshrines.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;

import static com.dreu.potionshrines.PotionShrines.MODID;


public class PSTags {
    public static TagKey<EntityType<?>> entityTypeTagKey(String modid, String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(modid, name));
    }
    public static TagKey<Biome> biomeTagKey(String modid, String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(modid, name));
    }
    public static class Entities {
        public static final TagKey<EntityType<?>> MONSTERS = entityTypeTagKey(MODID, "considered_monster");
    }
    public static class Biomes {
        public static final TagKey<Biome> HAS_UG_STONE_BRICK_DUNGEON = biomeTagKey(MODID, "has_structure/stone_dungeon");
    }

}
