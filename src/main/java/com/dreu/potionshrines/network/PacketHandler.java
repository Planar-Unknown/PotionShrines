package com.dreu.potionshrines.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.dreu.potionshrines.PotionShrines.MODID;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, ResetCooldownPacket.class, ((r, b) -> {}), byteBuf -> new ResetCooldownPacket(), ResetCooldownPacket::handle);
        CHANNEL.registerMessage(1, SyncAoEShrinePacket.class, SyncAoEShrinePacket::toBytes, SyncAoEShrinePacket::new, SyncAoEShrinePacket::handle);
        CHANNEL.registerMessage(2, SyncSimpleShrinePacket.class, SyncSimpleShrinePacket::toBytes, SyncSimpleShrinePacket::new, SyncSimpleShrinePacket::handle);
        CHANNEL.registerMessage(3, SyncAuraShrinePacket.class, SyncAuraShrinePacket::toBytes, SyncAuraShrinePacket::new, SyncAuraShrinePacket::handle);
    }
}
