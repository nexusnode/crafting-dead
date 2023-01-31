/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.world.entity.extension;

import java.util.Optional;
import java.util.UUID;
import com.craftingdead.core.world.entity.extension.BasicLivingExtension;
import com.craftingdead.core.world.entity.extension.LivingHandler;
import com.craftingdead.core.world.entity.extension.LivingHandlerType;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.tags.SurvivalItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ZombieHandler implements LivingHandler {

  public static final LivingHandlerType<ZombieHandler> TYPE =
      new LivingHandlerType<>(new ResourceLocation(CraftingDeadSurvival.ID, "zombie"));

  private static final UUID DAMAGE_MODIFIER_BABY_UUID =
      UUID.fromString("53405062-b8d8-461c-a542-26b0be8ed481");
  private static final AttributeModifier DAMAGE_MODIFIER_BABY =
      new AttributeModifier(DAMAGE_MODIFIER_BABY_UUID, "Baby damage reduction", -1.5D,
          AttributeModifier.Operation.MULTIPLY_BASE);

  private static final UUID HEALTH_MODIFIER_BABY_UUID =
      UUID.fromString("69d754ea-1ae3-4684-bb69-51a29de92b9a");
  private static final AttributeModifier HEALTH_MODIFIER_BABY =
      new AttributeModifier(HEALTH_MODIFIER_BABY_UUID, "Baby health reduction", -1.5D,
          AttributeModifier.Operation.MULTIPLY_BASE);

  protected final BasicLivingExtension<Zombie> extension;

  private int textureIndex;

  public ZombieHandler(BasicLivingExtension<Zombie> extension) {
    this.extension = extension;
    this.textureIndex = extension.random().nextInt(23);
  }

  public int getTextureIndex() {
    return this.textureIndex;
  }

  public void handleSetBaby(boolean baby) {
    if (this.extension.level().isClientSide()) {
      return;
    }
    var zombie = this.extension.entity();
    var damageAttribute = zombie.getAttribute(Attributes.ATTACK_DAMAGE);
    var healthAttribute = zombie.getAttribute(Attributes.MAX_HEALTH);
    damageAttribute.removeModifier(DAMAGE_MODIFIER_BABY);
    healthAttribute.removeModifier(HEALTH_MODIFIER_BABY);
    if (baby) {
      damageAttribute.addTransientModifier(DAMAGE_MODIFIER_BABY);
      healthAttribute.addTransientModifier(HEALTH_MODIFIER_BABY);
    }
  }

  public void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
    var zombie = this.extension.entity();
    zombie.setItemSlot(EquipmentSlot.MAINHAND, this.createHeldItem());
    this.extension.getItemHandler().setStackInSlot(ModEquipmentSlot.CLOTHING.getIndex(),
        this.createClothingItem());
    this.extension.getItemHandler().setStackInSlot(ModEquipmentSlot.HAT.getIndex(),
        this.getHatStack());
  }

  protected ItemStack createHeldItem() {
    return this
        .getRandomItem(SurvivalItemTags.ZOMBIE_HAND_LOOT,
            CraftingDeadSurvival.serverConfig.zombieHandSpawnChance.get().floatValue())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  protected ItemStack createClothingItem() {
    return this
        .getRandomItem(SurvivalItemTags.ZOMBIE_CLOTHING_LOOT,
            CraftingDeadSurvival.serverConfig.zombieClothingSpawnChance.get().floatValue())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  protected ItemStack getHatStack() {
    return this
        .getRandomItem(SurvivalItemTags.ZOMBIE_HAT_LOOT,
            CraftingDeadSurvival.serverConfig.zombieHatSpawnChance.get().floatValue())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  @SuppressWarnings("deprecation")
  protected Optional<Item> getRandomItem(TagKey<Item> tagKey, float probability) {
    var random = this.extension.random();
    return random.nextFloat() < probability
        ? Registry.ITEM.getTag(tagKey)
            .flatMap(tag -> tag.getRandomElement(random))
            .map(Holder::value)
        : Optional.empty();
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    out.writeVarInt(this.textureIndex);
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.textureIndex = in.readVarInt();
  }

  @Override
  public boolean requiresSync() {
    return false;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    tag.putInt("textureIndex", this.textureIndex);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.textureIndex = tag.getInt("textureIndex");
  }
}
