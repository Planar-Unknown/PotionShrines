package com.dreu.potionshrines.levelgen.processors.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("deprecation")
public record StateRetainRule(Block fromBlock, Block toBlock, float chance) {
    public static final Codec<StateRetainRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.BLOCK.byNameCodec().fieldOf("fromBlock").forGetter(rule -> rule.fromBlock),
            Registry.BLOCK.byNameCodec().fieldOf("toBlock").forGetter(rule -> rule.toBlock),
            Codec.FLOAT.fieldOf("chance").forGetter(rule -> rule.chance)
    ).apply(instance, StateRetainRule::new));
    public Block fromBlock(){return fromBlock;}
    public Block toBlock(){return toBlock;}
    public float chance(){return chance;}
}
