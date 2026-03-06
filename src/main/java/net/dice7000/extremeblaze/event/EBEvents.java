package net.dice7000.extremeblaze.event;

import net.dice7000.extremeblaze.Extremeblaze;
import net.dice7000.extremeblaze.entity.ExtremeBlazeEntity;
import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class EBEvents {
    @Mod.EventBusSubscriber(modid = Extremeblaze.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EBModBusEvent {
        @SubscribeEvent public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(EBRegistry.EXTREME_BLAZE_ENTITY.get(), ExtremeBlazeEntity.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = Extremeblaze.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EBForgeBusEvent {
        public static boolean fullSet = false;
        @SubscribeEvent public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            Player player = event.player;
            if (player.level().isClientSide()) return;

            fullSet =
                    isWearing(player, EquipmentSlot.HEAD, EBRegistry.VHOTHELMET.get()) &&
                    isWearing(player, EquipmentSlot.CHEST, EBRegistry.VHOTCHEST.get()) &&
                    isWearing(player, EquipmentSlot.LEGS, EBRegistry.VHOTLEGGINGS.get()) &&
                    isWearing(player, EquipmentSlot.FEET, EBRegistry.VHOTBOOTS.get());
            float damage =
                    (float) (countItem(player, EBRegistry.VHOTFRAGMENT.get()) + (countItem(player, EBRegistry.VHOTINGOT.get()) * 4)) / 100;
            if (damage > 0 && !fullSet) {
                player.hurt(EBRegistry.ebAttack((ServerLevel) player.level()), damage);
            }
        }

        private static boolean isWearing(Player player, EquipmentSlot slot, Item item) {
            return player.getItemBySlot(slot).is(item);
        }

        private static int countItem(Player player, Item item) {
            int total = 0;
            for (ItemStack stack : player.getInventory().items) {
                if (stack.is(item)) {
                    total += stack.getCount();
                }
            }
            return total;
        }

        @SubscribeEvent public static void onLivingHurt(LivingHurtEvent event) {
            DamageSource pSource = event.getSource();
            LivingEntity target = event.getEntity();
            if (fullSet && pSource.is(EBRegistry.EB_ATTACK)) {
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                event.setCanceled(true);
            }
        }
        @SubscribeEvent public static void onLivingAttack(LivingAttackEvent event) {
            DamageSource pSource = event.getSource();
            LivingEntity target = event.getEntity();
            if (fullSet && pSource.is(EBRegistry.EB_ATTACK)) {
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                event.setCanceled(true);
            }
        }
    }
}
