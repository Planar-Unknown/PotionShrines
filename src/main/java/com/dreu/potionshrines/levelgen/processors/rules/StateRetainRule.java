package com.dreu.potionshrines.levelgen.processors.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;

public class StateRetainRule {
    public static final Codec<StateRetainRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.BLOCK.byNameCodec().fieldOf("fromBlock").forGetter(rule -> rule.fromBlock),
            Registry.BLOCK.byNameCodec().fieldOf("toBlock").forGetter(rule -> rule.toBlock),
            Codec.FLOAT.fieldOf("chance").forGetter(rule -> rule.chance)
    ).apply(instance, StateRetainRule::new));
    public final Block fromBlock, toBlock;
    public final float chance;
    public StateRetainRule(Block fromBlock, Block toBlock, float chance) {
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.chance = chance;
    }
}
