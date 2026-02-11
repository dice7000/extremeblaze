package net.dice7000.extremeblaze.mixin;

import net.dice7000.extremeblaze.mixinmethod.MobMixinMethod;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
public abstract class MobMixin implements MobMixinMethod {
    @Shadow protected abstract void maybeDisableShield(Player pPlayer, ItemStack pMobItemStack, ItemStack pPlayerItemStack);
    @Override public void extremeblaze$rumMDS(Player pPlayer, ItemStack pMobItemStack, ItemStack pPlayerItemStack) {
        maybeDisableShield(pPlayer, pMobItemStack, pPlayerItemStack);
    }
}
