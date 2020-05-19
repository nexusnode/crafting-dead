package com.craftingdead.mod.entity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class CorpseEntity extends Entity {

  private static final int INVENTORY_SIZE = 9 * 5;

  private static final DataParameter<Optional<UUID>> DECEASED_ID =
      EntityDataManager.createKey(CorpseEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

  private static final DataParameter<Integer> LIMB_COUNT =
      EntityDataManager.createKey(CorpseEntity.class, DataSerializers.VARINT);

  private final IInventory inventory;

  public CorpseEntity(World world, UUID deceasedId, ITextComponent deceasedName) {
    super(ModEntityTypes.corpse, world);
    this.inventory = new Inventory(INVENTORY_SIZE);
    this.setCustomName(deceasedName);
    this.dataManager.set(DECEASED_ID, Optional.ofNullable(deceasedId));
  }

  public CorpseEntity(EntityType<?> type, World world) {
    this(world, null, null);
  }

  public CorpseEntity(FMLPlayMessages.SpawnEntity packet, World world) {
    this(world, null, null);
  }

  /**
   * Constructor for the collection of {@link ItemEntity} from {@link LivingDropsEvent}.
   *
   * @param deathDrops - the dropped items. <b>Will get cleared</b>.
   */
  public CorpseEntity(ServerPlayerEntity corpseOwner, Collection<ItemEntity> deathDrops) {
    this(corpseOwner, deathDrops.stream()
        .map(ItemEntity::getItem)
        .filter(stack -> !stack.isEmpty())
        .toArray(size -> new ItemStack[size]));
    // Clears for safety
    deathDrops.clear();
  }

  public CorpseEntity(ServerPlayerEntity corpseOwner, ItemStack[] items) {
    this(corpseOwner.world, corpseOwner.getUniqueID(), corpseOwner.getName());
    this.setPosition(corpseOwner.getX(), corpseOwner.getY(), corpseOwner.getZ());

    for (int i = 0; i < items.length; i++) {
      this.inventory.setInventorySlotContents(i, items[i]);
    }
  }

  @Override
  public void baseTick() {
    super.baseTick();
    // Every 5 seconds
    if (!this.world.isRemote && this.ticksExisted % (20 * 10) == 0) {
      this.decrementLimbCount();
    }
    if (!this.onGround) {
      this.setMotion(this.getMotion().subtract(0, 0.02D, 0));
    }
    this.move(MoverType.SELF, this.getMotion());
  }

  @Override
  public boolean hitByEntity(Entity entity) {
    if (!this.world.isRemote()) {
      this.decrementLimbCount();
    }
    return false;
  }

  @Override
  public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
    player.openContainer(new SimpleNamedContainerProvider((id, playerInventory, playerEntity) -> {
      return new ChestContainer(ContainerType.GENERIC_9X5, id, playerInventory, this.inventory, 5);
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

  public Optional<UUID> getDeceasedId() {
    return this.dataManager.get(DECEASED_ID);
  }

  public int getLimbCount() {
    return this.dataManager.get(LIMB_COUNT);
  }

  public IInventory getInventory() {
    return inventory;
  }

  @Override
  protected void registerData() {
    this.dataManager.register(DECEASED_ID, Optional.empty());
    this.dataManager.register(LIMB_COUNT, 4);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    if (compound.contains("playerUUID")) {
      this.dataManager.set(DECEASED_ID, Optional.of(compound.getUniqueId("playerUUID")));
    }
    if (compound.contains("limbCount")) {
      this.dataManager.set(LIMB_COUNT, compound.getInt("limbCount"));
    }
    if (compound.contains("inventory")) {
      NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
      ItemStackHelper.loadAllItems(compound.getCompound("inventory"), items);
      for (int i = 0; i < items.size(); i++) {
        this.inventory.setInventorySlotContents(i, items.get(i));
      }
    }
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    this.getDeceasedId().ifPresent((uuid) -> compound.putUniqueId("playerUUID", uuid));
    compound.putInt("limbCount", this.getLimbCount());
    NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
      items.set(i, this.inventory.getStackInSlot(i));
    }
    compound
        .put("inventory", ItemStackHelper.saveAllItems(compound.getCompound("inventory"), items));
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
