package com.craftingdead.mod.capability.triggerable;

import java.util.concurrent.TimeUnit;

import com.craftingdead.mod.event.BulletCollisionEvent;
import com.craftingdead.mod.init.ModSoundEvents;
import com.craftingdead.mod.item.ItemGun;
import com.craftingdead.mod.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

public class GunController implements Triggerable {

	private final ItemGun item;

	private boolean triggerPressed;

	private long lastShot;

	public GunController(ItemGun item) {
		this.item = item;
	}

	@Override
	public NBTTagCompound writeNBT(EnumFacing side) {
		return new NBTTagCompound();
	}

	@Override
	public void readNBT(EnumFacing side, NBTTagCompound nbt) {
		;
	}

	@Override
	public void update(ItemStack itemStack, Entity entity) {
		if (this.triggerPressed && System.nanoTime() - this.lastShot > TimeUnit.NANOSECONDS
				.convert(this.item.getFireRate(), TimeUnit.MILLISECONDS)) {
			this.lastShot = System.nanoTime();
			this.shoot(itemStack, entity);
		}
	}

	private void shoot(ItemStack itemStack, Entity entity) {
		entity.playSound(ModSoundEvents.ACR_SHOOT, 1.0F, 1.0F);
		RayTraceResult result = Util.rayTrace(entity, 100, 1.0F);
		if (result != null) {
			switch (result.typeOfHit) {
			case MISS:
				break;
			case BLOCK:
				this.hitBlock(itemStack, entity, result);
				break;
			case ENTITY:
				this.hitEntity(entity, result.entityHit);
				break;
			}
		}
	}

	private void hitEntity(Entity entity, Entity hit) {

	}

	private void hitBlock(ItemStack itemStack, Entity entity, RayTraceResult rayTrace) {
		BlockPos blockPos = rayTrace.getBlockPos();
		IBlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof BlockTNT && !MinecraftForge.EVENT_BUS
				.post(new BulletCollisionEvent.PrimeTNTEvent(entity, itemStack, rayTrace))) {
			((BlockTNT) block).explode(entity.getEntityWorld(), blockPos,
					blockState.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)),
					entity instanceof EntityLivingBase ? ((EntityLivingBase) entity) : null);
			entity.getEntityWorld().setBlockToAir(blockPos);
		} else if (!MinecraftForge.EVENT_BUS
				.post(new BulletCollisionEvent.DestoryBlockEvent(entity, itemStack, rayTrace))) {
			entity.getEntityWorld().setBlockToAir(blockPos);
		}
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity) {
		this.triggerPressed = triggerPressed;
	}

}
