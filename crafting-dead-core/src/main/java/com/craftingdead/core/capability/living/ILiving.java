/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.capability.living;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ILiving<E extends LivingEntity, L extends ILivingHandler>
    extends ILivingHandler {

  public static final ResourceLocation ID = new ResourceLocation(CraftingDead.ID, "living");

  Optional<L> getExtension(ResourceLocation id);

  void registerExtension(ResourceLocation id, L extension);

  default boolean performAction(IAction action, boolean sendUpdate) {
    return this.performAction(action, false, sendUpdate);
  }

  boolean performAction(IAction action, boolean force, boolean sendUpdate);

  void cancelAction(boolean sendUpdate);

  void setActionProgress(IActionProgress actionProgress);

  Optional<IActionProgress> getActionProgress();

  void setFreezeMovement(boolean movementFrozen);

  boolean getFreezeMovement();

  IItemHandlerModifiable getItemHandler();

  Optional<Snapshot> getSnapshot(long gameTime);

  boolean isCrouching();

  void setCrouching(boolean crouching, boolean sendUpdate);

  E getEntity();

  public static <E extends LivingEntity> ILiving<E, ?> getExpected(E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .<ILiving<E, ?>>cast()
        .orElseThrow(() -> new IllegalStateException("Missing living capability " + livingEntity));
  }

  public static <R extends ILiving<E, ?>, E extends LivingEntity> Optional<R> getOptional(
      E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .<R>cast()
        .map(Optional::of)
        .orElse(Optional.empty());
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

    public Optional<Vec3d> rayTrace(ILiving<?, ?> fromLiving) {
      LivingEntity fromEntity = fromLiving.getEntity();
      IAttributeInstance reachDistanceAttribute =
          fromEntity instanceof PlayerEntity
              ? ((PlayerEntity) fromEntity).getAttribute(PlayerEntity.REACH_DISTANCE)
              : null;
      return this.rayTrace(fromLiving,
          reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue());
    }

    public Optional<Vec3d> rayTrace(ILiving<?, ?> fromLiving, double distance) {
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
