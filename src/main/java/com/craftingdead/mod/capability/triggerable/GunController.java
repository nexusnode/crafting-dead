package com.craftingdead.mod.capability.triggerable;

import java.util.concurrent.TimeUnit;

import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.FireMode;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.RayTraceUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

public class GunController implements Triggerable {

	public static final float HEADSHOT_MULTIPLIER = 4;

	private final GunItem item;

	private boolean triggerPressed;

	private long lastShot = Integer.MIN_VALUE;

	private FireMode fireMode;

	private float reloadCounter;

	public GunController(GunItem item) {
		this.item = item;
		this.fireMode = item.getFireModes().get(0).get();
	}

	@Override
	public void update(ItemStack itemStack, Entity entity) {
		// On finished reloading
		if (this.reloadCounter-- == 0) {

		}
		if (this.fireMode.canShoot(this.triggerPressed) && System.nanoTime() - this.lastShot > TimeUnit.NANOSECONDS
				.convert(this.item.getFireRate(), TimeUnit.MILLISECONDS)) {
			this.lastShot = System.nanoTime();
			this.shoot(itemStack, entity);
		}
	}

	private void shoot(ItemStack itemStack, Entity entity) {
		entity.playSound(this.item.getShootSound().get(), 1.0F, 1.0F);
		RayTraceResult rayTrace = RayTraceUtil.rayTrace(entity, 100, 1.0F);
		if (MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Pre(this.item, entity, itemStack, rayTrace)))
			return;
		if (rayTrace != null) {
			switch (rayTrace.getType()) {
			case MISS:
				break;
			case BLOCK:
				this.hitBlock(itemStack, entity, (BlockRayTraceResult) rayTrace);
				break;
			case ENTITY:
				this.hitEntity(entity, (EntityRayTraceResult) rayTrace);
				break;
			}
		}
		MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(this.item, entity, itemStack, rayTrace));
	}

	private void hitEntity(Entity entity, EntityRayTraceResult rayTrace) {
		Entity entityHit = rayTrace.getEntity();
		float damage = this.item.getDamage();
		if ((entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity)
				&& rayTrace.getHitVec().y >= (entityHit.posY + entityHit.getEyeHeight()))
			damage *= HEADSHOT_MULTIPLIER;
		entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity), damage);
	}

	private void hitBlock(ItemStack itemStack, Entity entity, BlockRayTraceResult rayTrace) {
		System.out.println("block");

		BlockPos blockPos = rayTrace.getPos();
		BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof TNTBlock) {
			TNTBlock.explode(entity.getEntityWorld(), blockPos);
			entity.getEntityWorld().setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
		}
	}

	public void reload() {
		// If not already reloading
		if (this.reloadCounter == 0) {
			// Set reload time
			this.reloadCounter = this.item.getReloadTime();
		}
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity) {
		this.triggerPressed = triggerPressed;
	}

}
