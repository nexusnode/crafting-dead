package com.craftingdead.core.capability.living;

import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ILiving<E extends LivingEntity> extends INBTSerializable<CompoundNBT> {

  default boolean performAction(IAction action, boolean sendUpdate) {
    return this.performAction(action, false, sendUpdate);
  }

  boolean performAction(IAction action, boolean force, boolean sendUpdate);

  void cancelAction(boolean sendUpdate);

  void setActionProgress(IActionProgress actionProgress);

  Optional<IActionProgress> getActionProgress();

  void setFreezeMovement(boolean movementFrozen);

  boolean getFreezeMovement();

  void tick();

  /**
   * When this entity is damaged; with potions and armour taken into account.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return the new damage amount
   */
  float onDamaged(DamageSource source, float amount);

  /**
   * When this entity is attacked.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return if the event should be cancelled
   */
  boolean onAttacked(DamageSource source, float amount);

  /**
   * When this entity kills another entity.
   *
   * @param target - the {@link Entity} killed
   * @return if the event should be cancelled
   */
  boolean onKill(Entity target);

  /**
   * When this entity's health reaches 0.
   *
   * @param cause - the cause of death
   * @return if the event should be cancelled
   */
  boolean onDeath(DamageSource cause);

  /**
   * When this entity's death causes dropped items to appear.
   *
   * @param cause - the DamageSource that caused the drop to occur
   * @param drops - a collections of EntityItems that will be dropped
   * @return if the event should be cancelled
   */
  boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops);

  IItemHandlerModifiable getItemHandler();

  Optional<Snapshot> getSnapshot(long gameTime);

  boolean isCrouching();

  void setCrouching(boolean crouching, boolean sendUpdate);

  E getEntity();

  public static <L extends ILiving<? extends E>, E extends LivingEntity> L get(E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING).<L>cast()
        .orElseThrow(() -> new IllegalStateException("Missing living capability"));
  }

  public static interface IActionProgress {

    ITextComponent getMessage();

    @Nullable
    ITextComponent getSubMessage();

    float getProgress(float partialTicks);

    void stop();
  }

  public class Snapshot {

    private final long gameTime;
    private final Vec3d pos;
    private final AxisAlignedBB boundingBox;
    private final Vec2f pitchYaw;
    private final float eyeHeight;

    public Snapshot(long gameTime, Vec3d pos, AxisAlignedBB boundingBox, Vec2f pitchYaw,
        float eyeHeight) {
      this.gameTime = gameTime;
      this.pos = pos;
      this.boundingBox = boundingBox;
      this.pitchYaw = pitchYaw;
      this.eyeHeight = eyeHeight;
    }

    public Optional<Vec3d> rayTrace(ILiving<?> fromLiving) {
      LivingEntity fromEntity = fromLiving.getEntity();
      IAttributeInstance reachDistanceAttribute =
          fromEntity instanceof PlayerEntity
              ? ((PlayerEntity) fromEntity).getAttribute(PlayerEntity.REACH_DISTANCE)
              : null;
      return this.rayTrace(fromLiving,
          reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue());
    }

    public Optional<Vec3d> rayTrace(ILiving<?> fromLiving, double distance) {
      return fromLiving.getSnapshot(this.gameTime).flatMap(fromSnapshot -> {
        Vec3d start = new Vec3d(fromSnapshot.getPos().getX(),
            fromSnapshot.getPos().getY() + fromSnapshot.getEyeHeight(),
            fromSnapshot.getPos().getZ());
        Vec3d look =
            this.getVectorForRotation(fromSnapshot.getPitchYaw().x, fromSnapshot.getPitchYaw().y);
        Vec3d scaledLook = look.scale(distance);
        Vec3d end = start.add(scaledLook);
        return this.getBoundingBox().rayTrace(start, end);
      });
    }

    protected final Vec3d getVectorForRotation(float pitch, float yaw) {
      float f = pitch * ((float) Math.PI / 180F);
      float f1 = -yaw * ((float) Math.PI / 180F);
      float f2 = MathHelper.cos(f1);
      float f3 = MathHelper.sin(f1);
      float f4 = MathHelper.cos(f);
      float f5 = MathHelper.sin(f);
      return new Vec3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
    }

    public long getGameTime() {
      return this.gameTime;
    }

    public Vec3d getPos() {
      return this.pos;
    }

    public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
    }

    public Vec2f getPitchYaw() {
      return this.pitchYaw;
    }

    public float getEyeHeight() {
      return this.eyeHeight;
    }
  }
}
