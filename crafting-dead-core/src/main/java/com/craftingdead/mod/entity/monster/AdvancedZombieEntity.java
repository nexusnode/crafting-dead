package com.craftingdead.mod.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class AdvancedZombieEntity extends ZombieEntity {

  private static final DataParameter<Integer> TEXTURE_NUMBER =
      EntityDataManager.createKey(AdvancedZombieEntity.class, DataSerializers.VARINT);

  public AdvancedZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  public AdvancedZombieEntity(World world) {
    super(world);
  }

  @Override
  public boolean canSpawn(IWorld world, SpawnReason spawnReason) {
    return true;
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
  }

  @Override
  public void registerData() {
    super.registerData();
    this.getDataManager().register(TEXTURE_NUMBER, 0);
  }

  @Override
  public int getTotalArmorValue() {
    int armorValue = super.getTotalArmorValue() + 2;
    if (armorValue > 20) {
      armorValue = 20;
    }
    return armorValue;
  }

  @Override
  public int getMaxSpawnedInChunk() {
    return 12;
  }

  @Override
  protected boolean shouldBurnInDay() {
    return false;
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.dataManager.set(TEXTURE_NUMBER, compound.getInt("textureNumber"));
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putInt("textureNumber", this.dataManager.get(TEXTURE_NUMBER));
  }

  @Override
  public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty,
      SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
    spawnData = super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    this.dataManager.set(TEXTURE_NUMBER, this.rand.nextInt(23));
    return spawnData;
  }

  public int getTextureNumber() {
    return this.dataManager.get(TEXTURE_NUMBER);
  }

  /**
   * Method to be used as an arg for {@link EntitySpawnPlacementRegistry}.<code>register()</code>
   */
  public static boolean areSpawnConditionsMet(EntityType<?> entityType, IWorld world,
      SpawnReason spawnReason, BlockPos blockPos, Random random) {
    return true;
  }
}
