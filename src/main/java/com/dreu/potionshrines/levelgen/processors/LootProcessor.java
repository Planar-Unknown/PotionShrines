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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.registry.PSProcTypes.LOOT_PROCESSOR;
import static net.minecraft.world.level.block.ChestBlock.FACING;

public class LootProcessor extends StructureProcessor {
    static final CompoundTag[] lootOptions = new CompoundTag[]{
            null,
            withStrings(Map.entry("LootTable", MODID + ":chests/dungeons/common"), Map.entry("id", "minecraft:chest")),
            withStrings(Map.entry("LootTable", MODID + ":chests/dungeons/uncommon"), Map.entry("id", "minecraft:chest")),
            withStrings(Map.entry("LootTable", MODID + ":chests/dungeons/rare"), Map.entry("id", "minecraft:chest")),
            withStrings(Map.entry("LootTable", MODID + ":chests/dungeons/mythical"), Map.entry("id", "minecraft:chest")),
    };
    public static final Codec<LootProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT.listOf().xmap(
                    list -> {
                        float[] array = new float[list.size()];
                        for (int i = 0; i < list.size(); i++) {array[i] = list.get(i);}
                        return array;
                    },
                    array -> {
                        List<Float> list = new ArrayList<>(array.length);
                        for (float value : array) {list.add(value);}
                        return list;
                    }
            )).fieldOf("processors").forGetter(processor -> processor.processors)
    ).apply(instance, LootProcessor::new));
    private final Map<String, float[]> processors;

    public LootProcessor(Map<String, float[]> processors){
        this.processors = processors;
    }
    @Override
    @SuppressWarnings({"unused", "ConstantValue"})
    @ParametersAreNonnullByDefault
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos blockPos, BlockPos origin, StructureTemplate.StructureBlockInfo worldBlock, StructureTemplate.StructureBlockInfo structureBlock, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (settings.shouldKeepLiquids()) settings.setKeepLiquids(false);
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
                return newInfo(structureBlock.pos, df(Blocks.CHEST).setValue(FACING, structureBlock.state.getValue(FACING)), lootOptions[i]);
            }
        }
        return newInfo(structureBlock.pos, df(Blocks.CHEST).setValue(FACING, structureBlock.state.getValue(FACING)), lootOptions[4]);
    }
    @Override
    public @NotNull StructureProcessorType<?> getType() {
        return LOOT_PROCESSOR.get();
    }

}

