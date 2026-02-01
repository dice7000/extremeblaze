package net.dice7000.extremeblaze.client.event;

import net.dice7000.extremeblaze.Extremeblaze;
import net.dice7000.extremeblaze.client.entity.ExtremeBlazeModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Extremeblaze.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EBClientEvent {
    @SubscribeEvent public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ExtremeBlazeModel.LAYER_LOCATION, ExtremeBlazeModel::createBodyLayer);
    }
}
