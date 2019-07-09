package com.craftingdead.mod.item;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class GunItem extends Item {

  private static final UUID REACH_DISTANCE_MODIFIER =
      UUID.fromString("A625D496-9464-4891-9E1F-9345989E5DAE");

  /**
   * Time between shots in milliseconds.
   */
  @Getter
  private final int fireRate;

  @Getter
  private final int clipSize;

  @Getter
  private final int damage;

  @Getter
  private final float reloadTime;

  /**
   * Accuracy as percentage.
   */
  @Getter
  private final float accuracy;

  /**
   * A {@link List} of {@link IFireMode}s the gun can cycle through.
   */
  @Getter
  private final List<Supplier<IFireMode>> fireModes;

  @Getter
  private final Supplier<SoundEvent> shootSound;

  @Getter
  private final Map<GunAnimation.Type, Supplier<GunAnimation>> animations;

  public GunItem(Properties properties) {
    super(properties);
    this.fireRate = properties.fireRate;
    this.clipSize = properties.clipSize;
    this.damage = properties.damage;
    this.reloadTime = properties.reloadTime;
    this.accuracy = properties.accuracy;
    this.fireModes = properties.fireModes;
    this.shootSound = properties.shootSound;
    this.animations = properties.animations;
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(
      EquipmentSlotType equipmentSlot) {
    @SuppressWarnings("deprecation")
    Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(equipmentSlot);
    if (equipmentSlot == EquipmentSlotType.MAINHAND) {
      // Add 20 attack speed to remove cooldown
      modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(
          ATTACK_SPEED_MODIFIER, "Weapon modifier", 20, AttributeModifier.Operation.ADDITION));
      modifiers.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(
          REACH_DISTANCE_MODIFIER, "Weapon modofier", 100, AttributeModifier.Operation.ADDITION));
    }
    return modifiers;
  }

  @Override
  public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
    // Return true to stop swing animation on shoot
    return true;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
    return true;
  }

  @Override
  public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos,
      PlayerEntity player) {
    return false;
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    return true;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new ICapabilityProvider() {
      private final GunController gunController = new GunController(GunItem.this);

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return (cap == ModCapabilities.TRIGGERABLE || cap == ModCapabilities.AIMABLE)
            ? LazyOptional.of(() -> this.gunController).cast()
            : LazyOptional.empty();
      }
    };
  }

  public static class Properties extends Item.Properties {

    private Integer fireRate;

    private Integer clipSize;

    private Integer damage;

    private Float reloadTime;

    private Float accuracy;

    private List<Supplier<IFireMode>> fireModes;

    private Supplier<SoundEvent> shootSound;

    private Map<GunAnimation.Type, Supplier<GunAnimation>> animations;

    public Properties setFireRate(int fireRate) {
      this.fireRate = fireRate;
      return this;
    }

    public Properties setClipSize(int clipSize) {
      this.clipSize = clipSize;
      return this;
    }

    public Properties setDamage(int damage) {
      this.damage = damage;
      return this;
    }

    public Properties setReloadTime(float reloadTime) {
      this.reloadTime = reloadTime;
      return this;
    }

    public Properties setAccuracy(float accuracy) {
      this.accuracy = accuracy;
      return this;
    }

    public Properties setFireModes(List<Supplier<IFireMode>> fireModes) {
      this.fireModes = ImmutableList.copyOf(fireModes);
      return this;
    }

    public Properties setShootSound(Supplier<SoundEvent> shootSound) {
      this.shootSound = shootSound;
      return this;
    }

    public Properties setAnimations(Map<GunAnimation.Type, Supplier<GunAnimation>> animations) {
      this.animations = ImmutableMap.copyOf(animations);
      return this;
    }
  }
}
