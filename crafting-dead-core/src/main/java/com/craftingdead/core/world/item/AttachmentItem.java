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
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
  public void appendHoverText(ItemStack stack, Level world,
      List<Component> lines, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);

    for (Entry<Attachment.MultiplierType, Float> entry : this.asAttachment()
        .getMultipliers().entrySet()) {
      lines.add(new TranslatableComponent(entry.getKey().getTranslationKey())
          .withStyle(ChatFormatting.GRAY)
          .append(new TextComponent(" " + entry.getValue() + "x")
              .withStyle(ChatFormatting.RED)));
    }
  }

  @Override
  public Attachment asAttachment() {
    return this.attachment.get();
  }
}
