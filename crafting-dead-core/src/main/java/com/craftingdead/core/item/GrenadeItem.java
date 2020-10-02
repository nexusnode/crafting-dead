/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.item;


import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import com.craftingdead.core.entity.grenade.GrenadeEntity;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GrenadeItem extends Item {

  private final BiFunction<LivingEntity, World, GrenadeEntity> grenadeEntitySupplier;
  private final float throwSpeed;

  public GrenadeItem(Properties properties) {
    super(properties);
    this.grenadeEntitySupplier = properties.grenadeEntitySupplier;
    this.throwSpeed = properties.throwSpeed;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world,
      List<ITextComponent> texts, ITooltipFlag tooltipFlag) {
    texts
        .add(Text.translate("item_lore.grenade").applyTextStyle(TextFormatting.GRAY));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    worldIn
        .playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
            SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
            0.4F / (random.nextFloat() * 0.4F + 0.8F));
    if (!worldIn.isRemote) {
      GrenadeEntity grenadeEntity = grenadeEntitySupplier.apply(playerIn, worldIn);

      float force = playerIn.isSneaking() ? 0.4F : this.throwSpeed;
      grenadeEntity.setPosition(playerIn.getPosX(), playerIn.getPosY() + playerIn.getEyeHeight(),
          playerIn.getPosZ());
      grenadeEntity.shootFromEntity(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0,
          force, 1.0F);
      worldIn.addEntity(grenadeEntity);
    }

    playerIn.addStat(Stats.ITEM_USED.get(this));
    if (!playerIn.abilities.isCreativeMode) {
      itemStack.shrink(1);
    }

    return ActionResult.resultSuccess(itemStack);
  }

  public static class Properties extends Item.Properties {

    private BiFunction<LivingEntity, World, GrenadeEntity> grenadeEntitySupplier;
    private float throwSpeed = 1.45F;

    public Properties setGrenadeEntitySupplier(
        BiFunction<LivingEntity, World, GrenadeEntity> grenadeEntitySupplier) {
      this.grenadeEntitySupplier = grenadeEntitySupplier;
      return this;
    }

    public Properties setThrowSpeed(float throwSpeed) {
      this.throwSpeed = throwSpeed;
      return this;
    }
  }
}
