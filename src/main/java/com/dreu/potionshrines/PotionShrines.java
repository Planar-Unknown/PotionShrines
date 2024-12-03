package com.dreu.potionshrines;

import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineRenderer;
import com.dreu.potionshrines.blocks.shrine.aura.AuraShrineRenderer;
import com.dreu.potionshrines.blocks.shrine.simple.SimpleShrineRenderer;
import com.dreu.potionshrines.config.ExampleResourcePack;
import com.dreu.potionshrines.levelgen.structures.Structures;
import com.dreu.potionshrines.levelgen.structures.TemplatePools;
import com.dreu.potionshrines.network.PacketHandler;
import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSMenuTypes;
import com.dreu.potionshrines.screen.IconSelectionScreen;
import com.dreu.potionshrines.screen.aoe.AoEShrineScreen;
import com.dreu.potionshrines.screen.aura.AuraShrineScreen;
import com.dreu.potionshrines.screen.simple.SimpleShrineScreen;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.config.AoEShrine.AOE_SHRINES;
import static com.dreu.potionshrines.config.AoEShrine.TOTAL_WEIGHT_AOE;
import static com.dreu.potionshrines.config.AuraShrine.AURA_SHRINES;
import static com.dreu.potionshrines.config.AuraShrine.TOTAL_WEIGHT_AURA;
import static com.dreu.potionshrines.config.SimpleShrine.SHRINES;
import static com.dreu.potionshrines.config.SimpleShrine.TOTAL_WEIGHT;
import static com.dreu.potionshrines.registry.PSBlockEntities.BLOCK_ENTITIES;
import static com.dreu.potionshrines.registry.PSBlocks.BLOCKS;
import static com.dreu.potionshrines.registry.PSFeatures.Configured.CONFIGURED_FEATURES;
import static com.dreu.potionshrines.registry.PSFeatures.FEATURES;
import static com.dreu.potionshrines.registry.PSFeatures.Placed.PLACED_FEATURES;
import static com.dreu.potionshrines.registry.PSItems.ITEMS;
import static com.dreu.potionshrines.registry.PSMenuTypes.MENUS;
import static com.dreu.potionshrines.registry.PSProcLists.PROC_LISTS;
import static com.dreu.potionshrines.registry.PSProcTypes.PROCESSOR_TYPES;

@SuppressWarnings("SpellCheckingInspection")
@Mod(MODID)
public class PotionShrines {
    public static final String MODID = "potion_shrines";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random rand = new Random();
    public static final Map<String, BakedModel> BAKED_ICONS = new HashMap<>();
    public static final Set<String> SHRINE_ICONS = new HashSet<>();
    public static final int EDIT_BOX_HEIGHT = 18, COMMON_HEX = 0x858d6d, UNCOMMON_HEX = 0x78a126, RARE_HEX = 0x3de0e5, EPIC_HEX = 0x9553cc, LEGENDARY_HEX = 0xe8af4d;
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    static {
        //____Structures____
        TemplatePools.register();
        Structures.registerSets();

        //____Icon Resource Pack____
        ExampleResourcePack.generate();

        SHRINES.forEach(shrine -> TOTAL_WEIGHT += shrine.getInt("Weight"));
        AOE_SHRINES.forEach(shrine -> TOTAL_WEIGHT_AOE += shrine.getInt("Weight"));
        AURA_SHRINES.forEach(shrine -> TOTAL_WEIGHT_AURA += shrine.getInt("Weight"));

        File resourcePacks = new File("resourcepacks");
        String iconSearchPath = MODID + "\\textures\\icon";

        searchDirectory(resourcePacks, iconSearchPath, ".png", 5).forEach(png -> {
            String iconName = png.getName().substring(0, png.getName().length() - 4);  // Remove ".png" extension
            File modelsIconFolder = new File(png.getParentFile().getParentFile().getParent(), "\\models\\icon");
            File jsonFile = new File(modelsIconFolder, iconName + ".json");

            if (!jsonFile.exists()) {
                try {
                    Files.createDirectories(modelsIconFolder.toPath());
                    try (FileWriter writer = new FileWriter(jsonFile)) {
                        writer.write("{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"" + MODID + ":icon/" + iconName + "\"}}");
                        System.out.println("Wrote Model File For: " + iconName);
                    }
                } catch (IOException ignored) {}
            }

            SHRINE_ICONS.add(iconName);
        });
        SHRINE_ICONS.addAll(List.of(
                "absorption", "bad_omen", "blindness", "conduit_power", "darkness", "wither",
                "dolphins_grace", "fire_resistance", "glowing", "haste", "health_boost", "hero_of_the_village",
                "hunger", "instant_damage", "instant_health", "invisibility", "jump_boost", "levitation",
                "luck", "mining_fatigue", "nausea", "night_vision", "poison", "regeneration", "resistance",
                "saturation", "slow_falling", "slowness", "speed", "strength", "unluck", "water_breathing", "weakness",
                "xp_boost"
        ));
    }


    public PotionShrines() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        FEATURES.register(eventBus);
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
        MENUS.register(eventBus);
        EFFECTS.register(eventBus);
        PROC_LISTS.register(eventBus);
        PROCESSOR_TYPES.register(eventBus);

