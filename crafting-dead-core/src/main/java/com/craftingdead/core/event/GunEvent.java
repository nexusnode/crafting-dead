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

package com.craftingdead.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.item.AttachmentItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.IItemHandler;

public abstract class GunEvent extends Event {

  private final IGun gun;
  private final ItemStack itemStack;

  public GunEvent(IGun gun, ItemStack itemStack) {
    this.gun = gun;
    this.itemStack = itemStack;
  }

  public IGun getGun() {
    return gun;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public static class CollectAmmoProviders extends GunEvent {

    private final ILiving<?, ?> living;

    private final List<IItemHandler> ammoProviders = new ArrayList<>();

    public CollectAmmoProviders(IGun gun, ItemStack itemStack, ILiving<?, ?> living) {
      super(gun, itemStack);
      this.living = living;
    }

    public ILiving<?, ?> getLiving() {
      return living;
    }

    public void addAmmoProvider(IItemHandler ammoProvider) {
      this.ammoProviders.add(ammoProvider);
    }

    public Collection<IItemHandler> getAmmoProviders() {
      return Collections.unmodifiableCollection(this.ammoProviders);
    }
  }

  @Cancelable
  public static class TriggerPressed extends GunEvent {

    private final ILiving<?, ?> living;

    public TriggerPressed(IGun gun, ItemStack itemStack, ILiving<?, ?> living) {
      super(gun, itemStack);
      this.living = living;
    }

    public ILiving<?, ?> getLiving() {
      return living;
    }
  }

  public static class Initialize extends GunEvent {

    private final NonNullList<ItemStack> ammoReserve = NonNullList.create();
    private final Set<AttachmentItem> attachments = new HashSet<>();

    public Initialize(IGun gun, ItemStack itemStack) {
      super(gun, itemStack);
    }

    public void addMagazineStack(ItemStack itemStack) {
      if (!this.getGun().getAcceptedMagazines().contains(itemStack.getItem())) {
        throw new IllegalArgumentException("Invalid magazine");
      }
      this.ammoReserve.add(itemStack);
    }

    public Collection<ItemStack> getAmmoReserve() {
      return Collections.unmodifiableCollection(this.ammoReserve);
    }

    public void addAttachment(AttachmentItem attachment) {
      this.attachments.add(attachment);
    }

    public Set<AttachmentItem> getAttachments() {
      return Collections.unmodifiableSet(this.attachments);
    }
  }

  public static class ReloadFinish extends GunEvent {

    private ItemStack oldMagazineStack;
    private ItemStack newMagazineStack;

    public ReloadFinish(IGun gun, ItemStack itemStack, ItemStack oldMagazineStack,
        ItemStack newMagazineStack) {
      super(gun, itemStack);
      this.oldMagazineStack = oldMagazineStack;
      this.newMagazineStack = newMagazineStack;
    }

    public ItemStack getOldMagazineStack() {
      return this.oldMagazineStack;
    }

    public void setOldMagazineStack(ItemStack oldMagazineStack) {
      this.oldMagazineStack = oldMagazineStack;
    }

    public ItemStack getNewMagazineStack() {
      return this.newMagazineStack;
    }

    public void setNewMagazineStack(ItemStack newMagazineStack) {
      this.newMagazineStack = newMagazineStack;
    }
  }
}
