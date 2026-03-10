package net.dice7000.extremeblaze.mixin.mixin;

import net.dice7000.extremeblaze.mixin.method.EBMixinMethod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements EBMixinMethod {
    @Shadow private Vec3 deltaMovement;
    @Unique private final Entity extremeblaze$self = (Entity) (Object) this;
    @Unique private static final EntityDataAccessor<Boolean> DATA_EXTREME_IMPACT = SynchedEntityData.defineId
            (Entity.class, EntityDataSerializers.BOOLEAN);

    @Override public void extremeblaze$setDataExtremeImpact(boolean value) {
        if (value && extremeblaze$impactCooldown > 0) return;
        extremeblaze$self.getEntityData().set(DATA_EXTREME_IMPACT, value);
    }
    @Override public boolean extremeblaze$getDataExtremeImpact() {
        return extremeblaze$self.getEntityData().get(DATA_EXTREME_IMPACT);
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void onLoad(CompoundTag pCompound, CallbackInfo ci) {
        if (pCompound.contains("ExtremeImpact")) {
            extremeblaze$setDataExtremeImpact(pCompound.getBoolean("ExtremeImpact"));
        }
    }

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    public void onSave(CompoundTag pCompound, CallbackInfoReturnable<CompoundTag> cir) {
        pCompound.putBoolean("ExtremeImpact", extremeblaze$getDataExtremeImpact());
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        extremeblaze$self.getEntityData().define(DATA_EXTREME_IMPACT, false);
    }
    @Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    public void EBSetDeltaMovementInject(Vec3 pDeltaMovement, CallbackInfo ci) {
        if (extremeblaze$self.getEntityData().get(DATA_EXTREME_IMPACT)) {
            deltaMovement = new Vec3(
                    Mth.clamp(pDeltaMovement.x * 0.5, 0, 1),
                    pDeltaMovement.y > 0 ? Mth.clamp(pDeltaMovement.y * 0.5, 0, 1) : pDeltaMovement.y,
                    Mth.clamp(pDeltaMovement.z * 0.5, 0, 1)
            );
            ci.cancel();
        }
    }
    @Unique private int extremeblaze$impactCooldown = 0;
    @Inject(method = "tick", at = @At("TAIL"))
    public void  EBTickInject(CallbackInfo ci) {
        if (extremeblaze$getDataExtremeImpact()) {
            if (extremeblaze$impactCooldown <= 0) {
                extremeblaze$impactCooldown = 20;
            } else {
                extremeblaze$impactCooldown--;
                if (extremeblaze$impactCooldown <= 0) {
                    extremeblaze$setDataExtremeImpact(false);
                }
            }
        }
    }
}
