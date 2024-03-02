/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.action.reload;

import java.util.List;
import java.util.Optional;
import com.craftingdead.core.ServerConfig;
import com.craftingdead.core.event.CollectMagazineItemHandlers;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.equipment.Equipment;
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
  public ActionType<?> type() {
    return ActionTypes.MAGAZINE_RELOAD.get();
  }

  @Override
  public boolean start(boolean simulate) {
	if(this.performer.entity().isCrouching()) {
		return false;	
	}	
    var result = this.findMagazine(this.performer());
    if (!result.isPresent()) {
      return false;
    }

    if (!simulate) {
      this.magazineLocation = result.get();
      this.newMagazineStack =
          this.magazineLocation.itemHandler.extractItem(this.magazineLocation.slot, 1, false);
    }

    return super.start(simulate);
  }

  @Override
  protected void loadNewMagazineStack(boolean displayOnly) {
	  
	if(this.performer.entity().isCrouching()) {
		this.revert();
		return;	
	}
	  
    this.ammoProvider.setMagazineStack(this.newMagazineStack);
    if (!displayOnly
        && !this.oldMagazineStack.isEmpty()
        && this.performer().entity() instanceof Player
        && !(this.oldMagazineStack.getCapability(Magazine.CAPABILITY).map(Magazine::isEmpty)
            .orElse(true)
            && ServerConfig.instance.reloadDestroyMagWhenEmpty.get())) {
      ((Player) this.performer().entity()).addItem(this.oldMagazineStack);
    }
  }

  @Override
  protected void revert() {
    this.ammoProvider.setMagazineStack(this.oldMagazineStack);
    var remainingStack = this.magazineLocation.itemHandler().insertItem(
        this.magazineLocation.slot(), this.newMagazineStack, false);
    this.performer().entity().spawnAtLocation(remainingStack);
  }

  private List<IItemHandler> collectItemHandlers(LivingExtension<?, ?> living) {
    var builder = ImmutableList.<IItemHandler>builder();

    var event = new CollectMagazineItemHandlers(living);
    MinecraftForge.EVENT_BUS.post(event);
    builder.addAll(event.getItemHandlers());

    // Vest - first
    living.getItemInSlot(Equipment.Slot.VEST)
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        .ifPresent(builder::add);

    // Backpack - second
    living.getItemInSlot(Equipment.Slot.BACKPACK)
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        .ifPresent(builder::add);

    // Inventory - third
    living.entity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
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
