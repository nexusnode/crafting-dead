package com.craftingdead.core.world.item;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BoltCuttersItem extends MeleeWeaponItem {

  private final int damageToHandcuffs;

  public BoltCuttersItem(int damageToHandcuffs, int attackDamage, double attackSpeed,
      Properties properties) {
    super(attackDamage, attackSpeed, properties);
    this.damageToHandcuffs = damageToHandcuffs;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity livingEntity, InteractionHand hand) {
    if (livingEntity instanceof ServerPlayer playerHit) {
      var extension = PlayerExtension.getOrThrow(playerHit);
      if (extension.isHandcuffed()) {
        if (extension.damageHandcuffs(this.damageToHandcuffs)) {
          playerHit.displayClientMessage(
              new TranslatableComponent("bolt_cutters.free", player.getDisplayName()), true);
        } else {
          playerHit.getLevel().playLocalSound(playerHit.getX(), playerHit.getY(),
              playerHit.getZ(), SoundEvents.ITEM_BREAK,
              playerHit.getSoundSource(), 0.2F, 0.5F, false);
        }

        itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      }
    }
    return InteractionResult.sidedSuccess(player.getLevel().isClientSide());
  }
}
