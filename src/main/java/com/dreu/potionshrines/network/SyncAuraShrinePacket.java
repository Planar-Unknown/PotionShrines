package com.dreu.potionshrines.network;

import com.dreu.potionshrines.screen.aura.AuraShrineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class SyncAuraShrinePacket {
    private final String effect, icon;
    private final int amplifier, radius, maxDuration, maxCooldown, remainingDuration, remainingCooldown;
    private final boolean effectPlayers, effectMonsters, replenish, active;
    public SyncAuraShrinePacket(String effect, int amplifier, int duration, int maxCooldown, int radius, boolean effectPlayers, boolean effectMonsters, boolean replenish, String icon, boolean active, int remainingCooldown, int remainingDuration){
        this.effect = effect;
        this.amplifier = amplifier;
        this.radius = radius;
        this.maxDuration = duration;
        this.maxCooldown = maxCooldown;
        this.remainingDuration = remainingDuration;
        this.remainingCooldown = remainingCooldown;
        this.effectPlayers = effectPlayers;
        this.effectMonsters = effectMonsters;
        this.replenish = replenish;
        this.icon = icon;
        this.active = active;
    }
    public SyncAuraShrinePacket(FriendlyByteBuf buffer) {
        effect = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        amplifier = buffer.readInt();
        maxDuration = buffer.readInt();
        maxCooldown = buffer.readInt();
        radius = buffer.readInt();
        effectPlayers = buffer.readBoolean();
        effectMonsters = buffer.readBoolean();
        replenish = buffer.readBoolean();
        icon = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        active = buffer.readBoolean();
        remainingDuration = buffer.readInt();
        remainingCooldown = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(effect.length());
        buffer.writeCharSequence(effect, StandardCharsets.UTF_8);
        buffer.writeInt(amplifier);
        buffer.writeInt(maxDuration);
        buffer.writeInt(maxCooldown);
        buffer.writeInt(radius);
        buffer.writeBoolean(effectPlayers);
        buffer.writeBoolean(effectMonsters);
        buffer.writeBoolean(replenish);
        buffer.writeInt(icon.length());
        buffer.writeCharSequence(icon, StandardCharsets.UTF_8);
        buffer.writeBoolean(active);
        buffer.writeInt(remainingDuration);
        buffer.writeInt(remainingCooldown);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof AuraShrineMenu menu) {
                menu.shrineEntity.setEffect(effect);
                menu.shrineEntity.setAmplifier(amplifier);
                menu.shrineEntity.setMaxDuration(maxDuration);
                menu.shrineEntity.setMaxCooldown(maxCooldown);
                menu.shrineEntity.setRadius(radius);
                menu.shrineEntity.setCanEffectPlayers(effectPlayers);
                menu.shrineEntity.setCanEffectMonsters(effectMonsters);
                menu.shrineEntity.setCanReplenish(replenish);
                menu.shrineEntity.setIcon(icon);
                menu.shrineEntity.setActive(active);
                menu.shrineEntity.setRemainingDuration(remainingDuration);
                menu.shrineEntity.setRemainingCooldown(remainingCooldown);

                menu.shrineEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
