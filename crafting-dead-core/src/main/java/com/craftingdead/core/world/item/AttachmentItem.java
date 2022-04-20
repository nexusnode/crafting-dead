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
