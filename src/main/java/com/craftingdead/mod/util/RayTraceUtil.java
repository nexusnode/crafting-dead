package com.craftingdead.mod.util;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RayTraceUtil {

	public static EntityRayTraceResult rayTraceEntities(Entity fromEntity, Vec3d p_221273_1_, Vec3d p_221273_2_,
			AxisAlignedBB boundingBox, Predicate<Entity> filter, double distance) {
		World world = fromEntity.world;
		Entity resultEntity = null;
		Vec3d vec3d = null;

		for (Entity potentialEntity : world.getEntitiesInAABBexcluding(fromEntity, boundingBox, filter)) {
			AxisAlignedBB axisalignedbb = potentialEntity.getBoundingBox()
					.grow((double) potentialEntity.getCollisionBorderSize());
			Optional<Vec3d> optional = axisalignedbb.func_216365_b(p_221273_1_, p_221273_2_);
			if (axisalignedbb.contains(p_221273_1_)) {
				if (distance >= 0.0D) {
					resultEntity = potentialEntity;
					vec3d = optional.orElse(p_221273_1_);
					distance = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3d vec3d1 = optional.get();
				double d1 = p_221273_1_.squareDistanceTo(vec3d1);
				if (d1 < distance || distance == 0.0D) {
					if (potentialEntity.getLowestRidingEntity() == fromEntity.getLowestRidingEntity()) {
						if (distance == 0.0D) {
							resultEntity = potentialEntity;
							vec3d = vec3d1;
						}
					} else {
						resultEntity = potentialEntity;
						vec3d = vec3d1;
						distance = d1;
					}
				}
			}
		}

		if (resultEntity == null) {
			return null;
		} else {
			return new EntityRayTraceResult(resultEntity, vec3d);
		}
	}

	/**
	 * Perform a block ray trace from the parsed entity
	 * 
	 * @param entity       - the entity performing the ray trace
	 * @param distance     - the distance
	 * @param partialTicks - the partialTicks (parse 1 if not available)
	 * @return the result or null
	 */
	@Nullable
	public static RayTraceResult rayTraceBlocks(Entity entity, double distance, float partialTicks) {
		Vec3d vec3d = entity.getEyePosition(partialTicks);
		Vec3d vec3d1 = entity.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
		return entity.world.func_217299_a(new RayTraceContext(vec3d, vec3d2, RayTraceContext.BlockMode.OUTLINE,
				RayTraceContext.FluidMode.NONE, entity));
	}

	/**
	 * Perform a full ray trace from the parsed entity
	 * 
	 * @param entity       - the entity performing the ray trace
	 * @param distance     - the distance
	 * @param partialTicks - the partialTicks (parse 1 if not available)
	 * @return the result or null
	 */
	public static RayTraceResult rayTrace(final Entity entity, double distance, final float partialTicks) {
		RayTraceResult result = rayTraceBlocks(entity, distance, partialTicks);
		Vec3d eyePosition = entity.getEyePosition(partialTicks);

		double squareDistance = result != null ? result.getHitVec().squareDistanceTo(eyePosition) : distance * distance;

		Vec3d look = entity.getLook(partialTicks);
		Vec3d scaledLook = look.scale(distance);
		Vec3d eyeScaledLook = eyePosition.add(scaledLook);

		AxisAlignedBB boundingBox = entity.getBoundingBox().func_216361_a(scaledLook).grow(1.0D, 1.0D, 1.0D);

		EntityRayTraceResult entityRayTrace = ProjectileHelper.func_221269_a(entity.world, entity, eyePosition,
				eyeScaledLook, boundingBox, (entityTest) -> {
					return !entityTest.isSpectator() && entityTest.canBeCollidedWith();
				}, squareDistance);
		if (entityRayTrace != null)
			result = entityRayTrace;

		return result;
	}
}
