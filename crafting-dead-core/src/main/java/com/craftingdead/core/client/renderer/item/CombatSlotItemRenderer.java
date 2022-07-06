package com.craftingdead.core.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public interface CombatSlotItemRenderer extends CustomItemRenderer {

  void renderInCombatSlot(ItemStack itemStack, PoseStack poseStack, float partialTick,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay);
}
