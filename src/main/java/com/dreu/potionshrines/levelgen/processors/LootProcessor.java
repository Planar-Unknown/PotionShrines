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
import java.util.HashMap;
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
            withStrings(Map.entry("LootTable", MODID + ":chests/dungeons/epic"), Map.entry("id", "minecraft:chest")),
            withStrings(Map.entry("LootTable", MODID + ":chests/dungeons/legendary"), Map.entry("id", "minecraft:chest")),
    };
    public static final Codec<LootProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT.listOf().xmap(
                    list -> {float[] array = new float[list.size()];for (int i = 0; i < list.size(); i++) {array[i] = list.get(i);}return array;},
                    array -> {List<Float> list = new ArrayList<>(array.length);for (float value : array) {list.add(value);}return list;}
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
        for (int i = 0; i < weights.length; i++) {
            if (rarity < weights[i] * 0.01) {
                return (lootOptions[i] == null)
                        ? newInfo(structureBlock.pos, df(Blocks.AIR), null)
                        : newInfo(structureBlock.pos, df(Blocks.CHEST).setValue(FACING, structureBlock.state.getValue(FACING)), lootOptions[i]);
            }
        }
        throw new IllegalStateException("LootProcessor failed because the weights for Chest Type: [" + structureBlock.nbt.getString("LootTable") + "] do not properly accumulate up to 1.0");
    }
    @Override public @NotNull StructureProcessorType<?> getType() {return LOOT_PROCESSOR.get();}
    public static final String COMMON = MODID + ":chests/dungeons/common", UNCOMMON = MODID + ":chests/dungeons/uncommon", RARE = MODID + ":chests/dungeons/rare", EPIC = MODID + ":chests/dungeons/epic", LEGENDARY = MODID + ":chests/dungeons/legendary", GG = "_guaranteed";
    public static final HashMap<String, float[]> LOOT_PROCESSOR_RULES = new HashMap<>();
    static {
            LOOT_PROCESSOR_RULES.put(    COMMON,     new float[]{/*Empty*/75.00F, /*Common*/90.00F, /*Uncommon*/94.50F, /*Rare*/100.00F, /*Epic*/00.00F, /*Legendary*/00.00F});
            LOOT_PROCESSOR_RULES.put(   UNCOMMON,    new float[]{/*Empty*/20.00F, /*Common*/50.00F, /*Uncommon*/90.00F, /*Rare*/99.50F, /*Epic*/100.00F, /*Legendary*/00.00F});
            LOOT_PROCESSOR_RULES.put(     RARE,      new float[]{/*Empty*/00.00F, /*Common*/20.00F, /*Uncommon*/50.00F, /*Rare*/90.00F, /*Epic*/99.50F, /*Legendary*/100.00F});
            LOOT_PROCESSOR_RULES.put(     EPIC,      new float[]{/*Empty*/00.00F, /*Common*/00.00F, /*Uncommon*/20.00F, /*Rare*/50.00F, /*Epic*/90.00F, /*Legendary*/100.00F});
            LOOT_PROCESSOR_RULES.put(   LEGENDARY,   new float[]{/*Empty*/00.00F, /*Common*/00.00F, /*Uncommon*/00.00F, /*Rare*/20.00F, /*Epic*/50.00F, /*Legendary*/100.00F});

            LOOT_PROCESSOR_RULES.put(  COMMON + GG,  new float[]{0, /*Common*/100, 0, 0, 0, 0});
            LOOT_PROCESSOR_RULES.put( UNCOMMON + GG, new float[]{0, 0, /*Uncommon*/100, 0, 0, 0});
            LOOT_PROCESSOR_RULES.put(   RARE + GG,   new float[]{0, 0, 0, /*Rare*/100, 0, 0});
            LOOT_PROCESSOR_RULES.put(   EPIC + GG,   new float[]{0, 0, 0, 0, /*Epic*/100, 0});
            LOOT_PROCESSOR_RULES.put( LEGENDARY + GG,new float[]{0, 0, 0, 0, 0, /*Legendary*/100});
    }
}

