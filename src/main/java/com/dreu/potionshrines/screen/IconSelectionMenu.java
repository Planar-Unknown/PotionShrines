package com.dreu.potionshrines.screen;

import com.dreu.potionshrines.registry.PSMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IconSelectionMenu extends AbstractContainerMenu {
    @SuppressWarnings("unused") public IconSelectionMenu(int id, Inventory inv, FriendlyByteBuf buf) {this(id);}
    public IconSelectionMenu(int id) {
        super(PSMenuTypes.ICON_SELECTION_MENU.get() , id);
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return player.getInventory().getItem(i);
    }
    @Override
    public boolean stillValid(@NotNull Player player) {return true;}
}
