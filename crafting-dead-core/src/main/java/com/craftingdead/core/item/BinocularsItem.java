package com.craftingdead.core.item;

import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.scope.DefaultScope;
import com.craftingdead.core.util.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BinocularsItem extends Item {

  private static final ResourceLocation SCOPE_OVERLAY_TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/scope/binoculars.png");

  public BinocularsItem(Properties properties) {
    super(properties);
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    return 72000;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity,
      Hand hand) {
    ItemStack itemstack = playerEntity.getHeldItem(hand);
    playerEntity.setActiveHand(hand);
    playerEntity.playSound(ModSoundEvents.SCOPE_ZOOM.get(), 0.75F, 1.0F);
    if (world.isRemote()) {
      Minecraft mc = Minecraft.getInstance();
      mc.gameSettings.thirdPersonView = 0;
    }
    return ActionResult.consume(itemstack);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(new DefaultScope(1 / 14, SCOPE_OVERLAY_TEXTURE, 2048, 512),
        () -> ModCapabilities.SCOPE);
  }
}
