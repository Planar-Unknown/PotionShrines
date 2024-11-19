package com.dreu.potionshrines.registry;

import com.dreu.potionshrines.levelgen.processors.LootProcessor;
import com.dreu.potionshrines.levelgen.processors.RetainStatesProcessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.dreu.potionshrines.PotionShrines.MODID;

@SuppressWarnings("SpellCheckingInspection")
public class PSProcTypes {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPES =
            DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, MODID);

    public static final RegistryObject<StructureProcessorType<RetainStatesProcessor>> RETAIN_STATES_PROCESSOR =
            PROCESSOR_TYPES.register("stain_retain_processor",
                    () -> () -> RetainStatesProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<LootProcessor>> LOOT_PROCESSOR =
            PROCESSOR_TYPES.register("loot_processor",
                    () -> () -> LootProcessor.CODEC);
}
