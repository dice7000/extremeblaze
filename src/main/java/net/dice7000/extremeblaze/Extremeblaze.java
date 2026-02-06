package net.dice7000.extremeblaze;

import net.dice7000.extremeblaze.client.entity.EBRenderer;
import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Extremeblaze.MOD_ID)
public class Extremeblaze {
    public static final String MOD_ID = "extremeblaze";
    public Extremeblaze(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        EBRegistry.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(EBRegistry.VCOOLBUCKET);
            event.accept(EBRegistry.VHOTSWORD);
            event.accept(EBRegistry.VHOTHELMET);
            event.accept(EBRegistry.VHOTCHEST);
            event.accept(EBRegistry.VHOTLEGGINGS);
            event.accept(EBRegistry.VHOTBOOTS);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EBRegistry.VHOTFRAGMENT);
            event.accept(EBRegistry.VHOTINGOT);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(EBRegistry.SPAWNEGG);
        }
    }
    @SubscribeEvent public void onServerStarting(ServerStartingEvent event) {
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EBRegistry.EXTREME_BLAZE_ENTITY.get(), EBRenderer::new);
        }
    }
}
