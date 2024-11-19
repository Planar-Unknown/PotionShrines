package com.dreu.potionshrines.levelgen.processors;

import com.dreu.potionshrines.levelgen.processors.rules.StateRetainRule;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;

import static com.dreu.potionshrines.PotionShrines.df;
import static com.dreu.potionshrines.registry.PSProcTypes.RETAIN_STATES_PROCESSOR;
@SuppressWarnings("deprecation")
public class RetainStatesProcessor extends StructureProcessor {

    public static final Codec<RetainStatesProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StateRetainRule.CODEC.listOf().fieldOf("rules").forGetter(processor -> List.of(processor.rules))
    ).apply(instance, rules -> new RetainStatesProcessor(rules.toArray(new StateRetainRule[0]))));

    private final StateRetainRule[] rules;

    public RetainStatesProcessor(StateRetainRule... rules){
        for (StateRetainRule rule : rules) {
            if (!new HashSet<>(rule.toBlock().getStateDefinition().getPossibleStates().stream().map((StateHolder::getValues)).toList()).containsAll(rule.fromBlock().getStateDefinition().getPossibleStates().stream().map(StateHolder::getValues).toList())){
                throw new IllegalStateException("RetainStatesProcessor cannot retain states between [" + rule.fromBlock().getDescriptionId() + "] and [" + rule.toBlock().getDescriptionId() + "] because their possible states do not match.");
            }
        }
        this.rules = rules;
    }

    @Override @SuppressWarnings({"unchecked", "rawtypes"}) @ParametersAreNonnullByDefault
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos blockPos, BlockPos origin, StructureTemplate.StructureBlockInfo worldBlock, StructureTemplate.StructureBlockInfo structureBlock, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (rules == null) return structureBlock;
        for (StateRetainRule rule : rules) {
            if (!structureBlock.state.is(rule.fromBlock()) || settings.getRandom(structureBlock.pos).nextFloat() >= rule.chance()) continue;
            BlockState newState = df(rule.toBlock());
            for (Property property : structureBlock.state.getProperties()) {
                newState = newState.setValue(property, structureBlock.state.getValue(property));
            }
            return new StructureTemplate.StructureBlockInfo(structureBlock.pos, newState, null);
        }
        return structureBlock;
    }

    @Override @ParametersAreNonnullByDefault
    public @org.jetbrains.annotations.Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos pos1, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo blockInfo1, StructurePlaceSettings settings) {
        return process(levelReader, pos, pos1, blockInfo, blockInfo1, settings, null);
    }

    @Override
    public @NotNull StructureProcessorType<?> getType() {
        return RETAIN_STATES_PROCESSOR.get();
    }

}