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
package com.craftingdead.core.client.gui;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.item.GrenadeItem;
import com.craftingdead.core.item.GunItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class KillFeedEntry {

  private final PlayerEntity playerEntity;
  private final LivingEntity deadEntity;
  private final ItemStack weaponStack;
  private final Type type;

  public KillFeedEntry(PlayerEntity playerEntity, LivingEntity deadEntity, ItemStack weaponStack,
      Type type) {
    this.playerEntity = playerEntity;
    this.deadEntity = deadEntity;
    this.weaponStack = weaponStack;
    this.type = type;
  }

  public void render(float x, float y, float alpha) {
    final Minecraft minecraft = Minecraft.getInstance();

    final String playerEntityName = this.playerEntity.getDisplayName().getFormattedText();
    final String deadEntityName = this.deadEntity.getDisplayName().getFormattedText();
    final int playerEntityNameWidth = minecraft.fontRenderer.getStringWidth(playerEntityName);
    final int deadEntityNameWidth = minecraft.fontRenderer.getStringWidth(deadEntityName);

    int spacing = 20;
    alpha *= minecraft.player == this.playerEntity ? 0.7F : 0.5F;
    boolean renderWeaponStack = true;

    switch (this.type) {
      case WALLBANG_HEADSHOT:
        spacing += 16;
      case HEADSHOT:
      case WALLBANG:
        spacing += 16;
        break;
      default:
        break;
    }

    final int opacity = Math.min((int) (alpha * 255.0F), 255);
    if (opacity < 8) {
      return;
    }

    int colour = 0x000000 + (opacity << 24);
    RenderUtil.drawGradientRectangle(x, y,
        x + playerEntityNameWidth + deadEntityNameWidth + spacing, y + 11, colour, colour);

    minecraft.fontRenderer.drawStringWithShadow(
        this.playerEntity.getDisplayName().getFormattedText(),
        x + 2, y + 2, 0xFFFFFF + ((int) (alpha * 255.0F) << 24));
    minecraft.fontRenderer.drawStringWithShadow(this.deadEntity.getDisplayName().getFormattedText(),
        x + playerEntityNameWidth + spacing - 1, y + 2, 0xFFFFFF + (opacity << 24));

    switch (this.type) {
      case HEADSHOT:
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/headshot.png"));
        RenderUtil.drawTexturedRectangle(x + playerEntityNameWidth + 17, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      case WALLBANG:
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/wallbang.png"));
        RenderUtil.drawTexturedRectangle(x + playerEntityNameWidth + 35, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      case WALLBANG_HEADSHOT:
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/wallbang.png"));
        RenderUtil.drawTexturedRectangle(x + playerEntityNameWidth + 35, y - 1, 12, 12);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/headshot.png"));
        RenderUtil.drawTexturedRectangle(x + playerEntityNameWidth + 35 + 14, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      default:
        break;
    }

    if (this.weaponStack != null && renderWeaponStack) {
      RenderSystem.pushMatrix();
      {
        RenderSystem.translated(x + playerEntityNameWidth + 4, y - 1, 0);

        if (this.weaponStack.getItem() instanceof GunItem) {
          double scale = 0.75D;
          RenderSystem.scaled(scale, scale, scale);
        }

        // if (this.itemStack.getItem() instanceof ItemKnife) {
        // double scale = 0.6D;
        // GL11.glScaled(scale, scale, scale);
        // GL11.glRotated(180, 0, 1, 0);
        // GL11.glRotated(-20, 0, 0, 1);
        // }

        if (this.weaponStack.getItem() instanceof GrenadeItem) {
          double scale = 0.8D;
          RenderSystem.scaled(scale, scale, scale);
          RenderSystem.translated(4, 1, 0);
        }

        RenderUtil.renderItemIntoGUI(this.weaponStack, 0, 0, 0xFFFFFF + (opacity << 24));
      }
      RenderSystem.popMatrix();
    }
  }

  public static enum Type {
    NONE, HEADSHOT, WALLBANG, WALLBANG_HEADSHOT;
  }
}
