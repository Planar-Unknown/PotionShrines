package com.dreu.potionshrines.levelgen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nullable;

import java.util.HashSet;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.registry.PSProcessors.STATE_RETAIN_PROCESSOR;

public class StateRetainProcessor extends StructureProcessor {

    public static final Codec<StateRetainProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.BLOCK.byNameCodec().fieldOf("from_block").forGetter(processor -> processor.fromBlock),
            Registry.BLOCK.byNameCodec().fieldOf("to_block").forGetter(processor -> processor.toBlock),
            Codec.FLOAT.fieldOf("chance").forGetter(processor -> processor.chance)
    ).apply(instance, StateRetainProcessor::new));

    private final Block fromBlock;
    private final Block toBlock;
    private final float chance;

    public StateRetainProcessor(Block fromBlock, Block toBlock, float chance) {
        this.fromBlock = fromBlock;
        if (new HashSet<>(toBlock.getStateDefinition().getPossibleStates().stream().map((StateHolder::getValues)).toList()).containsAll(fromBlock.getStateDefinition().getPossibleStates().stream().map(StateHolder::getValues).toList())){
            this.toBlock = toBlock;
        } else {
            LOGGER.warn("StateRetainProcessor cannot retain states between [{}] and [{}] because their possible states do not match.", fromBlock.getDescriptionId(), toBlock.getDescriptionId());
            this.toBlock = fromBlock;
        }
        this.chance = chance;
    }
    @Override
    public StructureProcessorType<?> getType() {
        return STATE_RETAIN_PROCESSOR.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos blockPos, BlockPos origin, StructureTemplate.StructureBlockInfo worldBlock, StructureTemplate.StructureBlockInfo structureBlock, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (!structureBlock.state.is(fromBlock) || rand.nextFloat() >= chance) {return structureBlock;}
        BlockState newState = df(toBlock);
        for (Property property : structureBlock.state.getProperties()) {
            newState = newState.setValue(property, structureBlock.state.getValue(property));
        }
        return new StructureTemplate.StructureBlockInfo(structureBlock.pos, newState, null);
    }

}