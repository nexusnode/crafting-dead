package com.craftingdead.mod.capability.gun;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimation;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
import com.craftingdead.mod.item.FireMode;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.MagazineItem;
import com.craftingdead.mod.item.PaintItem;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import com.craftingdead.mod.util.RayTraceUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemGunController implements IGunController {

  private static final float HEADSHOT_MULTIPLIER = 4;

  private final GunItem gunItem;

  private boolean triggerPressed;

  /**
   * Time of the last shot.
   */
  private long lastShotNanos = Integer.MIN_VALUE;

  protected int reloadDurationTicks = 0;

  protected int totalReloadDurationTicks;

  private int fireModeIndex = 0;

  private ItemStack magazineStack = ItemStack.EMPTY;

  private int ammo;

  private Set<AttachmentItem> attachments = new HashSet<>();

  private Optional<PaintItem> paint = Optional.empty();

  public ItemGunController(GunItem gunItem) {
    this.gunItem = gunItem;
  }

  @Override
  public void tick(Entity entity, ItemStack itemStack) {
    if (this.gunItem.getFireModes().get(this.fireModeIndex) == FireMode.AUTO) {
      this.tryShoot(entity, itemStack);
    }

    if (this.isReloading() && --this.reloadDurationTicks == 0) {
      ItemStack magazineStack = null;
      if (entity instanceof PlayerEntity) {
        PlayerEntity playerEntity = (PlayerEntity) entity;
        if (!playerEntity.isCreative()) {
          magazineStack = playerEntity.findAmmo(itemStack);
        }
      }

      if (magazineStack == null) {
        magazineStack = new ItemStack(this.gunItem.getAcceptedMagazines().iterator().next());
      }

      this.magazineStack = magazineStack;
      this.ammo = ((MagazineItem) magazineStack.getItem()).getSize();

      if (entity instanceof PlayerEntity) {
        PlayerEntity playerEntity = (PlayerEntity) entity;
        if (!playerEntity.isCreative()) {
          magazineStack.shrink(1);
          if (magazineStack.isEmpty()) {
            playerEntity.inventory.deleteStack(magazineStack);
          }
        }
      }
    }
  }

  @Override
  public void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed) {
    this.triggerPressed = triggerPressed && !this.isReloading();
    this.tryShoot(entity, itemStack);
  }

  @Override
  public boolean isReloading() {
    return this.reloadDurationTicks > 0;
  }

  @Override
  public int getTotalReloadDurationTicks() {
    return this.totalReloadDurationTicks;
  }

  @Override
  public int getReloadDurationTicks() {
    return this.reloadDurationTicks;
  }

  @Override
  public void stopReloading() {
    if (this.isReloading()) {
      Minecraft.getInstance().getSoundHandler().stop(null, SoundCategory.PLAYERS);
      this.reloadDurationTicks = 0;
    }
  }

  @Override
  public void startReloading(Entity entity, ItemStack itemStack) {
    boolean canReload =
        entity instanceof PlayerEntity
            ? (((PlayerEntity) entity).isCreative()
                || !((PlayerEntity) entity).findAmmo(itemStack).isEmpty())
            : true;
    if (!this.isReloading() && canReload) {
      entity.playSound(this.gunItem.getReloadSound().get(), 1.0F, 1.0F);
      this.reloadDurationTicks =
          this.totalReloadDurationTicks = this.gunItem.getReloadDurationTicks();
    }
  }

  private void tryShoot(Entity entity, ItemStack itemStack) {
    long fireRateNanoseconds =
        TimeUnit.NANOSECONDS.convert(this.gunItem.getFireRate(), TimeUnit.MILLISECONDS);
    long time = System.nanoTime();
    long timeDelta = time - this.lastShotNanos;
    if (timeDelta > fireRateNanoseconds && this.triggerPressed) {
      this.lastShotNanos = time;
      this.shoot(entity, itemStack);
    }
  }

  private void shoot(Entity entity, ItemStack itemStack) {
    if (this.ammo <= 0) {
      entity.playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      entity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> {
        player.reload(false);
      });
      return;
    }

    this.ammo--;

    if (entity.world.isRemote()) {
      if (entity instanceof ClientPlayerEntity) {
        ((ClientDist) CraftingDead.getInstance().getModDist())
            .joltCamera(this.getAccuracy(entity, itemStack));
      }

      itemStack
          .getCapability(ModCapabilities.ANIMATION_CONTROLLER)
          .ifPresent(animationController -> {
            IAnimation animation =
                this.gunItem.getAnimations().get(GunItem.AnimationType.SHOOT).get();
            if (animation != null) {
              animationController.cancelCurrentAnimation();
              animationController.addAnimation(animation);
            }
          });
    }

    entity.playSound(this.gunItem.getShootSound().get(), 0.25F, 1.0F);

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

  private void hitEntity(Entity entity, EntityRayTraceResult rayTrace) {
    Entity entityHit = rayTrace.getEntity();
    float damage = this.gunItem.getDamage();
    boolean headshot = (entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity)
        && rayTrace.getHitVec().y >= (entityHit.getY() + entityHit.getEyeHeight());
    if (headshot) {
      entity.playSound(SoundEvents.ENTITY_ITEM_BREAK, 2F, 1.5F);
      damage *= HEADSHOT_MULTIPLIER;
    }
    entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity, headshot), damage);
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
  public float getAccuracy(Entity entity, ItemStack itemStack) {
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

    return accuracy * this.gunItem.getAccuracy()
        * this.getAttachmentMultiplier(MultiplierType.ACCURACY);
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
  }

  @Override
  public int getAmmo() {
    return this.ammo;
  }

  @Override
  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }

  @Override
  public Set<AttachmentItem> getAttachments() {
    return this.attachments;
  }

  @Override
  public void setAttachments(Set<AttachmentItem> attachments) {
    this.attachments = attachments;
  }

  @Override
  public Optional<PaintItem> getPaint() {
    return this.paint;
  }

  @Override
  public void setPaint(Optional<PaintItem> paint) {
    this.paint = paint;
  }

  @Override
  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return itemStack != null
        && (this.gunItem.getAcceptedAttachments().contains((itemStack.getItem()))
            || this.gunItem.getAcceptedPaints().contains((itemStack.getItem())));
  }

  @Override
  public void toggleFireMode(Entity entity) {
    this.fireModeIndex = ~this.fireModeIndex & 1;
    entity.playSound(ModSoundEvents.TOGGLE_FIRE_MODE.get(), 1.0F, 1.0F);
    if (entity instanceof PlayerEntity) {
      ((PlayerEntity) entity)
          .sendStatusMessage(new TranslationTextComponent(
              this.gunItem.getFireModes().get(this.fireModeIndex).getTranslationKey()), true);
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("ammo", this.ammo);
    ListNBT attachmentsTag = this.attachments
        .stream()
        .map(attachment -> StringNBT.of(attachment.getRegistryName().toString()))
        .collect(ListNBT::new, ListNBT::add, List::addAll);
    nbt.put("attachments", attachmentsTag);
    this.paint.ifPresent(item -> {
      nbt.putString("paint", item.getRegistryName().toString());
    });
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.ammo = nbt.getInt("ammo");
    this.attachments = nbt
        .getList("attachments", 8)
        .stream()
        .map(tag -> (AttachmentItem) ForgeRegistries.ITEMS
            .getValue(new ResourceLocation(tag.getString())))
        .collect(Collectors.toSet());
    String paintLocation = nbt.getString("paint");
    this.paint = !paintLocation.isEmpty()
        ? Optional
            .of((PaintItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation(paintLocation)))
        : Optional.empty();
  }
}
