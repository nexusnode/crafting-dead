package com.craftingdead.mod.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimation;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import com.craftingdead.mod.util.RayTraceUtil;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class GunItem extends ShootableItem {

  private static final UUID REACH_DISTANCE_MODIFIER =
      UUID.fromString("A625D496-9464-4891-9E1F-9345989E5DAE");

  private static final float HEADSHOT_MULTIPLIER = 4;

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
   * {@link FireMode}s the gun can cycle through.
   */
  private final List<FireMode> fireModes;

  private final Supplier<SoundEvent> shootSound;

  private final Supplier<SoundEvent> reloadSound;

  private final Map<AnimationType, Supplier<IAnimation>> animations;

  private final Set<ResourceLocation> acceptedClips;

  private final float cameraZoom;

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
    this.acceptedClips = properties.acceptedClips;
    this.cameraZoom = properties.cameraZoom;
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

  public List<FireMode> getFireModes() {
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

  public Set<ResourceLocation> getAcceptedClips() {
    return this.acceptedClips;
  }

  public float getCameraZoom(ItemStack itemStack) {
    float cameraZoom = this.cameraZoom;
    AttachmentItem sight = this.getAttachments(itemStack).get(AttachmentItem.Type.SIGHT);
    if (sight != null) {
      cameraZoom *= sight.getCameraZoom();
    }
    return cameraZoom;
  }

  public float getAccuracy(ItemStack itemStack, Entity entity) {
    float accuracy = 1.0F;

    if (entity.getX() != entity.lastTickPosX || entity.getY() != entity.lastTickPosY
        || entity.getZ() != entity.lastTickPosZ) {

      accuracy = 0.5F;

      if (entity.isSprinting()) {
        accuracy = 0.25F;
      }

      if (entity.isSneaking() && entity.onGround) {
        accuracy = 1.0F;
      }
    }

    accuracy *= this.accuracy * this
        .getAttachments(itemStack)
        .values()
        .stream()
        .map(AttachmentItem::getAccuracy)
        .reduce(1.0F, (a, b) -> a * b);
    return accuracy;
  }

  public Map<AttachmentItem.Type, AttachmentItem> getAttachments(ItemStack itemStack) {
    CompoundNBT compound = itemStack.getOrCreateChildTag("attachments");
    NonNullList<ItemStack> attachments =
        NonNullList.withSize(AttachmentItem.Type.values().length, ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(compound, attachments);
    Map<AttachmentItem.Type, AttachmentItem> attachmentsMap = attachments
        .stream()
        .filter(attachment -> attachment.getItem() instanceof AttachmentItem)
        .collect(Collectors
            .toMap(attachment -> ((AttachmentItem) attachment.getItem()).getType(),
                attachment -> (AttachmentItem) attachment.getItem(), (u, v) -> {
                  throw new IllegalStateException(String.format("Duplicate key %s", u));
                }, () -> new EnumMap<>(AttachmentItem.Type.class)));
    return attachmentsMap;
  }

  public void shoot(ItemStack itemStack, Entity entity) {
    if (this.getAmmoCount(itemStack) <= 0) {
      entity.playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      return;
    }

    this.decrementAmmoCount(itemStack);

    if (entity.world.isRemote()) {
      if (entity instanceof ClientPlayerEntity) {
        ((ClientDist) CraftingDead.getInstance().getModDist())
            .getRecoilHelper()
            .jolt(this.accuracy);
      }
      itemStack
          .getCapability(ModCapabilities.ANIMATION_CONTROLLER)
          .ifPresent(animationController -> {
            IAnimation animation = this.animations.get(GunItem.AnimationType.SHOOT).get();
            if (animation != null) {
              animationController.cancelCurrentAnimation();
              animationController.addAnimation(animation);
            }
          });
    }

    entity.playSound(this.shootSound.get(), 1.0F, 1.0F);

    Optional<? extends RayTraceResult> rayTrace = RayTraceUtil.traceAllObjects(entity, 100, 1.0F);

    if (MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Pre(itemStack, entity, rayTrace))) {
      return;
    }

    rayTrace.ifPresent(result -> {
      switch (result.getType()) {
        case BLOCK:
          this.hitBlock(entity, (BlockRayTraceResult) result);
          break;
        case ENTITY:
          this.hitEntity(entity, (EntityRayTraceResult) result);
          break;
        default:
          break;
      }
    });

    MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(itemStack, entity, rayTrace));
  }

  public int getAmmoCount(ItemStack itemStack) {
    return itemStack.getOrCreateTag().getInt("ammo");
  }

  public void setAmmoCount(ItemStack itemStack, int count) {
    itemStack.getOrCreateTag().putInt("ammo", count);
  }

  public void decrementAmmoCount(ItemStack itemStack) {
    this.setAmmoCount(itemStack, this.getAmmoCount(itemStack) - 1);
  }

  public FireMode getFireMode(ItemStack itemStack) {
    return this.fireModes.get(itemStack.getOrCreateTag().getInt("fireMode"));
  }

  private void hitEntity(Entity entity, EntityRayTraceResult rayTrace) {
    Entity entityHit = rayTrace.getEntity();
    float damage = this.getDamage();
    if ((entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity)
        && rayTrace.getHitVec().y >= (entityHit.getY() + entityHit.getEyeHeight())) {
      damage *= HEADSHOT_MULTIPLIER;
    }
    entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity), damage);
  }

  private void hitBlock(Entity entity, BlockRayTraceResult rayTrace) {
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    Block block = blockState.getBlock();
    if (block instanceof TNTBlock) {
      block
          .catchFire(blockState, entity.getEntityWorld(), blockPos, null,
              entity instanceof LivingEntity ? (LivingEntity) entity : null);
      entity.getEntityWorld().removeBlock(blockPos, false);
    }
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot,
      ItemStack itemStack) {
    Multimap<String, AttributeModifier> modifiers =
        super.getAttributeModifiers(equipmentSlot, itemStack);
    if (equipmentSlot == EquipmentSlotType.MAINHAND) {
      modifiers
          .put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(REACH_DISTANCE_MODIFIER,
              "Weapon modifier", 100, AttributeModifier.Operation.ADDITION));
    }
    return modifiers;
  }

  @Override
  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return itemStack -> this.acceptedClips
        .stream()
        .map(ForgeRegistries.ITEMS::getValue)
        .anyMatch(itemStack.getItem()::equals);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new ICapabilityProvider() {
      private final IAnimationController animationController =
          ModCapabilities.ANIMATION_CONTROLLER.getDefaultInstance();

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ModCapabilities.ANIMATION_CONTROLLER
            ? LazyOptional.of(() -> this.animationController).cast()
            : LazyOptional.empty();
      }
    };
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {
    return oldStack.getItem() != newStack.getItem();
  }

  public static enum AnimationType {
    SHOOT;
  }

  public static class Properties extends Item.Properties {

    private int fireRate;

    private int damage;

    private int reloadDurationTicks;

    private float accuracy;

    private List<FireMode> fireModes;

    private Supplier<SoundEvent> shootSound;

    private Supplier<SoundEvent> reloadSound;

    private Map<AnimationType, Supplier<IAnimation>> animations;

    private Set<ResourceLocation> acceptedClips;

    private float cameraZoom = 1.0F;

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

    public Properties setFireModes(List<FireMode> fireModes) {
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

    public Properties setAcceptedClips(Set<ResourceLocation> acceptedClips) {
      this.acceptedClips = acceptedClips;
      return this;
    }

    public Properties setCameraZoom(float cameraZoom) {
      this.cameraZoom = cameraZoom;
      return this;
    }
  }
}
