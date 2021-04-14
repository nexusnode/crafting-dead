package com.craftingdead.virus.enchantment;

import com.craftingdead.virus.CraftingDeadVirus;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class VirusEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CraftingDeadVirus.ID);

  public static final RegistryObject<InfectionEnchantment> INFECTION =
      ENCHANTMENTS.register("infection",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
}
