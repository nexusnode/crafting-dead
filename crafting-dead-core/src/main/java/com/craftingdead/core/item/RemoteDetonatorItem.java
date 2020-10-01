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

import java.util.List;
import com.craftingdead.core.entity.grenade.GrenadeEntity;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class RemoteDetonatorItem extends Item {

  public static final int RANGE = 50;

  public RemoteDetonatorItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity,
      Hand hand) {
    ItemStack itemstack = playerEntity.getHeldItem(hand);
    playerEntity.setActiveHand(hand);

    if (world instanceof ServerWorld) {
      ServerWorld serverWorld = (ServerWorld) world;

      serverWorld.playMovingSound(null, playerEntity, SoundEvents.UI_BUTTON_CLICK,
          SoundCategory.PLAYERS, 0.8F, 1.2F);

      serverWorld.getEntitiesInAABBexcluding(playerEntity,
          playerEntity.getBoundingBox().grow(RANGE), (entity) -> {
            if (!(entity instanceof GrenadeEntity)) {
              return false;
            }
            GrenadeEntity grenadeEntity = (GrenadeEntity) entity;

            boolean isOwner =
                grenadeEntity.getThrower().map(thrower -> thrower == playerEntity).orElse(false);

            return isOwner && grenadeEntity.canBeRemotelyActivated();
          }).forEach(entity -> ((GrenadeEntity) entity).setActivated(true));
    }
    return ActionResult.resultConsume(itemstack);
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);
    lines.add(Text.translate("item_lore." + this.getRegistryName().getPath(), RANGE)
        .applyTextStyle(TextFormatting.GRAY));
  }
}
