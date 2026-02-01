package net.dice7000.extremeblaze.mixinmethod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface MobMixinMethod {
    void extremeblaze$rumMDS(Player pPlayer, ItemStack pMobItemStack, ItemStack pPlayerItemStack);
}
