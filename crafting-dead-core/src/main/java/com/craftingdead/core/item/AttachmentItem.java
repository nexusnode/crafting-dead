/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class AttachmentItem extends Item {

  private final Map<MultiplierType, Float> multipliers;
  private final CraftingInventorySlotType inventorySlot;
  private final boolean soundSuppressor;
  private final boolean scope; 

  public AttachmentItem(Properties properties) {
    super(properties);
    this.multipliers = properties.multipliers;
    this.inventorySlot = properties.inventorySlot;
    this.soundSuppressor = properties.soundSuppressor;
    this.scope = properties.scope;
  }

  public Float getMultiplier(MultiplierType multiplierType) {
    return this.multipliers.getOrDefault(multiplierType, 1.0F);
  }

  public CraftingInventorySlotType getInventorySlot() {
    return this.inventorySlot;
  }

  public boolean isSoundSuppressor() {
    return this.soundSuppressor;
  }
  
  public boolean isScope() {
    return this.scope;
  }
  
  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    for (Entry<MultiplierType, Float> entry : this.multipliers.entrySet()) {
      lines.add(Text.translate(entry.getKey().getTranslationKey())
          .applyTextStyle(TextFormatting.GRAY)
          .appendSibling(Text.of(" " + entry.getValue() + "x").applyTextStyle(TextFormatting.RED)));
    }
  }

  public static enum MultiplierType {
    DAMAGE, ACCURACY, ZOOM;

    private final String translationKey = "attachment_multiplier_type." + this.name().toLowerCase();

    public String getTranslationKey() {
      return this.translationKey;
    }
  }

  public static class Properties extends Item.Properties {

    private final Map<MultiplierType, Float> multipliers = new EnumMap<>(MultiplierType.class);
    private CraftingInventorySlotType inventorySlot;
    private boolean soundSuppressor;
    private boolean scope;

    public Properties addMultiplier(MultiplierType modifierType, float multiplier) {
      this.multipliers.put(modifierType, multiplier);
      return this;
    }

    public Properties setSoundSuppressor(boolean soundSuppressor) {
      this.soundSuppressor = soundSuppressor;
      return this;
    }

    public Properties setInventorySlot(CraftingInventorySlotType inventorySlot) {
      this.inventorySlot = inventorySlot;
      return this;
    }
    
    public Properties setScope(boolean scope) {
      this.scope = scope;
      return this;
    }
  }
}
