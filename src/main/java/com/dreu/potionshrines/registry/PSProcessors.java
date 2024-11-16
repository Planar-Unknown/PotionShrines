package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.levelgen.processors.LootProcessor;
import com.dreu.potionshrines.levelgen.processors.StateRetainProcessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PSProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPES =
            DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, "potion_shrines");

    public static final RegistryObject<StructureProcessorType<StateRetainProcessor>> STATE_RETAIN_PROCESSOR =
            PROCESSOR_TYPES.register("stain_retain_processor",
                    () -> () -> StateRetainProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<LootProcessor>> LOOT_PROCESSOR =
            PROCESSOR_TYPES.register("stain_retain_processor",
                    () -> () -> LootProcessor.CODEC);
}
