package com.craftingdead.mod.entity;

import com.craftingdead.mod.item.GrenadeType;
import com.craftingdead.mod.item.ModItems;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;

public class GrenadeEntity extends ProjectileItemEntity {

	private float explosionRadius;
	private float countdown;
	private GrenadeType type;
	
	public GrenadeEntity(EntityType<? extends ProjectileItemEntity> entityIn, World worldIn) {
		super(entityIn, worldIn);
	}
	
	
	public GrenadeType getGraGrenadeType() {
		return type;
	}
	

	public GrenadeEntity(World worldIn, LivingEntity playerIn,
			GrenadeType type,
			float explosionRadius,
			float countdown) {
		super(ModEntityTypes.grenade, playerIn, worldIn);
		this.type = type;
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
		
		boolean causeFire = type == GrenadeType.FIRE ? true : false;
		Mode mode =  type == GrenadeType.FIRE ? Mode.BREAK : Mode.NONE;
		switch(type) {
		case DECOY:
			break;
		case FIRE:
			break;
		case FLASH:
			break;
		case GAS:
			
			break;
		case PIPEBOMB:
			break;
		case SMOKE:
			
			for(double x = this.getX() - explosionRadius; x < this.getX() + explosionRadius; x++)
				for(double y = this.getY() - explosionRadius; y < this.getY() + explosionRadius; y++)
					for(double z = this.getZ() - explosionRadius; z < this.getZ() + explosionRadius; z++)
						this.world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.5d, 0.5d, 0.5d);
			
			
			
			break;
		default:
			break;
				
		}
		
		
		
		return this.world.createExplosion(this, this.getX(),
				this.getBodyY(0.0625D), this.getZ(), explosionRadius, 
				causeFire, 	
				mode);
	}

}
