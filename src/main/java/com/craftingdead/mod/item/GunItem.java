package com.craftingdead.mod.item;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
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
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

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

  private final Set<Supplier<ClipItem>> acceptedClips;

  private final Set<Supplier<AttachmentItem>> acceptedAttachments;

  private final Set<Supplier<PaintItem>> acceptedPaints;

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
    this.acceptedAttachments = properties.acceptedAttachments;
    this.acceptedPaints = properties.acceptedPaints;
    this
        .addPropertyOverride(new ResourceLocation("aiming"),
            (itemStack, world, entity) -> entity != null ? entity
                .getCapability(ModCapabilities.PLAYER)
                .map(player -> entity.getHeldItemMainhand() == itemStack && player.isAiming() ? 1.0F
                    : 0.0F)
                .orElse(0.0F) : 0.0F);
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

  public Set<ClipItem> getAcceptedClips() {
    return this.acceptedClips.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Set<AttachmentItem> getAcceptedAttachments() {
    return this.acceptedAttachments.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Set<PaintItem> getAcceptedPaints() {
    return this.acceptedPaints.stream().map(Supplier::get).collect(Collectors.toSet());
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

    return accuracy * this.accuracy
        * this.getAttachmentMultiplier(itemStack, MultiplierType.ACCURACY);
  }

  public Float getAttachmentMultiplier(ItemStack itemStack,
      AttachmentItem.MultiplierType multiplierType) {
    return this
        .getAttachments(itemStack)
        .stream()
        .map(attachment -> attachment.getMultiplier(multiplierType))
        .reduce(1.0F, (x, y) -> x * y);
  }

  public Set<AttachmentItem> getAttachments(ItemStack itemStack) {
    CompoundNBT compound = itemStack.getOrCreateChildTag("attachments");
    NonNullList<ItemStack> attachments = NonNullList.withSize(3, ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(compound, attachments);
    return attachments
        .stream()
        .filter(attachmentStack -> attachmentStack.getItem() instanceof AttachmentItem)
        .map(attachmentStack -> (AttachmentItem) attachmentStack.getItem())
        .collect(Collectors.toSet());
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
            .jolt(this.getAccuracy(itemStack, entity));
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

  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return itemStack != null && (this.getAcceptedAttachments().contains((itemStack.getItem()))
        || this.getAcceptedPaints().contains((itemStack.getItem())));
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
        .map(Supplier::get)
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
  public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
    return true;
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {
    return oldStack.getItem() != newStack.getItem();
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity,
      Hand hand) {
    if (world.isRemote()) {
      playerEntity
          .getCapability(ModCapabilities.PLAYER)
          .ifPresent(player -> player.toggleAiming(true));
    }
    return super.onItemRightClick(world, playerEntity, hand);
  }

  public static enum AnimationType {
    SHOOT;
  }

  public static class Properties extends Item.Properties {

    private int fireRate;

    private int damage;

    private int reloadDurationTicks;

    private float accuracy;

    private final List<FireMode> fireModes = new ArrayList<>();

    private Supplier<SoundEvent> shootSound;

    private Supplier<SoundEvent> reloadSound;

    private final Map<AnimationType, Supplier<IAnimation>> animations =
        new EnumMap<>(AnimationType.class);

    private final Set<Supplier<ClipItem>> acceptedClips = new HashSet<>();

    private final Set<Supplier<AttachmentItem>> acceptedAttachments = new HashSet<>();

    private final Set<Supplier<PaintItem>> acceptedPaints = new HashSet<>();

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

    public Properties addFireMode(FireMode fireMode) {
      this.fireModes.add(fireMode);
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

    public Properties addAnimation(AnimationType type, Supplier<IAnimation> animation) {
      this.animations.put(type, animation);
      return this;
    }

    public Properties addAcceptedClip(Supplier<ClipItem> acceptedClip) {
      this.acceptedClips.add(acceptedClip);
      return this;
    }

    public Properties addAcceptedAttachment(Supplier<AttachmentItem> acceptedAttachment) {
      this.acceptedAttachments.add(acceptedAttachment);
      return this;
    }

    public Properties addAcceptedPaint(Supplier<PaintItem> acceptedPaint) {
      this.acceptedPaints.add(acceptedPaint);
      return this;
    }
  }
}
