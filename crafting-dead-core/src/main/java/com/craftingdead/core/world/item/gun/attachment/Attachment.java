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

package com.craftingdead.core.world.item.gun.attachment;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Attachment extends ForgeRegistryEntry<Attachment> implements ItemLike {

  private final Map<MultiplierType, Float> multipliers;
  private final GunCraftSlotType inventorySlot;
  private final boolean soundSuppressor;
  private final boolean scope;
  private final Supplier<? extends Item> item;

  @Nullable
  private String descriptionId;

  private Attachment(Builder builder) {
    this.multipliers = builder.multipliers;
    this.inventorySlot = builder.inventorySlot;
    this.soundSuppressor = builder.soundSuppressor;
    this.scope = builder.scope;
    this.item = builder.item;
  }

  public Map<MultiplierType, Float> getMultipliers() {
    return Collections.unmodifiableMap(this.multipliers);
  }

  public float getMultiplier(MultiplierType multiplierType) {
    return this.multipliers.getOrDefault(multiplierType, 1.0F);
  }

  public GunCraftSlotType getInventorySlot() {
    return this.inventorySlot;
  }

  public boolean isSoundSuppressor() {
    return this.soundSuppressor;
  }

  public boolean isScope() {
    return this.scope;
  }

  protected String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("attachment", this.getRegistryName());
    }
    return this.descriptionId;
  }

  public String getDescriptionId() {
    return this.getOrCreateDescriptionId();
  }

  public Component getDescription() {
    return new TranslatableComponent(this.getDescriptionId());
  }

  @Override
  public Item asItem() {
    return this.item.get();
  }

  public static Builder builder() {
    return new Attachment.Builder();
  }

  public static enum MultiplierType {

    DAMAGE("damage"), ACCURACY("accuracy"), ZOOM("zoom");

    private final String name;

    private MultiplierType(String name) {
      this.name = name;
    }

    public String getTranslationKey() {
      return "attachment_multiplier_type." + this.name;
    }
  }

  public static class Builder {

    private final Map<MultiplierType, Float> multipliers = new EnumMap<>(MultiplierType.class);
    private GunCraftSlotType inventorySlot;
    private boolean soundSuppressor;
    private boolean scope;
    private Supplier<? extends Item> item;

    public Builder addMultiplier(MultiplierType modifierType, float multiplier) {
      this.multipliers.put(modifierType, multiplier);
      return this;
    }

    public Builder setSoundSuppressor(boolean soundSuppressor) {
      this.soundSuppressor = soundSuppressor;
      return this;
    }

    public Builder setInventorySlot(GunCraftSlotType inventorySlot) {
      this.inventorySlot = inventorySlot;
      return this;
    }

    public Builder setScope(boolean scope) {
      this.scope = scope;
      return this;
    }

    public Builder setItem(Supplier<? extends Item> item) {
      this.item = item;
      return this;
    }

    public Attachment build() {
      return new Attachment(this);
    }
  }
}
