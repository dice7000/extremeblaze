package net.dice7000.extremeblaze.item;

import net.dice7000.extremeblaze.registry.EBRegistry;
import net.minecraft.world.item.ArmorItem;

public class SuperHotArmorItem extends ArmorItem {
    public SuperHotArmorItem(Type pType) {
        super(EBRegistry.EBArmorMaterial.SUPERHOT, pType, new Properties());
    }
}
