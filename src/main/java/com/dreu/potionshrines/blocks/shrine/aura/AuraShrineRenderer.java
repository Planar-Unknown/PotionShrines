package com.dreu.potionshrines.blocks.shrine.aura;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static com.dreu.potionshrines.PotionShrines.MODID;
import static com.dreu.potionshrines.PotionShrines.getBakedIconOrDefault;

@SuppressWarnings("DataFlowIssue")
public class AuraShrineRenderer implements BlockEntityRenderer<AuraShrineBlockEntity>{
    private static final ResourceLocation RECHARGE_PNG = new ResourceLocation(MODID, "textures/block/aura_recharging.png");
    public AuraShrineRenderer(){}
    @Override @ParametersAreNonnullByDefault
    public void render(AuraShrineBlockEntity auraShrine, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (!Objects.equals(auraShrine.getEffect(), "null")) {
            int cooldown = auraShrine.getRemainingCooldown();
            int remainingDuration = auraShrine.getRemainingDuration();
            float uvY = auraShrine.canReplenish() ? 1f - (float) auraShrine.getRemainingCooldown() / auraShrine.getMaxCooldown() : 0;

            if (cooldown == 0) {
                uvY = 1;
                renderIcon(poseStack, auraShrine, 1, partialTicks, combinedOverlay, bufferSource);
            } else if (cooldown < 40) {
                //Animation on replenish
                renderReplenishAnim(auraShrine, partialTicks, poseStack, bufferSource, combinedOverlay, cooldown);
            } else if (auraShrine.isActive()) {
                uvY = (float) remainingDuration / auraShrine.getMaxDuration();
                if (remainingDuration < 20){
                    renderFadeAnim(poseStack, auraShrine, 20 - remainingDuration, partialTicks, combinedOverlay, bufferSource);
                } else renderIcon(poseStack, auraShrine, 20, partialTicks, combinedOverlay, bufferSource);
            }
            renderCharge(poseStack, uvY);
        }
    }

    private void renderFadeAnim(PoseStack poseStack, AuraShrineBlockEntity auraShrine, int cooldown, float partialTicks, int combinedOverlay, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.translate(0.5, cooldown * 0.04 + 0.1, 0.5);
        poseStack.translate(0, Math.sin((auraShrine.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0);
        float normalizedCooldown = cooldown / 19.0f;
        poseStack.scale(1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(((auraShrine.getLevel().getGameTime() + partialTicks) - 1800 * normalizedCooldown * normalizedCooldown) % 360));  // Apply rotation around the Y-axis

        RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
        RenderSystem.enableDepthTest();

        poseStack.scale(0.88889f, 0.88889f, 0.88889f);
        poseStack.translate(-0.5, 0, -0.5);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderModelLists(getBakedIconOrDefault(auraShrine.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

        poseStack.popPose();
    }

    private void renderIcon(PoseStack poseStack, AuraShrineBlockEntity auraShrine, int spinSpeed, float partialTicks, int combinedOverlay, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.translate(0.5, Math.sin((auraShrine.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees((auraShrine.getLevel().getGameTime() + partialTicks) * spinSpeed % 360));

        RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
        RenderSystem.enableDepthTest();

        poseStack.scale(0.88889f, 0.88889f, 0.88889f);
        poseStack.translate(-0.5, 0.2, -0.5);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderModelLists(getBakedIconOrDefault(auraShrine.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

        poseStack.popPose();
    }

    private static void renderReplenishAnim(AuraShrineBlockEntity auraShrine, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedOverlay, float cooldown) {
        poseStack.pushPose();
        poseStack.translate(0.5, -cooldown * 0.01, 0.5);
        poseStack.translate(0, Math.sin((auraShrine.getLevel().getGameTime() + partialTicks) * 0.05) * 0.1, 0);
        float normalizedCooldown = cooldown / 39.0f;
        poseStack.scale(1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown, 1 - normalizedCooldown * normalizedCooldown);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(((auraShrine.getLevel().getGameTime() + partialTicks) - 3600 * normalizedCooldown * normalizedCooldown) % 360));  // Apply rotation around the Y-axis

        RenderSystem.setShader(GameRenderer::getRendertypeItemEntityTranslucentCullShader);
        RenderSystem.enableDepthTest();

        poseStack.scale(0.88889f, 0.88889f, 0.88889f);
        poseStack.translate(-0.5, 0, -0.5);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderModelLists(getBakedIconOrDefault(auraShrine.getIcon()), ItemStack.EMPTY, 0xF000F0, combinedOverlay, poseStack, bufferSource.getBuffer(RenderType.cutout()));

        poseStack.popPose();
    }

    private static void renderCharge(PoseStack poseStack, float uvY) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, RECHARGE_PNG);
        poseStack.pushPose();

        poseStack.translate(0.5, -1.44875, 0.5);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        for (int i = 0; i < 4; i++) {
            buffer.vertex(poseStack.last().pose(), -0.0625f, 0.0f, 0.064f).uv(0, 1).endVertex();
            buffer.vertex(poseStack.last().pose(), 0.0625f, 0.0f, 0.064f).uv(1, 1).endVertex();
            buffer.vertex(poseStack.last().pose(), 0.0625f, 0.875f * uvY, 0.064f).uv(1, 1 - uvY).endVertex();
            buffer.vertex(poseStack.last().pose(), -0.0625f, 0.875f * uvY, 0.064f).uv(0, 1 - uvY).endVertex();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        }
        Tesselator.getInstance().end();

        poseStack.popPose();
    }
}