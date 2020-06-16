package com.craftingdead.core.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MeleeWeaponItem extends ToolItem {

  private final int attackDamage;
  private final double attackSpeed;

  public MeleeWeaponItem(int attackDamage, double attackSpeed, Item.Properties properties) {
    super(properties);
    this.attackSpeed = attackSpeed;
    this.attackDamage = attackDamage;
  }

  @Override
  public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.damageItem(this.attackDamage, attacker, (entity) -> {
      entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
    });
    return true;
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot,
      ItemStack itemStack) {
    Multimap<String, AttributeModifier> multimap =
        super.getAttributeModifiers(equipmentSlot, itemStack);
    if (equipmentSlot == EquipmentSlotType.MAINHAND) {
      multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
          new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier",
              (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
      multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
          new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.attackSpeed,
              AttributeModifier.Operation.ADDITION));
    }
    return multimap;
  }
}
