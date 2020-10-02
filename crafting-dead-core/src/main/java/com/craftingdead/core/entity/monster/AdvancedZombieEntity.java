/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.core.entity.monster;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.entity.ai.FollowAttractiveGrenadeGoal;
import com.craftingdead.core.entity.ai.LookAtEntityGoal;
import com.craftingdead.core.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.ClothingItem;
import com.craftingdead.core.item.HatItem;
import com.craftingdead.core.item.MeleeWeaponItem;
import com.craftingdead.core.item.ModItems;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class AdvancedZombieEntity extends ZombieEntity implements IRangedAttackMob {

  private static final float MELEE_CHANCE = 0.15F;
  private static final float CLOTHING_CHANCE = 0.25F;
  private static final float HAT_CHANCE = 0.05F;

  private static final DataParameter<Integer> TEXTURE_NUMBER =
      EntityDataManager.createKey(AdvancedZombieEntity.class, DataSerializers.VARINT);

  private RangedAttackGoal rangedAttackGoal;

  private long triggerPressedStartTime;

  public AdvancedZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 40, 20F);
    this.goalSelector.addGoal(3, this.rangedAttackGoal);
    this.goalSelector.addGoal(1, new FollowAttractiveGrenadeGoal(this, 1.15F));
    this.goalSelector
        .addGoal(4,
            new LookAtEntityGoal<FlashGrenadeEntity>(this, FlashGrenadeEntity.class, 20.0F, 0.35F));
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
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    this.dataManager.set(TEXTURE_NUMBER, this.rand.nextInt(23));
    this.setItemStackToSlot(EquipmentSlotType.MAINHAND,
        new ItemStack(this.getMelee()));
    this.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      living.getItemHandler()
          .setStackInSlot(InventorySlotType.CLOTHING.getIndex(),
              new ItemStack(
                  this.getClothing()));
      living.getItemHandler().setStackInSlot(InventorySlotType.HAT.getIndex(),
          new ItemStack(this.getHat()));
    });
  }

  protected Item getMelee() {
    return this.getRandomItem(item -> item instanceof MeleeWeaponItem, MELEE_CHANCE);
  }

  protected Item getClothing() {
    return this.getRandomItem(item -> item instanceof ClothingItem, CLOTHING_CHANCE);
  }

  protected Item getHat() {
    return this.getRandomItem(item -> item instanceof HatItem, HAT_CHANCE);
  }

  protected Item getRandomItem(Predicate<Item> predicate, float probability) {
    if (this.rand.nextFloat() < probability) {
      List<Item> items = ModItems.ITEMS
          .getEntries()
          .stream()
          .map(RegistryObject::get)
          .filter(predicate)
          .collect(Collectors.toList());
      return items.get(this.rand.nextInt(items.size()));
    }
    return null;
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

  @Override
  public void tick() {
    super.tick();
    if (!this.getEntityWorld().isRemote()) {
      this.getCapability(ModCapabilities.LIVING).ifPresent(
          living -> this.getHeldItemMainhand().getCapability(ModCapabilities.GUN).ifPresent(gun -> {
            if (gun.getMagazineSize() == 0) {
              this
                  .setHeldItem(Hand.OFF_HAND,
                      new ItemStack(gun.getAcceptedMagazines().stream().findAny().get()));
            }
            if (gun.isTriggerPressed()
                && (!this.rangedAttackGoal.shouldContinueExecuting() || (Util.milliTime()
                    - this.triggerPressedStartTime > 1000 + this.rand.nextInt(2000)))) {
              gun.setTriggerPressed(living, this.getHeldItemMainhand(), false, true);
            }
          }));
    }
  }

  @Override
  public void attackEntityWithRangedAttack(LivingEntity livingEntity, float distance) {
    if (!this.getEntityWorld().isRemote()) {
      this.getCapability(ModCapabilities.LIVING).ifPresent(
          living -> this.getHeldItemMainhand().getCapability(ModCapabilities.GUN).ifPresent(gun -> {
            this.triggerPressedStartTime = Util.milliTime();
            gun.setTriggerPressed(living, this.getHeldItemMainhand(), true, true);
          }));
    }
  }
}