        PacketHandler.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static List<File> searchDirectory(File rootDir, String searchPath, String fileExtension, int maxDepth) {
        List<File> result = new ArrayList<>();
        searchDirectory(rootDir, searchPath, fileExtension, result, 0, maxDepth);
        return result;
    }
    private static void searchDirectory(File dir, String searchPath, String fileExtension, List<File> result, int depth, int maxDepth) {
        if (depth > maxDepth || !dir.isDirectory()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                searchDirectory(file, searchPath, fileExtension, result, depth + 1, maxDepth);
            } else if (file.getPath().contains(searchPath) && file.getName().endsWith(fileExtension)) {
                result.add(file);
            }
        }
    }

    @SuppressWarnings("unused") @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent @SuppressWarnings("unused")
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(PSMenuTypes.SIMPLE_SHRINE_MENU.get(), SimpleShrineScreen::new);
            MenuScreens.register(PSMenuTypes.AOE_SHRINE_MENU.get(), AoEShrineScreen::new);
            MenuScreens.register(PSMenuTypes.AURA_SHRINE_MENU.get(), AuraShrineScreen::new);
            MenuScreens.register(PSMenuTypes.ICON_SELECTION_MENU.get(), IconSelectionScreen::new);

            BlockEntityRenderers.register(PSBlockEntities.SIMPLE_SHRINE.get(), (c) -> new SimpleShrineRenderer());
            BlockEntityRenderers.register(PSBlockEntities.AOE_SHRINE.get(), (c) -> new AoEShrineRenderer());
            BlockEntityRenderers.register(PSBlockEntities.AURA_SHRINE.get(), (c) -> new AuraShrineRenderer());
        }
        @SubscribeEvent @SuppressWarnings("unused")
        public static void registerModels(ModelEvent.RegisterAdditional event){
            for (String icon : SHRINE_ICONS) {
                event.register(new ResourceLocation(MODID, "icon/" + icon));
            }
            event.register(new ResourceLocation(MODID, "icon/default"));
        }
        @SubscribeEvent @SuppressWarnings("unused")
        public static void bakeModels(ModelEvent.BakingCompleted event){
            for (String icon : SHRINE_ICONS) {
                ResourceLocation iconLocation = new ResourceLocation(MODID, "icon/" + icon);
                BAKED_ICONS.put(icon, Minecraft.getInstance().getModelManager().getModelBakery().getModel(iconLocation).bake(
                        Minecraft.getInstance().getModelManager().getModelBakery(),
                        (material) -> Minecraft.getInstance().getTextureAtlas(material.atlasLocation()).apply(material.texture()),
                        new SimpleModelState(Transformation.identity()),
                        iconLocation
                ));
            }
            ResourceLocation iconLocation = new ResourceLocation(MODID, "icon/default");
            BAKED_ICONS.put("default", Minecraft.getInstance().getModelManager().getModelBakery().getModel(iconLocation).bake(
                        Minecraft.getInstance().getModelManager().getModelBakery(),
                        (material) -> Minecraft.getInstance().getTextureAtlas(material.atlasLocation()).apply(material.texture()),
                        new SimpleModelState(Transformation.identity()),
                        iconLocation
            ));
        }

        @SubscribeEvent @SuppressWarnings({"unused", "deprecation"})
        public static void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
                for (String icon : SHRINE_ICONS) {
                    event.addSprite(new ResourceLocation(MODID, "icon/" + icon));
                }
                event.addSprite(new ResourceLocation(MODID, "icon/default"));
            }
        }
    }

    public static MobEffect getEffectFromString(String effect){
        try {
            return ForgeRegistries.MOB_EFFECTS.getDelegateOrThrow(new ResourceLocation(effect)).get();
        } catch (Exception ignore){
            return null;
        }
    }

    public static final List<String> romanNumerals = new ArrayList<>();
    static {
        romanNumerals.add("");
        romanNumerals.add("");
        for (int i = 2; i <= 255; i++) {
            romanNumerals.add(toRoman(i));
        }
    }

    // Method to convert an integer to Roman numeral
    public static String toRoman(int number) {
        String[] thousands = {"", "M"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[number / 1000] +
               hundreds[(number % 1000) / 100] +
               tens[(number % 100) / 10] +
               ones[number % 10] + " ";
    }
    public static String asTime(int seconds) {return String.format("%d:%02d", seconds / 60, seconds % 60);}
    public static BakedModel getBakedIconOrDefault(String key) {
        return BAKED_ICONS.get(key) == null ? BAKED_ICONS.get("default") : BAKED_ICONS.get(key);
    }
    public static BlockState df(Block block){return block.defaultBlockState();}
    public static BlockState df(BlockState block){return block.getBlock().defaultBlockState();}
    public static StructureTemplate.StructureBlockInfo newInfo(BlockPos pos, BlockState state, CompoundTag tag){
        return new StructureTemplate.StructureBlockInfo(pos, state, tag);
    }
    @SafeVarargs
    public static CompoundTag withStrings(Map.Entry<String, String>... entries){
        CompoundTag a = new CompoundTag();
        for (Map.Entry<String, String> entry : entries) {a.putString(entry.getKey(), entry.getValue());}
        return a;
    }
    public static boolean containsAny(String string, Collection<String> targets){
        for (String target : targets){
            if (string.contains(target)) return true;
        }
        return false;
    }
}
