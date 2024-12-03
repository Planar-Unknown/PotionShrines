package com.dreu.potionshrines.blocks.shrine.aura;

import com.dreu.potionshrines.registry.PSBlockEntities;
import com.dreu.potionshrines.registry.PSTags;
import com.dreu.potionshrines.screen.aura.AuraShrineMenu;
import com.electronwill.nightconfig.core.Config;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.PotionShrines.getEffectFromString;
import static com.dreu.potionshrines.config.AuraShrine.getRandomAuraShrine;

@SuppressWarnings("DataFlowIssue")
public class AuraShrineBlockEntity extends BlockEntity implements MenuProvider {
    private int maxCooldown = 0, remainingCooldown = 0, maxDuration = 0, remainingDuration = 0, radius = 0, amplifier = 1;
    private String effect = "null", icon = "default";
    private boolean effectPlayers = false, effectMonsters = false, replenish = true, active = false;

    public AuraShrineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PSBlockEntities.AURA_SHRINE.get(), blockPos, blockState);
    }

    public AuraShrineBlockEntity fromConfig() {
        Config aoeShrine = getRandomAuraShrine();
        amplifier = Mth.clamp((int) aoeShrine.get("Amplifier") - 1, 1, 256);
        maxDuration = Mth.clamp(aoeShrine.get("AuraDuration"), 1, 999999) * 20;
        maxCooldown = Mth.clamp(aoeShrine.get("Cooldown"), 3, 999999) * 20;
        replenish = aoeShrine.get("Replenish");
        effect = aoeShrine.get("Effect");
        icon = aoeShrine.get("Icon");
        effectPlayers = aoeShrine.get("Players");
        effectMonsters = aoeShrine.get("Monsters");
        radius = Mth.clamp(aoeShrine.get("Radius"), 3, 64);
        return this;
    }

    @SuppressWarnings("unused")
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AuraShrineBlockEntity shrine) {
        if (shrine.active){
            if (level.getGameTime() % 20 == 1){
                effectEntities(level, blockPos, shrine);
            }
            shrine.remainingDuration--;
            if (shrine.remainingDuration == 0) {
                shrine.active = false;
            }
        } else if (shrine.remainingCooldown > 0) shrine.remainingCooldown--;
                
        if (shrine.getLevel().getGameTime() % 2 == 1) {
            spawnAreaParticles(shrine);
        }
    }

    private static void effectEntities(Level level, BlockPos blockPos, AuraShrineBlockEntity shrine) {
        if (shrine.canEffectPlayers()) {
            MobEffect mobEffect = getEffectFromString(shrine.getEffect());
            level.getEntitiesOfClass(Player.class, new AABB(blockPos).inflate(shrine.getRadius())).stream()
                    .filter(nearPlayer -> nearPlayer.blockPosition().distSqr(blockPos) <= shrine.getRadius() * shrine.getRadius())
                    .toList().forEach(filteredPlayer -> {
                        assert mobEffect != null;
                        if (filteredPlayer.hasEffect(mobEffect)) {
                            if (filteredPlayer.getEffect(mobEffect).getAmplifier() >= shrine.getAmplifier() - 1)
                                filteredPlayer.getEffect(mobEffect).update(new MobEffectInstance(
                                    mobEffect, 21, shrine.getAmplifier() - 1));
                            else {
                                MobEffectInstance hiddenEffect = filteredPlayer.getEffect(mobEffect);
                                filteredPlayer.removeEffect(mobEffect);
                                filteredPlayer.addEffect(new MobEffectInstance(mobEffect, 21, shrine.amplifier - 1, false, true, true, hiddenEffect, mobEffect.createFactorData()));
                            }
                        } else {
                            filteredPlayer.addEffect(new MobEffectInstance(
                                    mobEffect, 21, shrine.getAmplifier() - 1));
                        }
                    });
        }

        if (shrine.canEffectMonsters())
            level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos).inflate(shrine.getRadius())).stream()
                    .filter(nearEntity -> nearEntity.blockPosition().distSqr(blockPos) <= shrine.getRadius() * shrine.getRadius()
                            && (nearEntity instanceof Monster || nearEntity.getType().getTags().toList().contains(PSTags.Entities.MONSTERS)))
                    .toList().forEach(filteredMonster ->
                            filteredMonster.addEffect(new MobEffectInstance(
                                    getEffectFromString(shrine.getEffect()),
                                    25,
                                    shrine.getAmplifier())));
    }

    private static void spawnAreaParticles(AuraShrineBlockEntity shrine) {
        Vector3f color = new Vector3f(
                (getEffectFromString(shrine.effect).getColor() >> 16 & 0xFF) / 255.0f,
                (getEffectFromString(shrine.effect).getColor() >> 8 & 0xFF) / 255.0f,
                (getEffectFromString(shrine.effect).getColor() & 0xFF) / 255.0f
        );
        int particleCount = (int) Math.pow((double) shrine.radius / 5, 3) + 1;
        for (int i = 0; i < particleCount; i++) {
            double theta = Math.random() * 2 * Math.PI;
            double phi = Math.acos(2 * Math.random() - 1);
            shrine.getLevel().addParticle(ParticleTypes.ENTITY_EFFECT,
                    shrine.getBlockPos().getX() + 0.5 + shrine.radius * Math.sin(phi) * Math.cos(theta),
                    shrine.getBlockPos().getY() + 0.5 + shrine.radius * Math.sin(phi) * Math.sin(theta),
                    shrine.getBlockPos().getZ() + 0.5 + shrine.radius * Math.cos(phi),
                    color.x(), color.y(), color.z());
        }
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("effect", effect);
        nbt.putInt("duration", maxDuration);
        nbt.putInt("max_cooldown", maxCooldown);
        nbt.putBoolean("replenish", replenish);
        nbt.putInt("remaining_cooldown", remainingCooldown);
        nbt.putInt("amplifier", amplifier);
        nbt.putString("icon", icon);
        nbt.putBoolean("players", effectPlayers);
        nbt.putBoolean("monsters", effectMonsters);
        nbt.putInt("radius", radius);
        super.saveAdditional(nbt);
    }
    @Override
    public void load(@NotNull CompoundTag nbt){
        super.load(nbt);
        setAmplifier(nbt.getInt("amplifier"));
        setRemainingCooldown(nbt.getInt("remaining_cooldown"));
        setMaxCooldown(nbt.getInt("max_cooldown"));
        setCanReplenish(nbt.getBoolean("replenish"));
        setMaxDuration(nbt.getInt("duration"));
        setEffect(nbt.getString("effect"));
        setIcon(nbt.getString("icon"));
        setCanEffectPlayers(nbt.getBoolean("players"));
        setCanEffectMonsters(nbt.getBoolean("monsters"));
        setRadius(nbt.getInt("radius"));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt);
        return nbt;
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    public String getEffect(){return effect;}
    public int getAmplifier(){return amplifier;}
    public int getMaxCooldown(){return maxCooldown;}
    public int getRemainingCooldown(){return remainingCooldown;}
    public int getMaxDuration(){return maxDuration;}
    @SuppressWarnings("unused")
    public int getRemainingDuration(){return remainingDuration;}
    public int getRadius() {return radius;}
    public boolean canEffectPlayers(){return effectPlayers;}
    public boolean canEffectMonsters(){return effectMonsters;}
    public boolean canReplenish(){return replenish;}
    public String getIcon(){return icon;}

    public void setEffect(String resourceLocation){effect = resourceLocation;}
    public void setAmplifier(int lvl){amplifier = Mth.clamp(lvl, 1, 256);}
    public void setMaxCooldown(int ticks){
        maxCooldown = Mth.clamp(ticks, 60, 19999980);
        if (remainingCooldown > maxCooldown) remainingCooldown = maxCooldown;
    }
    public void setRemainingCooldown(int ticks){
        remainingCooldown = Mth.clamp(ticks, 0, maxCooldown);
        if (remainingCooldown < maxCooldown) remainingDuration = 0; 
    }
    public void setMaxDuration(int ticks){
        maxDuration = Mth.clamp(ticks, 1, 19999980);
        if (remainingDuration > maxDuration) remainingDuration = maxDuration;
    }
    @SuppressWarnings("unused")
    public void setRemainingDuration(int ticks){
        remainingDuration = Mth.clamp(ticks, 1, maxDuration);
        if (remainingDuration > 0) remainingCooldown = maxCooldown;
    }
    public void setRadius(int blocks){radius = Mth.clamp(blocks, 3, 64);}
    public void setCanEffectPlayers(boolean b){effectPlayers = b;}
    public void setCanEffectMonsters(boolean b){effectMonsters = b;}
    public void setCanReplenish(boolean b){replenish = b;}
    public void setIcon(String name){icon = name;}

    public boolean canUse() {
        return remainingCooldown == 0 ;
    }
    public void activateAura(){
        active = true;
        remainingCooldown = maxCooldown;
        remainingDuration = maxDuration;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui." + MODID + ".shrine_options");
    }

    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AuraShrineMenu(id, this);
    }
}
