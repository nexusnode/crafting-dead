package com.craftingdead.mod.capability.triggerable;

import java.util.concurrent.TimeUnit;

import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.init.ModDamageSource;
import com.craftingdead.mod.item.FireMode;
import com.craftingdead.mod.item.ItemGun;
import com.craftingdead.mod.util.RayTraceUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

public class GunController implements Triggerable {

	public static final float HEADSHOT_MULTIPLIER = 4;

	private final ItemGun item;

	private boolean triggerPressed;

	private long lastShot = Integer.MIN_VALUE;

	private FireMode fireMode;

	public GunController(ItemGun item) {
		this.item = item;
		this.fireMode = item.getFireModes().get(0).get();
	}

	@Override
	public void update(ItemStack itemStack, Entity entity) {
		if (this.fireMode.canShoot(this.triggerPressed) && System.nanoTime() - this.lastShot > TimeUnit.NANOSECONDS
				.convert(this.item.getFireRate(), TimeUnit.MILLISECONDS)) {
			this.lastShot = System.nanoTime();
			this.shoot(itemStack, entity);
		}
	}

	private void shoot(ItemStack itemStack, Entity entity) {
		entity.playSound(this.item.getShootSound().get(), 1.0F, 1.0F);
		RayTraceResult rayTrace = RayTraceUtil.rayTrace(entity, 100, 1.0F);
		if (MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Pre(item, entity, itemStack, rayTrace)))
			return;
		if (rayTrace != null) {
			switch (rayTrace.typeOfHit) {
			case MISS:
				break;
			case BLOCK:
				this.hitBlock(itemStack, entity, rayTrace);
				break;
			case ENTITY:
				this.hitEntity(entity, rayTrace);
				break;
			}
		}
		MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(item, entity, itemStack, rayTrace));
	}

	private void hitEntity(Entity entity, RayTraceResult rayTrace) {
		Entity entityHit = rayTrace.entityHit;
		float damage = this.item.getDamage();
		if ((entityHit instanceof EntityPlayer || entityHit instanceof EntityZombie)
				&& rayTrace.hitVec.y >= (entityHit.posY + entityHit.getEyeHeight()))
			damage *= HEADSHOT_MULTIPLIER;
		entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity), damage);
	}

	private void hitBlock(ItemStack itemStack, Entity entity, RayTraceResult rayTrace) {
		BlockPos blockPos = rayTrace.getBlockPos();
		IBlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
		Block block = blockState.getBlock();

		if (block instanceof BlockTNT) {
			((BlockTNT) block).explode(entity.getEntityWorld(), blockPos,
					blockState.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)),
					entity instanceof EntityLivingBase ? ((EntityLivingBase) entity) : null);
			entity.getEntityWorld().setBlockToAir(blockPos);
		}
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity) {
		this.triggerPressed = triggerPressed;
	}

}
