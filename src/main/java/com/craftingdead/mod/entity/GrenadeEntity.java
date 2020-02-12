package com.craftingdead.mod.entity;

import com.craftingdead.mod.item.ModItems;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class GrenadeEntity extends ProjectileItemEntity {

	private float explosionRadius;
	private float countdown;
	

	public GrenadeEntity(EntityType<? extends ProjectileItemEntity> p_i50155_1_, World worldIn) {
		super(p_i50155_1_, worldIn);
	}

	public GrenadeEntity(World worldIn, LivingEntity playerIn, float explosionRadius,
			float countdown) {
		super(ModEntityTypes.grenade, playerIn, worldIn);
		this.explosionRadius = explosionRadius;
		this.countdown = countdown;
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.FIRE_GRENADE.get();
	}
	
	

	@Override
	public void tick() {
		if (countdown <= 0) {
			
			Explosion exp = explode();
			this.remove();
		} else
			countdown--;
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.getType() == RayTraceResult.Type.BLOCK)
			setVelocity(0, 0, 0);
	}
	
	

	
	

	protected Explosion explode() {
		return this.world.createExplosion(this, this.getX(),
				this.getBodyY(0.0625D), this.getZ(), explosionRadius, true, Explosion.Mode.BREAK);
	}

}
