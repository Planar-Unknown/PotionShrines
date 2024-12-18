package com.dreu.potionshrines.screen.aura;

import com.dreu.potionshrines.network.ResetCooldownPacket;
import com.dreu.potionshrines.network.SyncAuraShrinePacket;
import com.dreu.potionshrines.screen.IconScreen;
import com.dreu.potionshrines.screen.IconSelectionMenu;
import com.dreu.potionshrines.screen.IconSelectionScreen;
import com.dreu.potionshrines.screen.ShrineScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.dreu.potionshrines.PotionShrines.*;
import static com.dreu.potionshrines.network.PacketHandler.CHANNEL;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class AuraShrineScreen extends ShrineScreen<AuraShrineMenu> implements IconScreen<AuraShrineScreen> {
    private EditBox radiusBox, maxDurationBox;
    private Button effectMonstersButton, effectPlayersButton, resetDurationButton;

    public AuraShrineScreen(AuraShrineMenu auraShrineMenu, Inventory inventory, Component component) {
        super(auraShrineMenu, inventory, component);
        imageWidth = 296;
        imageHeight = 196;
        iconX = 123.5;
        iconY = 103;
        backgroundTexture = new ResourceLocation(MODID, "textures/gui/aoe_shrine_screen.png");
    }

    @Override
    protected void containerTick() {
        resetCooldownButton.setMessage(Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)));
        resetDurationButton.setMessage(Component.literal(String.valueOf(menu.shrineEntity.getRemainingDuration() / 20)));
        boolean areButtonsActive = !(menu.shrineEntity.getRemainingCooldown() == 0 && menu.shrineEntity.getRemainingDuration() == 0);
        resetCooldownButton.active = areButtonsActive;
        resetDurationButton.active = areButtonsActive;

        if (cooldownDelay > 0) cooldownDelay--;
        effectBox.tick();
        amplifierBox.tick();
        maxDurationBox.tick();
        maxCooldownBox.tick();
        radiusBox.tick();
    }

    @Override
    protected void initialize() {
        saveButton = new Button(leftPos + 222, topPos + 166, NUMBER_BOX_WIDTH, 20, Component.translatable("gui." + MODID + ".save"), this::onSaveClick);
        resetCooldownButton = new Button(leftPos + 125, topPos + 65, 55, 20, Component.literal(String.valueOf(menu.shrineEntity.getRemainingCooldown() / 20)), this::onCooldownClick);
        resetDurationButton = new Button(leftPos + 8, topPos + 65, 55, 20, Component.literal(String.valueOf(menu.shrineEntity.getRemainingDuration() / 20)), this::onCooldownClick);
        blockNbtButton = new Button(leftPos + 82, topPos + 166, NUMBER_BOX_WIDTH, 20, Component.translatable("gui." + MODID + ".blockNbt"), this::onCopyBlockNbtClick);
        itemNbtButton = new Button(leftPos + 148, topPos + 166, NUMBER_BOX_WIDTH, 20, Component.translatable("gui." + MODID + ".itemNbt"), this::onCopyItemNbtClick);

        effectBox = new EditBox(font, leftPos + 8, topPos + 32, EFFECT_BOX_WIDTH, EDIT_BOX_HEIGHT, Component.literal(""));
        effectBox.setMaxLength(100);
        effectBox.setVisible(true);
        effectBox.setTextColor(0xFFFFFF);
        effectBox.setResponder(this::onEffectChanged);
        effectBox.setValue(menu.shrineEntity.getEffect());

        amplifierBox = new EditBox(font, leftPos + 243, topPos + 32, 45, EDIT_BOX_HEIGHT, Component.literal(""));
        amplifierBox.setMaxLength(3);
        amplifierBox.setVisible(true);
        amplifierBox.setTextColor(0xFFFFFF);
        amplifierBox.setResponder(this::onAmplifierChanged);
        amplifierBox.setFilter((s -> s.matches("\\d*")));
        amplifierBox.setValue(String.valueOf(menu.shrineEntity.getAmplifier()));

        maxDurationBox = new EditBox(font, leftPos + 63, topPos + 66, 55, EDIT_BOX_HEIGHT, Component.literal(""));
        maxDurationBox.setMaxLength(6);
        maxDurationBox.setVisible(true);
        maxDurationBox.setTextColor(0xFFFFFF);
        maxDurationBox.setResponder(this::onDurationChanged);
        maxDurationBox.setFilter((s -> s.matches("\\d*")));
        maxDurationBox.setValue(String.valueOf(menu.shrineEntity.getMaxDuration() / 20));


        maxCooldownBox = new EditBox(font, leftPos + 180, topPos + 66, 55, EDIT_BOX_HEIGHT, Component.literal(""));
        maxCooldownBox.setMaxLength(6);
        maxCooldownBox.setVisible(true);
        maxCooldownBox.setTextColor(0xFFFFFF);
        maxCooldownBox.setResponder(this::onCooldownChanged);
        maxCooldownBox.setFilter((s -> s.matches("\\d*")));
        maxCooldownBox.setValue(String.valueOf(menu.shrineEntity.getMaxCooldown() / 20));

        radiusBox = new EditBox(font, leftPos + 243, topPos + 66, 45, EDIT_BOX_HEIGHT, Component.literal(""));
        radiusBox.setMaxLength(2);
        radiusBox.setVisible(true);
        radiusBox.setTextColor(0xFFFFFF);
        radiusBox.setResponder(this::onRadiusChanged);
        radiusBox.setFilter((s -> s.matches("\\d*")));
        radiusBox.setValue(String.valueOf(menu.shrineEntity.getRadius()));

        effectPlayersButton = new Button(leftPos + 8, topPos + 99, NUMBER_BOX_WIDTH, 20,
                Component.translatable(MODID + "." + menu.shrineEntity.canEffectPlayers()), this::onBooleanClick);
        effectMonstersButton = new Button(leftPos + 8, topPos + 132, NUMBER_BOX_WIDTH, 20,
                Component.translatable(MODID + "." + menu.shrineEntity.canEffectMonsters()), this::onBooleanClick);
        replenishButton = new Button(leftPos + 8, topPos + 166, NUMBER_BOX_WIDTH, 20,
                Component.translatable(MODID + "." + menu.shrineEntity.canReplenish()), this::onBooleanClick);
        suggestions = new ArrayList<>();
        icon = menu.shrineEntity.getIcon();
    }

    @Override
    protected void alignWidgets() {
        effectBox.x = leftPos + 8;
        effectBox.y = topPos + 32;
        amplifierBox.x = leftPos + 243;
        amplifierBox.y = topPos + 32;
        maxDurationBox.x = leftPos + 63;
        maxDurationBox.y = topPos + 66;
        resetCooldownButton.x = leftPos + 125;
        resetCooldownButton.y = topPos + 65;
        resetDurationButton.x = leftPos + 8;
        resetDurationButton.y = topPos + 65;
        saveButton.x = leftPos + 222;
        saveButton.y = topPos + 166;
        blockNbtButton.x = leftPos + 82;
        blockNbtButton.y = topPos + 166;
        itemNbtButton.x = leftPos + 148;
        itemNbtButton.y = topPos + 166;
        maxCooldownBox.x = leftPos + 180;
        maxCooldownBox.y = topPos + 66;
        radiusBox.x = leftPos + 243;
        radiusBox.y = topPos + 66;
        effectPlayersButton.x = leftPos + 8;
        effectPlayersButton.y = topPos + 99;
        effectMonstersButton.x = leftPos + 8;
        effectMonstersButton.y = topPos + 132;
        replenishButton.x = leftPos + 8;
        replenishButton.y = topPos + 166;
    }

    @Override
    protected void addWidgets() {
        addRenderableWidget(effectBox);
        addRenderableWidget(amplifierBox);
        addRenderableWidget(maxDurationBox);
        addRenderableWidget(resetCooldownButton);
        addRenderableWidget(resetDurationButton);
        addRenderableWidget(maxCooldownBox);
        addRenderableWidget(radiusBox);
        addRenderableWidget(saveButton);
        addRenderableWidget(replenishButton);
        addRenderableWidget(effectPlayersButton);
        addRenderableWidget(effectMonstersButton);
        addRenderableWidget(blockNbtButton);
        addRenderableWidget(itemNbtButton);

        addRenderableWidget(new Button(leftPos + 222, topPos + 99, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui." + MODID + ".reset"), this::onResetClick));
        addRenderableWidget(new Button(leftPos + 222, topPos + 132, NUMBER_BOX_WIDTH, 20,
                Component.translatable("gui." + MODID + ".cancel"), this::onCancelClick));
    }

    private void onCopyBlockNbtClick(Button button) {
        CompoundTag tag = new CompoundTag();
        tag.putString("effect", effectBox.getValue());
        tag.putInt("amplifier", parseInt(amplifierBox.getValue()));
        tag.putInt("duration", parseInt(maxDurationBox.getValue()) * 20);
        tag.putInt("max_cooldown", parseInt(maxCooldownBox.getValue()) * 20);
        tag.putInt("radius", parseInt(radiusBox.getValue()));
        tag.putInt("remaining_cooldown", parseInt(resetCooldownButton.getMessage().getString()) * 20);
        tag.putBoolean("players", parseBoolean(effectPlayersButton.getMessage().getString()));
        tag.putBoolean("monsters", parseBoolean(effectMonstersButton.getMessage().getString()));
        tag.putBoolean("replenish", parseBoolean(replenishButton.getMessage().getString()));
        tag.putString("icon", icon);

        Minecraft.getInstance().keyboardHandler.setClipboard(tag.getAsString());
    }

    private void onCopyItemNbtClick(Button button){
        CompoundTag tag = new CompoundTag();
        tag.putString("effect", effectBox.getValue());
        tag.putInt("amplifier", parseInt(amplifierBox.getValue()));
        tag.putInt("duration", parseInt(maxDurationBox.getValue()) * 20);
        tag.putInt("max_cooldown", parseInt(maxCooldownBox.getValue()) * 20);
        tag.putInt("radius", parseInt(radiusBox.getValue()));
        tag.putInt("remaining_cooldown", parseInt(resetCooldownButton.getMessage().getString()) * 20);
        tag.putBoolean("players", parseBoolean(effectPlayersButton.getMessage().getString()));
        tag.putBoolean("monsters", parseBoolean(effectMonstersButton.getMessage().getString()));
        tag.putBoolean("replenish", parseBoolean(replenishButton.getMessage().getString()));
        tag.putString("icon", icon);
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("BlockEntityTag", tag);

        Minecraft.getInstance().keyboardHandler.setClipboard(compoundTag.getAsString());
    }
    @Override
    protected void onCooldownClick(Button button) {
        menu.resetCooldown();
        CHANNEL.sendToServer(new ResetCooldownPacket());
    }
    @Override
    protected void onIconClick() {
        assert Minecraft.getInstance().player != null;
        Minecraft.getInstance().setScreen(new IconSelectionScreen(
                new IconSelectionMenu(this.menu.containerId),
                Minecraft.getInstance().player.getInventory(),
                Component.literal("Icon Selection")).withReturnScreen(this));
    }
    protected void onResetClick(Button button) {
        effectBox.setValue(menu.shrineEntity.getEffect());
        amplifierBox.setValue(String.valueOf(menu.shrineEntity.getAmplifier()));
        maxDurationBox.setValue(String.valueOf(menu.shrineEntity.getMaxDuration() / 20));
        maxCooldownBox.setValue(String.valueOf(menu.shrineEntity.getMaxCooldown() / 20));
        radiusBox.setValue(String.valueOf(menu.shrineEntity.getRadius()));
        icon = menu.shrineEntity.getIcon();
        effectPlayersButton.setMessage(Component.translatable(MODID + "." + menu.shrineEntity.canEffectPlayers()));
        effectMonstersButton.setMessage(Component.translatable(MODID + "." + menu.shrineEntity.canEffectMonsters()));
        replenishButton.setMessage(Component.translatable(MODID + "." + menu.shrineEntity.canReplenish()));
    }
    private void onSaveClick(Button button) {
        if (getEffectFromString(effectBox.getValue()) == null) {
            if (suggestions.isEmpty()){
                effectInvalid = true;
                return;
            }
            effectBox.setValue(suggestions.get(0));
        }
        menu.shrineEntity.setEffect(effectBox.getValue());
        if (!amplifierBox.getValue().isEmpty())
            menu.shrineEntity.setAmplifier(parseInt(amplifierBox.getValue()));
        if (!maxDurationBox.getValue().isEmpty())
            menu.shrineEntity.setMaxDuration(parseInt(maxDurationBox.getValue()) * 20);
        if (!maxCooldownBox.getValue().isEmpty())
            menu.shrineEntity.setMaxCooldown(parseInt(maxCooldownBox.getValue()) * 20);
        if (!radiusBox.getValue().isEmpty())
            menu.shrineEntity.setRadius(parseInt(radiusBox.getValue()));
        menu.shrineEntity.setRemainingDuration(parseInt(resetDurationButton.getMessage().getString()) * 20);
        menu.shrineEntity.setRemainingCooldown(parseInt(resetCooldownButton.getMessage().getString()) * 20);
        menu.shrineEntity.setCanEffectPlayers(parseBoolean(effectPlayersButton.getMessage().getString()));
        menu.shrineEntity.setCanEffectMonsters(parseBoolean(effectMonstersButton.getMessage().getString()));
        menu.shrineEntity.setCanReplenish(parseBoolean(replenishButton.getMessage().getString()));
        menu.shrineEntity.setActive(menu.shrineEntity.isActive());
        menu.shrineEntity.setIcon(icon);
        CHANNEL.sendToServer(new SyncAuraShrinePacket(
                effectBox.getValue(),
                menu.shrineEntity.getAmplifier(),
                menu.shrineEntity.getMaxDuration(),
                menu.shrineEntity.getMaxCooldown(),
                menu.shrineEntity.getRadius(),
                menu.shrineEntity.canEffectPlayers(),
                menu.shrineEntity.canEffectMonsters(),
                menu.shrineEntity.canReplenish(),
                icon,
                menu.shrineEntity.isActive(),
                menu.shrineEntity.getRemainingDuration(),
                menu.shrineEntity.getRemainingCooldown()
        ));
        onClose();
    }

    private void onRadiusChanged(String newRadius) {
        if (!newRadius.isEmpty() && parseInt(newRadius) > 64) {
            radiusBox.setValue("64");
        }
        updateNbtValidity();
    }
    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if (suggestions.isEmpty()) {
            if (resetDurationButton.isMouseOver(mouseX, mouseY))
                renderTooltip(poseStack, Component.translatable("gui." + MODID + ".reset_cooldown"), mouseX, mouseY);
            if (isMouseOverIcon(mouseX, mouseY)) {
                poseStack.translate(0, 0, 1);
                hLine(poseStack, leftPos + 120, leftPos + 171, topPos + 100, 0xFF80ff80);
                hLine(poseStack, leftPos + 120, leftPos + 171, topPos + 151, 0xFF80ff80);
                vLine(poseStack, leftPos + 120, topPos + 100, topPos + 151, 0xFF80ff80);
                vLine(poseStack, leftPos + 171, topPos + 100, topPos + 151, 0xFF80ff80);
            }
        }
        RenderSystem.disableDepthTest();
    }
    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        font.draw(poseStack, title, 47 - font.width(title.getVisualOrderText()) * 0.5f, titleLabelY, 4210752);
        font.draw(poseStack, Component.translatable("gui." + MODID + ".effect").append(Component.translatable("gui." + MODID + ".tab_hint")), titleLabelX, 22, 4210752);
        font.draw(poseStack, Component.translatable("gui." + MODID + ".amplifier"), 243, 22, 4210752);
        font.draw(poseStack, Component.translatable("gui." + MODID + ".duration"), 9, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip." + MODID + ".cooldown"), 126, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip." + MODID + ".radius"), 243, 56, 4210752);
        font.draw(poseStack, Component.translatable("tooltip." + MODID + ".players"), 9, 90, 4210752);
        font.draw(poseStack, Component.translatable("tooltip." + MODID + ".monsters"), 9, 123, 4210752);
        font.draw(poseStack, Component.translatable("tooltip." + MODID + ".replenish"), 9, 157, 4210752);
        font.draw(poseStack, Component.translatable("tooltip." + MODID + ".icon"), 120, 90, 4210752);
    }
    @Override
    protected boolean isMouseOverIcon(int mouseX, int mouseY) {
        return mouseX > leftPos + 119 && mouseX < leftPos + 172 && mouseY > topPos + 100 && mouseY < topPos + 153;
    }

    @Override
    public AuraShrineScreen withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    @Override public String getIcon() {return icon;}
}
