package com.craftingdead.mod.util;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Util {

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
		Vec3d vec3d = entity.getPositionEyes(partialTicks);
		Vec3d vec3d1 = entity.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
		return entity.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
	}

	/**
	 * Perform a full ray trace from the parsed entity
	 * 
	 * @param entity       - the entity performing the ray trace
	 * @param distance     - the distance
	 * @param partialTicks - the partialTicks (parse 1 if not available)
	 * @return the result or null
	 */
	@Nullable
	public static RayTraceResult rayTrace(final Entity entity, final double distance, final float partialTicks) {
		if (entity != null) {
			if (entity.world != null) {
				RayTraceResult result = rayTraceBlocks(entity, distance, partialTicks);
				Vec3d eyePosition = entity.getPositionEyes(partialTicks);

				double currentHitDistance = distance;
				if (result != null)
					currentHitDistance = result.hitVec.distanceTo(eyePosition);

				Vec3d look = entity.getLook(1.0F);
				Vec3d selectedVector = eyePosition.add(look.x * distance, look.y * distance, look.z * distance);
				Entity hitEntity = null;
				Vec3d hitEntityVector = null;
				List<Entity> entitiesInBB = entity.world.getEntitiesInAABBexcluding(entity,
						entity.getEntityBoundingBox().expand(look.x * distance, look.y * distance, look.z * distance)
								.grow(1.0D, 1.0D, 1.0D),
						Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
							public boolean apply(@Nullable Entity entity) {
								return entity != null && entity.canBeCollidedWith();
							}
						}));

				double hitEntityDistance = currentHitDistance;
				for (Entity currentEntity : entitiesInBB) {
					AxisAlignedBB entityBoundingBox = currentEntity.getEntityBoundingBox()
							.grow((double) currentEntity.getCollisionBorderSize());
					RayTraceResult entityTraceResult = entityBoundingBox.calculateIntercept(eyePosition, selectedVector);

					if (entityBoundingBox.contains(eyePosition)) {
						if (hitEntityDistance >= 0.0D) {
							hitEntity = currentEntity;
							hitEntityVector = entityTraceResult == null ? eyePosition : entityTraceResult.hitVec;
							hitEntityDistance = 0.0D;
						}
					} else if (entityTraceResult != null) {
						double potentialDistanceToEntity = eyePosition.distanceTo(entityTraceResult.hitVec);

						if (potentialDistanceToEntity < hitEntityDistance || hitEntityDistance == 0.0D) {
							if (currentEntity.getLowestRidingEntity() == entity.getLowestRidingEntity()
									&& !currentEntity.canRiderInteract()) {
								if (hitEntityDistance == 0.0D) {
									hitEntity = currentEntity;
									hitEntityVector = entityTraceResult.hitVec;
								}
							} else {
								hitEntity = currentEntity;
								hitEntityVector = entityTraceResult.hitVec;
								hitEntityDistance = potentialDistanceToEntity;
							}
						}
					}
				}

				if (hitEntity != null && (hitEntityDistance < currentHitDistance || result == null)) {
					result = new RayTraceResult(hitEntity, hitEntityVector);
				}

				return result;
			}
		}
		return null;
	}

}
