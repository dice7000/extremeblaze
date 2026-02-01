package net.dice7000.extremeblaze.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dice7000.extremeblaze.Extremeblaze;
import net.dice7000.extremeblaze.entity.ExtremeBlazeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EBRenderer extends MobRenderer<ExtremeBlazeEntity, ExtremeBlazeModel<ExtremeBlazeEntity>> {
    public EBRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ExtremeBlazeModel<>(pContext.bakeLayer(ExtremeBlazeModel.LAYER_LOCATION)), 1.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ExtremeBlazeEntity pEntity) {
        return Objects.requireNonNull(ResourceLocation.tryBuild(Extremeblaze.MOD_ID, "textures/entity/ebtex.png"));
    }

    @Override
    public void render(ExtremeBlazeEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
