package net.dice7000.extremeblaze.entity;

import net.dice7000.extremeblaze.item.SuperCoolBucketItem;
import net.dice7000.extremeblaze.item.SuperHotSwordItem;
import net.dice7000.extremeblaze.mixinmethod.MobMixinMethod;
import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExtremeBlazeEntity extends Monster {
    private static final EntityDataAccessor<Integer> DATA_BUCKET_COUNT = SynchedEntityData.defineId(ExtremeBlazeEntity.class, EntityDataSerializers.INT);
    private int cooldown = 0;
    public final AnimationState idleAnimState = new AnimationState();
    private int idleAnimTimeout = 0;

    public ExtremeBlazeEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BUCKET_COUNT, 10);
    }

    public int getBucketCount() {
        return this.entityData.get(DATA_BUCKET_COUNT);
    }

    @Override protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 1000D)
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 1.5D)
                .add(Attributes.ATTACK_DAMAGE, 2048D)
                ;
    }

    @Override public void tick() {
        super.tick();
        if (!(cooldown <= 0)) cooldown--;
        if (!this.level().isClientSide) {
            AABB box = this.getBoundingBox().inflate(0.2);
            List<Entity> list = this.level().getEntities(this, box, e -> e instanceof LivingEntity);
            for (Entity e : list) {
                if (e instanceof LivingEntity target) {
                    target.hurt(EBRegistry.ebAttack((ServerLevel) target.level()), Float.POSITIVE_INFINITY);
                }
            }
        }
        if (level().isClientSide) {
            setupAnimState();
        }
    }

    private void setupAnimState() {
        if (idleAnimTimeout <= 0) {
            idleAnimTimeout = 20;
            idleAnimState.start(this.tickCount);
        } else {
            --idleAnimTimeout;
        }
    }

    @Override public float getHealth() {
        return getBucketCount() > 0 ? Float.POSITIVE_INFINITY : 0;
    }

    @Override public boolean isDeadOrDying() {
        return getBucketCount() <= 0;
    }

    @Override public boolean isAlive() {
        return !this.isRemoved() && getBucketCount() > 0;
    }

    @Override public boolean doHurtTarget(Entity pEntity) {
        float f = Float.POSITIVE_INFINITY;
        float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (pEntity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)pEntity).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            pEntity.setSecondsOnFire(i * 4);
        }

        boolean flag = pEntity.hurt(EBRegistry.ebAttack((ServerLevel) pEntity.level()), f);
        if (flag) {
            if (f1 > 0.0F && pEntity instanceof LivingEntity) {
                ((LivingEntity)pEntity).knockback((f1 * 0.5F), Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            if (pEntity instanceof Player player) {
                ((MobMixinMethod) this).extremeblaze$rumMDS(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
            }

            this.doEnchantDamageEffects(this, pEntity);
            this.setLastHurtMob(pEntity);
        }
        return flag;
    }

    @Override public boolean hurt(DamageSource pSource, float pAmount) {
        Entity attacker = pSource.getEntity();
        boolean sup = super.hurt(pSource, pAmount);

        if (!this.level().isClientSide && attacker instanceof LivingEntity target) {
            if (target instanceof Player player) {
                Item handover = player.getMainHandItem().getItem();
                if (handover instanceof SuperCoolBucketItem || handover instanceof SuperHotSwordItem) {
                    if (cooldown <= 0) {
                        this.entityData.set(DATA_BUCKET_COUNT, getBucketCount() - 1);
                        if (handover instanceof SuperCoolBucketItem) {
                            level().playSound(null, this.getX(), this.getY(), this.getZ(),
                                    SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 1.0F, 1.0F);
                            cooldown = 40;
                        } else {
                            level().playSound(null, this.getX(), this.getY(), this.getZ(),
                                    SoundEvents.ANVIL_PLACE, SoundSource.HOSTILE, 1.0F, 1.0F);
                        }
                    }
                    return false;
                }
            }
            if (sup) {
                target.hurt(EBRegistry.ebAttack((ServerLevel) this.level()), Float.POSITIVE_INFINITY);
                this.setLastHurtByMob(target);
                this.setTarget(target);
            }
        }
        return sup;
    }

    @Override
    public void remove(RemovalReason pReason) {
        for (int i = 0; i <= (4 + getRandom().nextInt(4)); i++) {
            ItemEntity drop = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(EBRegistry.VHOTFRAGMENT.get()));
            drop.setDeltaMovement(new Vec3(this.getX(), this.getY() + 1, this.getZ()).normalize().scale(1.0F));
            level().addFreshEntity(drop);
        }
        super.remove(pReason);
    }
}
