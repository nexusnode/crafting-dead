/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {

  @Inject(at = @At("TAIL"), method = "setupAnim")
  private void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
    if (entity instanceof Player playerEntity) {
      final var player = PlayerExtension.get(playerEntity);
      if (player != null) {
        final var model = (HumanoidModel<?>) (Object) this;
        model.rightArmPose = ArmPose.EMPTY;
        model.leftArmPose = ArmPose.EMPTY;
        if (player.isHandcuffed()) {
          final var xRot = 0.5F;
          final var zRot = 0.25F;
          model.rightArm.xRot = xRot;
          model.rightArm.zRot = -zRot;

          model.leftArm.xRot = xRot;
          model.leftArm.zRot = zRot;
        } else if (player.getMainHandItem().is(ModItems.MINIGUN.get())) {
          model.rightArm.xRot = 31F;
          model.leftArm.xRot = 30.5F;
        }
      }
    }
  }
}
