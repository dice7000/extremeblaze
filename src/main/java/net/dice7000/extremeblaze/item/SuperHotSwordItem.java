package net.dice7000.extremeblaze.item;

import net.dice7000.extremeblaze.entity.ExtremeBlazeEntity;
import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class SuperHotSwordItem extends SwordItem {
    public SuperHotSwordItem() {
        super(Tiers.NETHERITE, Integer.MAX_VALUE, Integer.MAX_VALUE, new Properties());
    }

    @Override public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide) {
            if (!(pTarget instanceof ExtremeBlazeEntity)) {
                pTarget.hurt(EBRegistry.ebAttack((ServerLevel) pTarget.level()), Float.POSITIVE_INFINITY);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
