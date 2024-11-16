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

import javax.annotation.Nullable;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.registry.PSProcessors.LOOT_PROCESSOR;
import static net.minecraft.world.level.block.ChestBlock.FACING;

public class LootProcessor extends StructureProcessor {

    public static final Codec<LootProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("do_not_use_this").forGetter(processor -> processor.target)
    ).apply(instance, LootProcessor::new));

    private final boolean json;

    private final String target;

    CompoundTag[] lootOptions;
    float[] weights;

    public LootProcessor(String target) {
        this.target = target;
        json = true;
    }
    public LootProcessor(String target, float emptyChance, float commonChance, float uncommonChance, float rareChance, float mythicalChance, CompoundTag commonLoot, CompoundTag uncommonLoot, CompoundTag rareLoot, CompoundTag mythicalLoot) {
        this.target = target;
        weights = new float[]{emptyChance, commonChance, uncommonChance, rareChance, mythicalChance};
        lootOptions = new CompoundTag[]{null, commonLoot, uncommonLoot, rareLoot, mythicalLoot};
        this.json = false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos blockPos, BlockPos origin, StructureTemplate.StructureBlockInfo worldBlock, StructureTemplate.StructureBlockInfo structureBlock, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (json || !structureBlock.state.is(Blocks.CHEST) || !structureBlock.nbt.contains(target))
            return structureBlock;

        float totalWeight = 0;
        for (float weight : weights) {totalWeight += weight;}

        float rarity = rand.nextFloat() * totalWeight;

        float cumulativeWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (rarity < cumulativeWeight) {
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

