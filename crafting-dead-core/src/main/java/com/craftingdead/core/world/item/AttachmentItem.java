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

package com.craftingdead.core.world.item;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.AttachmentLike;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class AttachmentItem extends Item implements AttachmentLike {

  private final Supplier<Attachment> attachment;

  public AttachmentItem(Supplier<Attachment> attachment, Properties properties) {
    super(properties);
    this.attachment = attachment;
  }

  @Override
  public String getDescriptionId() {
    return this.attachment.get().getDescriptionId();
  }

  @Override
  public void appendHoverText(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);

    for (Entry<Attachment.MultiplierType, Float> entry : this.asAttachment()
        .getMultipliers().entrySet()) {
      lines.add(new TranslationTextComponent(entry.getKey().getTranslationKey())
          .withStyle(TextFormatting.GRAY)
          .append(new StringTextComponent(" " + entry.getValue() + "x")
              .withStyle(TextFormatting.RED)));
    }
  }

  @Override
  public Attachment asAttachment() {
    return this.attachment.get();
  }
}
