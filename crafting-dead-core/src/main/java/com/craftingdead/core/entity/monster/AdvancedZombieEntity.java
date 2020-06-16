package com.craftingdead.core.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.entity.ai.FollowAttractiveGrenadeGoal;
import com.craftingdead.core.entity.ai.LookAtEntityGoal;
import com.craftingdead.core.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.inventory.InventorySlotType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class AdvancedZombieEntity extends ZombieEntity implements IRangedAttackMob {

  private static final DataParameter<Integer> TEXTURE_NUMBER =
      EntityDataManager.createKey(AdvancedZombieEntity.class, DataSerializers.VARINT);

  private final IItemProvider heldItem;
  private final IItemProvider clothingItem;
  private final IItemProvider hatItem;

  private RangedAttackGoal rangedAttackGoal;

  private long triggerPressedStartTime;

  public AdvancedZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    this(type, world, null, null, null);
  }

  public AdvancedZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world,
      IItemProvider mainHandItem, IItemProvider clothingItem, IItemProvider hatItem) {
    super(type, world);
    this.heldItem = mainHandItem;
    this.clothingItem = clothingItem;
    this.hatItem = hatItem;
  }

  public AdvancedZombieEntity(World world, IItemProvider heldItem, IItemProvider clothingItem,
      IItemProvider hatItem) {
    this(ModEntityTypes.advancedZombie, world, heldItem, clothingItem, hatItem);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 40, 20F);
    this.goalSelector.addGoal(2, this.rangedAttackGoal);
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
  public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty,
      SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
    spawnData = super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    this.dataManager.set(TEXTURE_NUMBER, this.rand.nextInt(23));
    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(this.heldItem));
    this.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      living
          .setStackInSlot(InventorySlotType.CLOTHING.getIndex(), new ItemStack(this.clothingItem));
      living.setStackInSlot(InventorySlotType.HAT.getIndex(), new ItemStack(this.hatItem));
    });
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

  @Override
  public void tick() {
    super.tick();
    if (!this.getEntityWorld().isRemote()) {
      this.getHeldItemMainhand().getCapability(ModCapabilities.GUN).ifPresent(gun -> {
        if (gun.getMagazineSize() == 0) {
          this
              .setHeldItem(Hand.OFF_HAND,
                  new ItemStack(gun.getAcceptedMagazines().stream().findAny().get()));
        }
        if (gun.isTriggerPressed()
            && (!this.rangedAttackGoal.shouldContinueExecuting() || (Util.milliTime()
                - this.triggerPressedStartTime > 1000 + this.rand.nextInt(2000)))) {
          gun.setTriggerPressed(this, this.getHeldItemMainhand(), false, true);
        }
      });
    }
  }

  @Override
  public void attackEntityWithRangedAttack(LivingEntity livingEntity, float distance) {
    if (!this.getEntityWorld().isRemote()) {
      this.getHeldItemMainhand().getCapability(ModCapabilities.GUN).ifPresent(gun -> {
        this.triggerPressedStartTime = Util.milliTime();
        gun.setTriggerPressed(this, this.getHeldItemMainhand(), true, true);
      });
    }
  }
}
