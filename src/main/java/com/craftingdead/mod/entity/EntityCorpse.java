package com.craftingdead.mod.entity;

import java.util.UUID;

import com.craftingdead.mod.inventory.InventoryCorpse;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCorpse extends Entity {

	private static final DataParameter<String> PLAYER_UUID = EntityDataManager.createKey(EntityCorpse.class,
			DataSerializers.STRING);
	private static final DataParameter<Integer> LIMB_COUNT = EntityDataManager.createKey(EntityCorpse.class,
			DataSerializers.VARINT);

	private InventoryCorpse inventory;

	public EntityCorpse(World world) {
		super(world);
		this.inventory = new InventoryCorpse(this);
		this.setSize(1.0F, 0.5F);
	}

	public EntityCorpse(EntityPlayerMP player) {
		this(player.world);
		this.dataManager.set(PLAYER_UUID, player.getUniqueID().toString());
		this.setCustomNameTag(player.getName());
		this.inventory.copy(player.inventory.mainInventory);
		player.inventory.clear();
		this.setPosition(player.posX, player.posY, player.posZ);
	}

	@Override
	public void entityInit() {
		this.dataManager.register(PLAYER_UUID, this.getUniqueID().toString());
		this.dataManager.register(LIMB_COUNT, 4);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("playerUUID"))
			this.dataManager.set(PLAYER_UUID, compound.getUniqueId("playerUUID").toString());
		if (compound.hasKey("limbCount"))
			this.dataManager.set(LIMB_COUNT, compound.getInteger("limbCount"));
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setUniqueId("playerUUID", this.getPlayerUUID());
		compound.setInteger("limbCount", this.getLimbCount());
		compound.setTag("inventory", this.inventory.serializeNBT());
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		// Every 5 seconds
		if (!this.world.isRemote && this.ticksExisted % (20 * 10) == 0)
			this.decrementLimbCount();
		if (!this.onGround)
			this.motionY -= 0.02D;
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
	}

	@Override
	public boolean hitByEntity(Entity entity) {
		this.decrementLimbCount();
		return false;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		player.displayGUIChest(this.inventory);
		return EnumActionResult.PASS;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	private void decrementLimbCount() {
		if (this.getLimbCount() > 0) {
			this.dataManager.set(LIMB_COUNT, this.getLimbCount() - 1);
		} else {
			this.setDead();
			this.inventory.dropAllItems();
		}

	}

	public UUID getPlayerUUID() {
		return UUID.fromString(this.dataManager.get(PLAYER_UUID));
	}

	public int getLimbCount() {
		return this.dataManager.get(LIMB_COUNT);
	}

}
