package net.dice7000.extremeblaze.registry;

import net.dice7000.extremeblaze.Extremeblaze;
import net.dice7000.extremeblaze.entity.ExtremeBlazeEntity;
import net.dice7000.extremeblaze.item.SuperCoolBucketItem;
import net.dice7000.extremeblaze.item.SuperHotArmorItem;
import net.dice7000.extremeblaze.item.SuperHotItem;
import net.dice7000.extremeblaze.item.SuperHotSwordItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.function.Supplier;

import static net.minecraft.world.item.ArmorItem.Type.*;

public class EBRegistry {
    public static final DeferredRegister<Item> EB_ITEM;
    public static final DeferredRegister<EntityType<?>> EB_ENTITY;
    static {
        EB_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, Extremeblaze.MOD_ID);
        EB_ENTITY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Extremeblaze.MOD_ID);
    }

    public static final RegistryObject<Item> VCOOLBUCKET;
    public static final RegistryObject<Item> VHOTFRAGMENT;
    public static final RegistryObject<Item> VHOTINGOT;
    public static final RegistryObject<Item> VHOTSWORD;
    public static final RegistryObject<Item> VHOTHELMET;
    public static final RegistryObject<Item> VHOTCHEST;
    public static final RegistryObject<Item> VHOTLEGGINGS;
    public static final RegistryObject<Item> VHOTBOOTS;
    static {
        VCOOLBUCKET  = EB_ITEM.register("super_cool_water_bucket",           SuperCoolBucketItem::new);
        VHOTFRAGMENT = EB_ITEM.register("super_hot_fragment"     ,           SuperHotItem::new);
        VHOTINGOT    = EB_ITEM.register("super_hot_ingot"        ,           SuperHotItem::new);
        VHOTSWORD    = EB_ITEM.register("super_hot_sword"        ,           SuperHotSwordItem::new);
        VHOTHELMET   = EB_ITEM.register("super_hot_helmet"       , () -> new SuperHotArmorItem(HELMET));
        VHOTCHEST    = EB_ITEM.register("super_hot_chestplate"   , () -> new SuperHotArmorItem(CHESTPLATE));
        VHOTLEGGINGS = EB_ITEM.register("super_hot_leggings"     , () -> new SuperHotArmorItem(LEGGINGS));
        VHOTBOOTS    = EB_ITEM.register("super_hot_boots"        , () -> new SuperHotArmorItem(BOOTS));
    }

    public static final RegistryObject<EntityType<ExtremeBlazeEntity>> EXTREME_BLAZE_ENTITY;
    static {
        EXTREME_BLAZE_ENTITY = EB_ENTITY.register("extreme_blaze", () -> EntityType.Builder.of
                (ExtremeBlazeEntity::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("extreme_blaze"));
    }

    public static void register(IEventBus eventBus) {
        EB_ITEM.register(eventBus);
        EB_ENTITY.register(eventBus);
    }

    public static final ResourceKey<DamageType> EB_ATTACK = ResourceKey.create
            (Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Extremeblaze.MOD_ID, "ebattack"));
    public static DamageSource ebAttack(ServerLevel level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(EB_ATTACK));
    }

    public enum EBArmorMaterial implements ArmorMaterial {
        SUPERHOT("super_hot", Integer.MAX_VALUE,
                new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
                Integer.MAX_VALUE, SoundEvents.ANVIL_PLACE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                () -> Ingredient.of(EBRegistry.VHOTINGOT.get()));

        private final String name;
        private final int durabilityMultiplier;
        private final int[] protectionAmount;
        private final int enchantmentValue;
        private final SoundEvent sound;
        private final float toughness;
        private final float knockbackResistance;
        private final Supplier<Ingredient> repairIngredient;
        private static final int[] baseDurability = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

        EBArmorMaterial(String pName, int pDurabilityMultiplier, int[] pProtectionAmount, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
            this.name = pName;
            this.durabilityMultiplier = pDurabilityMultiplier;
            this.protectionAmount = pProtectionAmount;
            this.enchantmentValue = pEnchantmentValue;
            this.sound = pSound;
            this.toughness = pToughness;
            this.knockbackResistance = pKnockbackResistance;
            this.repairIngredient = pRepairIngredient;
        }

        @Override public int getDurabilityForType(ArmorItem.Type pType) {
            return baseDurability[pType.ordinal()] * durabilityMultiplier;
        }
        @Override public int getDefenseForType(ArmorItem.Type pType) {
            return protectionAmount[pType.ordinal()];
        }
        @Override public int getEnchantmentValue() {
            return enchantmentValue;
        }
        @Override public SoundEvent getEquipSound() {
            return sound;
        }
        @Override public Ingredient getRepairIngredient() {
            return repairIngredient.get();
        }
        @Override public String getName() {
            return Extremeblaze.MOD_ID + ":" + name;
        }
        @Override public float getToughness() {
            return toughness;
        }
        @Override public float getKnockbackResistance() {
            return knockbackResistance;
        }
    }
}
