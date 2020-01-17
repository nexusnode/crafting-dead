package com.craftingdead.mod.item;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.GunController;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimation;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class GunItem extends ShootableItem {

  private static final UUID REACH_DISTANCE_MODIFIER =
      UUID.fromString("A625D496-9464-4891-9E1F-9345989E5DAE");

  /**
   * Time between shots in milliseconds.
   */
  private final int fireRate;

  private final int damage;

  private final int reloadDurationTicks;

  /**
   * Accuracy as percentage.
   */
  private final float accuracy;

  /**
   * {@link IFireMode}s the gun can cycle through.
   */
  private final List<Supplier<IFireMode>> fireModes;

  private final Supplier<SoundEvent> shootSound;

  private final Supplier<SoundEvent> reloadSound;

  private final Map<AnimationType, Supplier<IAnimation>> animations;

  private final Set<ResourceLocation> acceptedMagazines;

  public GunItem(Properties properties) {
    super(properties);
    this.fireRate = properties.fireRate;
    this.damage = properties.damage;
    this.reloadDurationTicks = properties.reloadDurationTicks;
    this.accuracy = properties.accuracy;
    this.fireModes = properties.fireModes;
    this.shootSound = properties.shootSound;
    this.reloadSound = properties.reloadSound;
    this.animations = properties.animations;
    this.acceptedMagazines = properties.acceptedMagazines;
  }

  public int getFireRate() {
    return this.fireRate;
  }

  public int getDamage() {
    return this.damage;
  }

  public int getReloadDurationTicks() {
    return this.reloadDurationTicks;
  }

  public float getAccuracy() {
    return this.accuracy;
  }

  public List<Supplier<IFireMode>> getFireModes() {
    return this.fireModes;
  }

  public Supplier<SoundEvent> getShootSound() {
    return this.shootSound;
  }

  public Supplier<SoundEvent> getReloadSound() {
    return this.reloadSound;
  }

  public Map<AnimationType, Supplier<IAnimation>> getAnimations() {
    return this.animations;
  }

  public Set<ResourceLocation> getAcceptedMagazines() {
    return this.acceptedMagazines;
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
  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return itemStack -> this.acceptedMagazines
        .stream()
        .map(ForgeRegistries.ITEMS::getValue)
        .anyMatch(itemStack.getItem()::equals);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new ICapabilitySerializable<CompoundNBT>() {
      private final GunController gunController = new GunController(GunItem.this);

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return (cap == ModCapabilities.SHOOTABLE || cap == ModCapabilities.AIMABLE
            || cap == ModCapabilities.ACTION) ? LazyOptional.of(() -> this.gunController).cast()
                : LazyOptional.empty();
      }

      @Override
      public CompoundNBT serializeNBT() {
        return this.gunController.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundNBT nbt) {
        this.gunController.deserializeNBT(nbt);
      }
    };
  }
  
  public static enum AnimationType {
    SHOOT;
  }

  public static class Properties extends Item.Properties {

    private int fireRate;

    private int damage;

    private int reloadDurationTicks;

    private float accuracy;

    private List<Supplier<IFireMode>> fireModes;

    private Supplier<SoundEvent> shootSound;

    private Supplier<SoundEvent> reloadSound;

    private Map<AnimationType, Supplier<IAnimation>> animations;

    private Set<ResourceLocation> acceptedMagazines;

    public Properties setFireRate(int fireRate) {
      this.fireRate = fireRate;
      return this;
    }

    public Properties setDamage(int damage) {
      this.damage = damage;
      return this;
    }

    public Properties setReloadDurationTicks(int reloadDurationTicks) {
      this.reloadDurationTicks = reloadDurationTicks;
      return this;
    }

    public Properties setAccuracy(float accuracy) {
      this.accuracy = accuracy;
      return this;
    }

    public Properties setFireModes(List<Supplier<IFireMode>> fireModes) {
      this.fireModes = fireModes;
      return this;
    }

    public Properties setShootSound(Supplier<SoundEvent> shootSound) {
      this.shootSound = shootSound;
      return this;
    }

    public Properties setReloadSound(Supplier<SoundEvent> reloadSound) {
      this.reloadSound = reloadSound;
      return this;
    }

    public Properties setAnimations(Map<AnimationType, Supplier<IAnimation>> animations) {
      this.animations = animations;
      return this;
    }

    public Properties setAcceptedMagazines(Set<ResourceLocation> acceptedMagazines) {
      this.acceptedMagazines = acceptedMagazines;
      return this;
    }
  }
}
