package com.craftingdead.mod.entity;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class CorpseEntity extends Entity {

	private static final DataParameter<Optional<UUID>> PLAYER_UUID = EntityDataManager.createKey(CorpseEntity.class,
			DataSerializers.field_187203_m);
	private static final DataParameter<Integer> LIMB_COUNT = EntityDataManager.createKey(CorpseEntity.class,
			DataSerializers.field_187192_b);

	private Inventory inventory = new Inventory();

	public CorpseEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	public CorpseEntity(ServerPlayerEntity player) {
		this(EntityTypes.CORPSE, player.world);
		this.dataManager.set(PLAYER_UUID, Optional.of(player.getUniqueID()));
		this.setCustomName(player.getName());
		this.inventory = new Inventory(player.inventory.mainInventory.toArray(new ItemStack[0]));
		player.inventory.clear();
		this.setPosition(player.posX, player.posY, player.posZ);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		// Every 5 seconds
		if (!this.world.isRemote && this.ticksExisted % (20 * 10) == 0)
			this.decrementLimbCount();
		if (!this.onGround)
			this.setMotion(this.getMotion().subtract(0, 0.02D, 0));
		this.move(MoverType.SELF, this.getMotion());
	}

	@Override
	public boolean hitByEntity(Entity entity) {
		this.decrementLimbCount();
		return false;
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
		player.openContainer(new SimpleNamedContainerProvider((id, playerInventory, playerEntity) -> {
			return ChestContainer.func_216992_a(id, playerInventory, this.inventory);
		}, new StringTextComponent(this.getDisplayName().getFormattedText() + "'s Corpse")));
		return ActionResultType.PASS;
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
			this.remove();
			InventoryHelper.dropInventoryItems(this.world, this, this.inventory);
		}
	}

	public Optional<UUID> getPlayerUUID() {
		return this.dataManager.get(PLAYER_UUID);
	}

	public int getLimbCount() {
		return this.dataManager.get(LIMB_COUNT);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(PLAYER_UUID, Optional.empty());
		this.dataManager.register(LIMB_COUNT, 4);
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		if (compound.contains("playerUUID"))
			this.dataManager.set(PLAYER_UUID, Optional.of(compound.getUniqueId("playerUUID")));
		if (compound.contains("limbCount"))
			this.dataManager.set(LIMB_COUNT, compound.getInt("limbCount"));
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putUniqueId("playerUUID", this.getPlayerUUID().get());
		compound.putInt("limbCount", this.getLimbCount());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return new SSpawnObjectPacket(this);
	}
}
