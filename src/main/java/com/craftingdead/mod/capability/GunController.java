package com.craftingdead.mod.capability;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.capability.aimable.IAimable;
import com.craftingdead.mod.capability.shootable.IShootable;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.IFireMode;
import com.craftingdead.mod.item.Magazine;
import com.craftingdead.mod.item.MagazineItem;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.ReloadMessage;
import com.craftingdead.mod.network.message.main.TriggerPressedMessage;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import com.craftingdead.mod.util.RayTraceUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class GunController implements IShootable, IAimable, IAction, INBTSerializable<CompoundNBT> {

  private static final float HEADSHOT_MULTIPLIER = 4;

  /**
   * The {@link GunItem}.
   */
  private final GunItem item;

  /**
   * If the trigger is currently pressed.
   */
  private boolean triggerPressed;

  /**
   * Time of the last shot.
   */
  private long lastShotNanos = Integer.MIN_VALUE;

  /**
   * The selected {@link IFireMode}.
   */
  private IFireMode fireMode;

  /**
   * The accuracy value (0.0F - 1.0F)
   */
  private float accuracy;

  private int reloadDurationTicks = 0;

  private MagazineItem loadingMagazine;

  private Magazine magazine;
  
  private ItemStack attachment

  /**
   * Constructs a new {@link GunController}.
   *
   * @param item - the {@link GunItem} of the associated {@link ItemStack}
   */
  public GunController(GunItem item) {
    this.item = item;
    this.fireMode = item.getFireModes().get(0).get();
  }

  public GunItem getItem() {
    return this.item;
  }

  @Override
  public void tick(ItemStack itemStack, Entity entity) {
    this.updateAccuracy(entity);

    if (this.reloadDurationTicks > 0 && --this.reloadDurationTicks == 0) {
      this.magazine = this.loadingMagazine.getMagazine();
    }

    long fireRateNanoseconds =
        TimeUnit.NANOSECONDS.convert(this.item.getFireRate(), TimeUnit.MILLISECONDS);
    long time = System.nanoTime();
    long timeDelta = time - this.lastShotNanos;
    if (this.reloadDurationTicks == 0 && this.fireMode.canShoot(this.triggerPressed)
        && timeDelta > fireRateNanoseconds) {
      this.lastShotNanos = time;
      this.shoot(itemStack, entity);
    }
  }

  private void updateAccuracy(Entity entity) {
    this.accuracy = 1.0F;

    if (entity.func_226277_ct_() != entity.lastTickPosX
        || entity.func_226278_cu_() != entity.lastTickPosY
        || entity.func_226281_cx_() != entity.lastTickPosZ) {

      this.accuracy = 0.5F;

      if (entity.isSprinting()) {
        this.accuracy = 0.25F;
      }

      if (entity.func_225608_bj_() && entity.onGround) {
        this.accuracy = 1.0F;
      }
    }

    this.accuracy *= this.item.getAccuracy();
  }

  private void shoot(ItemStack itemStack, Entity entity) {
    if (this.magazine == null || this.magazine.getSizeAndDecrement() <= 0) {
      entity.playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      return;
    }

    entity.playSound(this.item.getShootSound().get(), 1.0F, 1.0F);

    Optional<? extends RayTraceResult> rayTrace = RayTraceUtil.traceAllObjects(entity, 100, 1.0F);

    if (MinecraftForge.EVENT_BUS
        .post(new GunEvent.ShootEvent.Pre(this, entity, itemStack, rayTrace))) {
      return;
    }

    rayTrace.ifPresent((result) -> {
      switch (result.getType()) {
        case BLOCK:
          this.hitBlock(itemStack, entity, (BlockRayTraceResult) result);
          break;
        case ENTITY:
          this.hitEntity(entity, (EntityRayTraceResult) result);
          break;
        default:
          break;
      }
    });

    MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(this, entity, itemStack, rayTrace));
  }

  private void hitEntity(Entity entity, EntityRayTraceResult rayTrace) {
    Entity entityHit = rayTrace.getEntity();
    float damage = this.item.getDamage();
    if ((entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity)
        && rayTrace.getHitVec().y >= (entityHit.func_226277_ct_() + entityHit.getEyeHeight())) {
      damage *= HEADSHOT_MULTIPLIER;
    }
    entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity), damage);
  }

  private void hitBlock(ItemStack itemStack, Entity entity, BlockRayTraceResult rayTrace) {
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
  public void reload(ItemStack itemStack, Entity entity) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) entity;
      if (this.reloadDurationTicks == 0) {
        if (playerEntity.isCreative()) {
          this.loadingMagazine = (MagazineItem) ForgeRegistries.ITEMS
              .getValue(this.item.getAcceptedMagazines().iterator().next());
        } else {
          ItemStack foundStack = playerEntity.findAmmo(itemStack);
          if (foundStack != null && foundStack.getItem() instanceof MagazineItem) {
            this.loadingMagazine = (MagazineItem) foundStack.getItem();
            foundStack.shrink(1);
            if (foundStack.isEmpty()) {
              playerEntity.inventory.deleteStack(foundStack);
            }
          } else {
            return;
          }
        }
        entity.playSound(this.item.getReloadSound().get(), 1.0F, 1.0F);
        this.reloadDurationTicks = this.item.getReloadDurationTicks();
      }
    }

    if (entity.getEntityWorld().isRemote()) {
      NetworkChannel.MAIN.getSimpleChannel().sendToServer(new ReloadMessage(0));
    } else {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
              new ReloadMessage(entity.getEntityId()));
    }
  }

  @Override
  public void setTriggerPressed(ItemStack itemStack, Entity entity, boolean triggerPressed) {
    this.triggerPressed = triggerPressed;
    if (entity.getEntityWorld().isRemote()) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(new TriggerPressedMessage(0, triggerPressed));
    } else {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
              new TriggerPressedMessage(entity.getEntityId(), triggerPressed));
    }
  }

  @Override
  public float getAccuracy() {
    return this.accuracy;
  }

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {

  }

  @Override
  public boolean isActive(PlayerEntity playerEntity, ItemStack itemStack) {
    return this.reloadDurationTicks > 0;
  }

  @Override
  public ITextComponent getText(PlayerEntity playerEntity, ItemStack itemStack) {
    return new TranslationTextComponent("action.reload");
  }

  @Override
  public float getPercentComplete(PlayerEntity playerEntity, ItemStack itemStack) {
    return (float) (this.item.getReloadDurationTicks() - this.reloadDurationTicks)
        / (float) this.item.getReloadDurationTicks();
  }
}
