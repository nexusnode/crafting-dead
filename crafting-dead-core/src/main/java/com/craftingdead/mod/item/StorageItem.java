package com.craftingdead.mod.item;


import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.storage.DefaultStorage;
import com.craftingdead.mod.capability.storage.IStorage;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.inventory.container.GenericContainer;
import com.google.common.collect.ImmutableSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageItem extends Item {

  public static final Supplier<IStorage> SMALL_BACKPACK = () -> new DefaultStorage(2 * 9,
      InventorySlotType.BACKPACK, GenericContainer::createSmallBackpack);
  public static final Supplier<IStorage> MEDIUM_BACKPACK = () -> new DefaultStorage(4 * 9,
      InventorySlotType.BACKPACK, GenericContainer::createMediumBackpack);
  public static final Supplier<IStorage> LARGE_BACKPACK = () -> new DefaultStorage(6 * 9,
      InventorySlotType.BACKPACK, GenericContainer::createLargeBackpack);
  public static final Supplier<IStorage> GUN_BAG =
      () -> new DefaultStorage(4 * 9, InventorySlotType.BACKPACK, GenericContainer::createGunBag);
  public static final Supplier<IStorage> QUIVER =
      () -> new DefaultStorage(6 * 9, InventorySlotType.BACKPACK, GenericContainer::createQuiver);
  public static final Supplier<IStorage> VEST =
      () -> new DefaultStorage(2 * 9, InventorySlotType.VEST, GenericContainer::createVest);

  private final Supplier<IStorage> storageContainer;

  public StorageItem(Supplier<IStorage> storageContainer, Properties properties) {
    super(properties);
    this.storageContainer = storageContainer;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableProvider<>(this.storageContainer.get(), ImmutableSet
        .of(() -> ModCapabilities.STORAGE, () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY));
  }
}
