package com.craftingdead.mod.common.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCDZombie extends EntityZombie {

	private static final DataParameter<Integer> TEXTURE_NUMBER = EntityDataManager.createKey(EntityCDZombie.class,
			DataSerializers.VARINT);

	public EntityCDZombie(World world) {
		super(world);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 12.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.dataManager.register(TEXTURE_NUMBER, 0);
	}

	@Override
	public int getTotalArmorValue() {
		int armorValue = super.getTotalArmorValue() + 2;
		if (armorValue > 20) {
			armorValue = 20;
		}
		return armorValue;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 12;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		Item item = this.getDropItem();
		if (item != null) {
			this.dropItem(item, 1);
		}
	}

	@Override
	protected boolean shouldBurnInDay() {
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(TEXTURE_NUMBER, compound.getInteger("textureNumber"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("textureNumber", this.dataManager.get(TEXTURE_NUMBER));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.dataManager.set(TEXTURE_NUMBER, this.rand.nextInt(23));
		return livingdata;
	}

	public int getTextureNumber() {
		return this.dataManager.get(TEXTURE_NUMBER);
	}

}