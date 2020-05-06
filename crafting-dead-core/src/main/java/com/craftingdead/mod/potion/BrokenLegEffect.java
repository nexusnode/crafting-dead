package com.craftingdead.mod.potion;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BrokenLegEffect extends Effect {

  public BrokenLegEffect() {
    super(EffectType.HARMFUL, 0x816C5A);
    this
        .addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
            "021BEAA1-498F-4D7B-933E-F0FA0B88B9D1", (double) -0.15F,
            AttributeModifier.Operation.MULTIPLY_TOTAL);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative()) {
      livingEntity.removePotionEffect(ModEffects.BROKEN_LEG.get());
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(ModItems.FIRST_AID_KIT::get));
    items.add(new ItemStack(ModItems.MORPHINE_SYRINGE::get));
    items.add(new ItemStack(ModItems.SPLINT::get));
    items.add(new ItemStack(ModItems.ANTIBIOTICS::get));
    return items;
  }
}
