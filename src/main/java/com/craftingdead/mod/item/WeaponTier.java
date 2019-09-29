package com.craftingdead.mod.item;

import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadBase;

import java.util.function.Supplier;

public enum WeaponTier implements IItemTier {

    //TODO Find out and add the properties and type of our weapons
    bad(3, 10000, 20.0F, 10.0F, 10, () -> {
        return Ingredient.fromItems(Items.DIAMOND);
    }),
    WOOD(0, 59, 2.0F, 0.0F, 15, () -> {
        return Ingredient.fromTag(ItemTags.PLANKS);
    }),
    STONE(1, 131, 4.0F, 1.0F, 5, () -> {
        return Ingredient.fromItems(Blocks.COBBLESTONE);
    }),
    IRON(2, 250, 6.0F, 2.0F, 14, () -> {
        return Ingredient.fromItems(Items.IRON_INGOT);
    }),
    DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> {
        return Ingredient.fromItems(Items.DIAMOND);
    }),
    GOLD(0, 32, 12.0F, 0.0F, 22, () -> {
        return Ingredient.fromItems(Items.GOLD_INGOT);
    });

    /**
     * Production level.
     * For example: Wood 0, Stone 1, Iron 2, Diamond 3, Gold 0
     */
    private final int harvestLevel;

    /**
     * The number of maximum applications
     */
    private final int maxUses;

    /**
     * Mining Speed, or Efficiency
     */
    private final float efficiency;

    /**
     * Attack damage
     */
    private final float attackDamage;

    /**
     * How cool is enchanted
     */
    private final int enchantability;

    /**
     *  What material is needed for repair
     */
    private final LazyLoadBase<Ingredient> repairMaterial;

    private WeaponTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadBase<>(repairMaterialIn);
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
}
