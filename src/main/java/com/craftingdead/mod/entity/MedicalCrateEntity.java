package com.craftingdead.mod.entity;

import com.craftingdead.mod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class MedicalCrateEntity extends AirDropEntity {

  public MedicalCrateEntity(EntityType<?> entityEntityType, World world) {
    super(ModEntityTypes.medicalCrateEntity, world);
  }

  public MedicalCrateEntity(FMLPlayMessages.SpawnEntity packet, World world) {
    this(ModEntityTypes.medicalCrateEntity, world);
  }

  public MedicalCrateEntity(World world, double x, double y, double z) {
    super(ModEntityTypes.medicalCrateEntity, x, y, z, world);
  }

  @Override
  public void randomLoot() {
    this.addRandomItemWithChance(ModItems.backpackGunBag, 45, 1);
    this.addRandomItemWithChance(ModItems.orange, 45, 3);
    this.addRandomItemWithChance(ModItems.apple, 45, 2);
    this.addRandomItemWithChance(ModItems.antibiotics, 45, 1);
  }
}
