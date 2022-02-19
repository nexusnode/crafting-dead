/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.action.reload;

import java.util.List;
import java.util.Optional;
import com.craftingdead.core.event.CollectMagazineItemHandlers;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.item.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MagazineReloadAction extends AbstractReloadAction {

  private final MagazineAmmoProvider ammoProvider;

  private ItemStack newMagazineStack;

  private MagazineLocation magazineLocation;

  public MagazineReloadAction(LivingExtension<?, ?> performer) {
    super(performer);
    var ammoProvider = this.gun.getAmmoProvider();
    if (!(ammoProvider instanceof MagazineAmmoProvider)) {
      throw new IllegalStateException("No MagazineAmmoProvider present");
    }
    this.ammoProvider = (MagazineAmmoProvider) ammoProvider;
  }

  @Override
  public ActionType<?> getType() {
    return ActionTypes.MAGAZINE_RELOAD.get();
  }

  @Override
  public boolean start(boolean simulate) {
    var result = this.findMagazine(this.getPerformer());
    if (!result.isPresent()) {
      return false;
    }

    if (!simulate) {
      this.magazineLocation = result.get();
      this.newMagazineStack =
          this.magazineLocation.itemHandler.extractItem(this.magazineLocation.slot, 1, false);
    }

    return true;
  }

  @Override
  protected void loadNewMagazineStack(boolean displayOnly) {
    this.ammoProvider.setMagazineStack(this.newMagazineStack);
    if (!displayOnly
        && !this.oldMagazineStack.isEmpty()
        && this.getPerformer().getEntity() instanceof Player) {
      ((Player) this.getPerformer().getEntity()).addItem(this.oldMagazineStack);
    }
  }

  @Override
  protected void revert() {
    this.ammoProvider.setMagazineStack(this.oldMagazineStack);
    var remainingStack = this.magazineLocation.itemHandler().insertItem(
        this.magazineLocation.slot(), this.newMagazineStack, false);
    this.getPerformer().getEntity().spawnAtLocation(remainingStack);
  }

  private List<IItemHandler> collectItemHandlers(LivingExtension<?, ?> living) {
    var builder = ImmutableList.<IItemHandler>builder();

    var event = new CollectMagazineItemHandlers(living);
    MinecraftForge.EVENT_BUS.post(event);
    builder.addAll(event.getItemHandlers());

    // Vest - first
    living.getItemHandler().getStackInSlot(ModEquipmentSlot.VEST.getIndex())
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(builder::add);

    // Backpack - second
    living.getItemHandler().getStackInSlot(ModEquipmentSlot.BACKPACK.getIndex())
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(builder::add);

    // Inventory - third
    living.getEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        .ifPresent(builder::add);

    return builder.build();
  }

  private Optional<MagazineLocation> findMagazine(LivingExtension<?, ?> living) {
    for (var itemHandler : this.collectItemHandlers(living)) {
      for (int i = 0; i < itemHandler.getSlots(); ++i) {
        var itemStack = itemHandler.getStackInSlot(i);
        if (this.gun.getAcceptedMagazines().contains(itemStack.getItem())
            && !itemStack.getCapability(Magazine.CAPABILITY)
                .map(Magazine::isEmpty)
                .orElse(true)) {
          return Optional.of(new MagazineLocation(itemHandler, i));
        }
      }
    }
    return Optional.empty();
  }

  private record MagazineLocation(IItemHandler itemHandler, int slot) {}
}
