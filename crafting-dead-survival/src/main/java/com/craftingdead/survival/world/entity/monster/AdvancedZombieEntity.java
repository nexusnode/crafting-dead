/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.survival.world.entity.monster;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.entity.ai.FollowAttractiveGrenadeGoal;
import com.craftingdead.core.world.entity.ai.LookAtEntityGoal;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.item.ClothingItem;
import com.craftingdead.core.world.item.HatItem;
import com.craftingdead.core.world.item.MeleeWeaponItem;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
      EntityDataManager.defineId(AdvancedZombieEntity.class, DataSerializers.INT);

  private RangedAttackGoal rangedAttackGoal;

  private long triggerPressedStartTime;

  public AdvancedZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 40, 20F) {
      @Override
      public boolean canUse() {
        return super.canUse()
            && AdvancedZombieEntity.this.getCapability(Capabilities.LIVING)
                .map(living -> AdvancedZombieEntity.this.getMainHandItem()
                    .getCapability(Capabilities.GUN)
                    .isPresent())
                .orElse(false);
      }
    };
    this.goalSelector.addGoal(2, this.rangedAttackGoal);
    this.goalSelector.addGoal(1, new FollowAttractiveGrenadeGoal(this, 1.15F));
    this.goalSelector.addGoal(4,
        new LookAtEntityGoal<>(this, FlashGrenadeEntity.class, 20.0F, 0.35F));
  }

  @Override
  public boolean checkSpawnRules(IWorld world, SpawnReason spawnReason) {
    return true;
  }

  public static AttributeModifierMap.MutableAttribute registerAttributes() {
    return ZombieEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D);
  }

  @Override
  public void defineSynchedData() {
    super.defineSynchedData();
    this.getEntityData().define(TEXTURE_NUMBER, 0);
  }

  @Override
  public int getArmorValue() {
    int armorValue = super.getArmorValue() + 2;
    if (armorValue > 20) {
      armorValue = 20;
    }
    return armorValue;
  }

  @Override
  public int getMaxSpawnClusterSize() {
    return 12;
  }

  @Override
  protected boolean isSunSensitive() {
    return false;
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT compound) {
    super.readAdditionalSaveData(compound);
    this.entityData.set(TEXTURE_NUMBER, compound.getInt("textureNumber"));
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("textureNumber", this.entityData.get(TEXTURE_NUMBER));
  }

  @Override
  protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
    this.entityData.set(TEXTURE_NUMBER, this.random.nextInt(23));
    this.setItemSlot(EquipmentSlotType.MAINHAND, this.getHeldStack());
    this.getCapability(Capabilities.LIVING).ifPresent(living -> {
      living.getItemHandler().setStackInSlot(ModEquipmentSlotType.CLOTHING.getIndex(),
          this.getClothingStack());
      living.getItemHandler().setStackInSlot(ModEquipmentSlotType.HAT.getIndex(),
          this.getHatStack());
    });
  }

  protected ItemStack getHeldStack() {
    return this.getRandomItem(item -> item instanceof MeleeWeaponItem, MELEE_CHANCE)
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  protected ItemStack getClothingStack() {
    return this.getRandomItem(item -> item instanceof ClothingItem, CLOTHING_CHANCE)
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  protected ItemStack getHatStack() {
    return this.getRandomItem(item -> item instanceof HatItem, HAT_CHANCE)
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  protected Optional<Item> getRandomItem(Predicate<Item> predicate, float probability) {
    if (this.random.nextFloat() < probability) {
      List<Item> items = ModItems.ITEMS
          .getEntries()
          .stream()
          .map(RegistryObject::get)
          .filter(predicate)
          .collect(Collectors.toList());
      return Optional.of(items.get(this.random.nextInt(items.size())));
    }
    return Optional.empty();
  }

  public int getTextureNumber() {
    return this.entityData.get(TEXTURE_NUMBER);
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
    if (!this.getCommandSenderWorld().isClientSide()) {
      this.getCapability(Capabilities.LIVING).ifPresent(
          living -> this.getMainHandItem().getCapability(Capabilities.GUN).ifPresent(gun -> {
            if (gun.isTriggerPressed()
                && (!this.rangedAttackGoal.canContinueToUse() || (Util.getMillis()
                    - this.triggerPressedStartTime > 1000 + this.random.nextInt(2000)))) {
              gun.setTriggerPressed(living, false, true);
            }
          }));
    }
  }

  @Override
  public void performRangedAttack(LivingEntity livingEntity, float distance) {
    if (!this.level.isClientSide()) {
      this.getCapability(Capabilities.LIVING).ifPresent(
          living -> this.getMainHandItem().getCapability(Capabilities.GUN).ifPresent(gun -> {
            this.triggerPressedStartTime = Util.getMillis();
            gun.setTriggerPressed(living, true, true);
          }));
    }
  }
}
