package net.dice7000.extremeblaze.mixin;

import net.dice7000.extremeblaze.entity.ExtremeBlazeEntity;
import net.dice7000.extremeblaze.mixinmethod.LivingEntityMixinMethod;
import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityMixinMethod {
    @Unique LivingEntity extremeblaze$targetClass = ((LivingEntity) (Object) this);
    @Unique public boolean extremeblaze$isAllWearing = false;
    @Override
    public void extremeblaze$setAllWearing(boolean value) {
        extremeblaze$isAllWearing = value;
    }
    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    public void getMaxHealthInject(CallbackInfoReturnable<Float> cir) {
        if (extremeblaze$targetClass instanceof ExtremeBlazeEntity) {
            cir.setReturnValue(Float.POSITIVE_INFINITY);
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void hurtInject(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        if (pSource.is(EBRegistry.EB_ATTACK)) {
            ((LivingEntity) (Object) this).invulnerableTime = 0;
            if (extremeblaze$isAllWearing) {
                ((LivingEntity) (Object) this).level().playSound(null,
                        extremeblaze$targetClass.getX(),
                        extremeblaze$targetClass.getY(),
                        extremeblaze$targetClass.getZ(),
                        SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                cir.setReturnValue(false);
            }
        }
    }

    /*
    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    public void setHealthInject(float pHealth, CallbackInfo ci) {
        //
    }

     */
}
