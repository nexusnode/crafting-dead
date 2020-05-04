package com.craftingdead.mod.enchantment;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      new DeferredRegister<>(ForgeRegistries.ENCHANTMENTS, CraftingDead.ID);

  public static final RegistryObject<ExplosionEnchantment> EXPLOSION = ENCHANTMENTS
      .register("explosion",
          () -> new ExplosionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
  public static final RegistryObject<IncendiaryEnchantment> INCENDIARY = ENCHANTMENTS
      .register("incendiary",
          () -> new IncendiaryEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
  public static final RegistryObject<InfectionEnchantment> INFECTION = ENCHANTMENTS
      .register("infection",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
}
