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
    return ActionResult.consume(itemstack);
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);
    lines.add(Text.translate("item_lore." + this.getRegistryName().getPath(), RANGE)
        .applyTextStyle(TextFormatting.GRAY));
  }
}
