package com.dreu.potionshrines.levelgen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

import java.util.*;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.registry.PSProcTypes.LOOT_PROCESSOR;
import static net.minecraft.world.level.block.ChestBlock.FACING;

public class LootProcessor extends StructureProcessor {
    static CompoundTag[] lootOptions = new CompoundTag[]{
            null,
            withStrings(Pair.of("LootTable", MODID + ":chests/dungeons/common"), Pair.of("id", "minecraft:chest")),
            withStrings(Pair.of("LootTable", MODID + ":chests/dungeons/uncommon"), Pair.of("id", "minecraft:chest")),
            withStrings(Pair.of("LootTable", MODID + ":chests/dungeons/rare"), Pair.of("id", "minecraft:chest")),
            withStrings(Pair.of("LootTable", MODID + ":chests/dungeons/mythical"), Pair.of("id", "minecraft:chest")),
    };
    public static final Codec<LootProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT.listOf().xmap(
                    list -> {
                        // Convert List<Float> to float[]
                        float[] array = new float[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            array[i] = list.get(i);
                        }
                        return array;
                    },
                    array -> {
                        // Convert float[] to List<Float>
                        List<Float> list = new ArrayList<>(array.length);
                        for (float value : array) {
                            list.add(value);
                        }
                        return list;
                    }
            )).fieldOf("processors").forGetter(processor -> processor.processors)
    ).apply(instance, LootProcessor::new));
    private final Map<String, float[]> processors;

    public LootProcessor(Map<String, float[]> processors){
        this.processors = processors;
    }
    @Override
    @SuppressWarnings("unchecked")
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos blockPos, BlockPos origin, StructureTemplate.StructureBlockInfo worldBlock, StructureTemplate.StructureBlockInfo structureBlock, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (!structureBlock.state.is(Blocks.CHEST) || structureBlock.nbt == null || !structureBlock.nbt.contains("LootTable") || !containsAny(structureBlock.nbt.getString("LootTable"), processors.keySet())) {return structureBlock;}

        float[] weights = processors.get(structureBlock.nbt.getString("LootTable"));

        float rarity = rand.nextFloat();
        float cumulativeWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (rarity < cumulativeWeight) {
                if (lootOptions[i] == null){
                    return newInfo(structureBlock.pos, df(Blocks.AIR), null);
                }
                System.out.println("With: ");
                System.out.println(newInfo(structureBlock.pos, df(Blocks.CHEST).setValue(FACING, structureBlock.state.getValue(FACING)), lootOptions[i]));
                return newInfo(structureBlock.pos, df(Blocks.CHEST).setValue(FACING, structureBlock.state.getValue(FACING)), lootOptions[i]);
            }
        }
        return newInfo(structureBlock.pos, df(Blocks.CHEST).setValue(FACING, structureBlock.state.getValue(FACING)), lootOptions[4]);
    }
    @Override
    public StructureProcessorType<?> getType() {
        return LOOT_PROCESSOR.get();
    }

}

