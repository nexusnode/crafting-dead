package com.craftingdead.mod.item;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.client.animation.IGunAnimation;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class GunItem extends Item {

  private static final UUID REACH_DISTANCE_MODIFIER =
      UUID.fromString("A625D496-9464-4891-9E1F-9345989E5DAE");

  /**
   * Time between shots in milliseconds.
   */
  private final int fireRate;

  private final int clipSize;

  private final int damage;

  private final float reloadTime;

  /**
   * Accuracy as percentage.
   */
  private final float accuracy;

  /**
   * A {@link List} of {@link IFireMode}s the gun can cycle through.
   */
  private final List<Supplier<IFireMode>> fireModes;

  private final Supplier<SoundEvent> shootSound;

  private final Map<IGunAnimation.Type, Supplier<IGunAnimation>> animations;

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

  public int getFireRate() {
    return fireRate;
  }

  public int getClipSize() {
    return clipSize;
  }

  public int getDamage() {
    return damage;
  }

  public float getReloadTime() {
    return reloadTime;
  }

  public float getAccuracy() {
    return accuracy;
  }

  public List<Supplier<IFireMode>> getFireModes() {
    return fireModes;
  }

  public Supplier<SoundEvent> getShootSound() {
    return shootSound;
  }

  public Map<IGunAnimation.Type, Supplier<IGunAnimation>> getAnimations() {
    return animations;
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(
      EquipmentSlotType equipmentSlot) {
    @SuppressWarnings("deprecation")
    Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(equipmentSlot);
    if (equipmentSlot == EquipmentSlotType.MAINHAND) {
      modifiers
          .put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(REACH_DISTANCE_MODIFIER,
              "Weapon modifier", 100, AttributeModifier.Operation.ADDITION));
    }
    return modifiers;
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

    private Map<IGunAnimation.Type, Supplier<IGunAnimation>> animations;

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

    public Properties setAnimations(Map<IGunAnimation.Type, Supplier<IGunAnimation>> animations) {
      this.animations = ImmutableMap.copyOf(animations);
      return this;
    }
  }
}
