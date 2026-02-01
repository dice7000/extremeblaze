package net.dice7000.extremeblaze.event;

import net.dice7000.extremeblaze.Extremeblaze;
import net.dice7000.extremeblaze.entity.ExtremeBlazeEntity;
import net.dice7000.extremeblaze.mixinmethod.LivingEntityMixinMethod;
import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class EBEvents {
    @Mod.EventBusSubscriber(modid = Extremeblaze.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class EBModBusEvent {
        @SubscribeEvent public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(EBRegistry.EXTREME_BLAZE_ENTITY.get(), ExtremeBlazeEntity.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = Extremeblaze.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class EBForgeBusEvent {
        @SubscribeEvent
        public static void onLivingDrops(LivingDropsEvent event) {
            LivingEntity mob = event.getEntity();
            if (mob.level().isClientSide()) return;
            if (mob.getType() == EBRegistry.EXTREME_BLAZE_ENTITY.get()) {
                ItemStack drop = new ItemStack(EBRegistry.VHOTFRAGMENT.get());
                ItemEntity itemEntity = new ItemEntity(mob.level(), mob.getX(), mob.getY(), mob.getZ(), drop);
                event.getDrops().add(itemEntity);
            }
        }

        @SubscribeEvent public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            Player player = event.player;
            if (player.level().isClientSide()) return;

            boolean fullSet =
                    isWearing(player, EquipmentSlot.HEAD, EBRegistry.VHOTHELMET.get()) &&
                            isWearing(player, EquipmentSlot.CHEST, EBRegistry.VHOTCHEST.get()) &&
                            isWearing(player, EquipmentSlot.LEGS, EBRegistry.VHOTLEGGINGS.get()) &&
                            isWearing(player, EquipmentSlot.FEET, EBRegistry.VHOTBOOTS.get());

            Inventory inv = player.getInventory();
            boolean hasVHotItem = (inv.contains(new ItemStack(EBRegistry.VHOTFRAGMENT.get())) ||
                                  inv.contains(new ItemStack(EBRegistry.VHOTINGOT.get()))) && !fullSet;

            if (hasVHotItem) {
                player.hurt(EBRegistry.ebAttack((ServerLevel) player.level()), (float) 1 / 100);
            }
            ((LivingEntityMixinMethod) player).extremeblaze$setAllWearing(fullSet);
        }

        private static boolean isWearing(Player player, EquipmentSlot slot, Item item) {
            return player.getItemBySlot(slot).is(item);
        }
    }
}
