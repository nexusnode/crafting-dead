package com.craftingdead.core.world.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class HandcuffsKeyItem extends Item {

  public HandcuffsKeyItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity livingEntity, InteractionHand hand) {
    if (livingEntity instanceof ServerPlayer playerHit) {
      final var extension = PlayerExtension.getOrThrow(playerHit);
      final var handcuffs = extension.getHandcuffs();
      if (!handcuffs.isEmpty()) {
        extension.setHandcuffs(ItemStack.EMPTY);

        playerHit.displayClientMessage(
            new TranslatableComponent("handcuffs_key.removed").withStyle(ChatFormatting.RED),
            true);

        if (!player.addItem(handcuffs)) {
          player.drop(handcuffs, false);
        }
      }
    }


    return InteractionResult.sidedSuccess(player.getLevel().isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack item, @Nullable Level level,
      List<Component> lines, TooltipFlag tooltipFlag) {
    lines.add(
        new TranslatableComponent("handcuffs_key.information").withStyle(ChatFormatting.GRAY));
  }
}
