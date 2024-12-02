package com.dreu.potionshrines.items;

import com.dreu.potionshrines.blocks.crate.Rarities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.dreu.potionshrines.PotionShrines.*;

public class LootCrateBlockItem extends BlockItem {

    public LootCrateBlockItem(Block block, Properties properties) {super(block, properties);}

    @Override @ParametersAreNonnullByDefault @SuppressWarnings("DataFlowIssue")
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (itemStack.hasTag() && Arrays.stream(Rarities.values()).map(Rarities::getSerializedName).toList().contains(itemStack.getTag().getString("Rarity"))) {
            String rarity = itemStack.getTag().getString("Rarity");
            int color;

            switch (rarity) {
                case "uncommon" -> color = UNCOMMON_HEX;
                case "rare" -> color = RARE_HEX;
                case "epic" -> color = EPIC_HEX;
                case "legendary" -> color = LEGENDARY_HEX;
                default -> color = COMMON_HEX;
            }

            components.add(Component.translatable("tooltip.potion_shrines." + rarity).withStyle(Style.EMPTY.withColor(color)));
        }
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack itemStack) {
        //TODO this
        return super.getTooltipImage(itemStack);
    }
}
