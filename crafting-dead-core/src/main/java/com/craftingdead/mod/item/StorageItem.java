package com.craftingdead.mod.item;


import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.storage.DefaultStorage;
import com.craftingdead.mod.capability.storage.IStorage;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.inventory.container.GenericContainer;
import com.craftingdead.mod.util.Text;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageItem extends Item {

  public static final int MAX_ROWS_TO_SHOW = 6;

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

  @Override
  public void addInformation(ItemStack backpackStack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(backpackStack, world, lines, tooltipFlag);

    backpackStack.getCapability(ModCapabilities.STORAGE).ifPresent(storage -> {
      if (!storage.isEmpty()) {
        lines.add(Text.of(" "));
        lines.add(Text.translate("container.inventory").applyTextStyles(TextFormatting.RED,
            TextFormatting.BOLD));

        int rowsBeyondLimit = 0;

        for (int i = 0; i < storage.getSlots(); i++) {
          ItemStack stack = storage.getStackInSlot(i);
          if (!stack.isEmpty()) {
            if (i >= MAX_ROWS_TO_SHOW) {
              ++rowsBeyondLimit;
            } else {
              ITextComponent amountText =
                  Text.of(stack.getCount() + "x ").applyTextStyle(TextFormatting.DARK_GRAY);
              ITextComponent itemText = stack.getDisplayName().applyTextStyle(TextFormatting.GRAY);
              lines.add(amountText.appendSibling(itemText));
            }
          }
        }

        if (rowsBeyondLimit > 0) {
          lines.add(Text.of(". . . +" + rowsBeyondLimit).applyTextStyle(TextFormatting.RED));
        }
      }
    });
  }
}
