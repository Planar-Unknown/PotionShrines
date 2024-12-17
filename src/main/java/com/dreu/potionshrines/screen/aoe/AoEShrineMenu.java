package com.dreu.potionshrines.screen.aoe;

import com.dreu.potionshrines.blocks.shrine.aoe.AoEShrineBlockEntity;
import com.dreu.potionshrines.registry.PSMenuTypes;
import com.dreu.potionshrines.screen.ShrineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class AoEShrineMenu extends AbstractContainerMenu implements ShrineMenu {
    public final AoEShrineBlockEntity shrineEntity;
    public AoEShrineMenu(int id, Inventory inventory, FriendlyByteBuf extraData){
        this(id, inventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }
    public AoEShrineMenu(int id, BlockEntity blockEntity){
        super(PSMenuTypes.AOE_SHRINE_MENU.get() , id);
        shrineEntity = (AoEShrineBlockEntity) blockEntity;
    }

    @Override
    public void resetCooldown() {
        shrineEntity.setRemainingCooldown(0);
        shrineEntity.setChanged();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return player.getInventory().getItem(i);
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
