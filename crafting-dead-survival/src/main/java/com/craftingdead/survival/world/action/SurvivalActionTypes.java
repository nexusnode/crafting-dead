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

package com.craftingdead.survival.world.action;

import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.TargetSelector;
import com.craftingdead.core.world.action.item.BlockItemActionType;
import com.craftingdead.core.world.action.item.EntityItemActionType;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.clothing.Clothing;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      DeferredRegister.create((Class<ActionType<?>>) (Object) ActionType.class,
          CraftingDeadSurvival.ID);

  public static final RegistryObject<EntityItemActionType<?>> SHRED_CLOTHING =
      ACTION_TYPES.register("shred_clothing",
          () -> EntityItemActionType.builder(TargetSelector.SELF_ONLY)
              .forItem(CapabilityUtil.capabilityPresent(Clothing.CAPABILITY))
              .customAction((performer, target) -> {
                var random = target.getRandom();
                int randomRagAmount = random.nextInt(3) + 3;

                for (int i = 0; i < randomRagAmount; i++) {
                  if (random.nextBoolean()) {
                    target.getEntity().spawnAtLocation(
                        new ItemStack(SurvivalItems.CLEAN_RAG::get));
                  } else {
                    target.getEntity().spawnAtLocation(
                        new ItemStack(SurvivalItems.DIRTY_RAG::get));
                  }
                }
              }, 1.0F)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_SPLINT =
      ACTION_TYPES.register("use_splint",
          () -> EntityItemActionType
              .builder(TargetSelector.SELF_OR_OTHERS.hasEffect(SurvivalMobEffects.BROKEN_LEG))
              .forItem(SurvivalItems.SPLINT)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_CLEAN_RAG =
      ACTION_TYPES.register("use_clean_rag",
          () -> EntityItemActionType
              .builder(TargetSelector.SELF_OR_OTHERS.hasEffect(SurvivalMobEffects.BLEEDING))
              .forItem(SurvivalItems.CLEAN_RAG)
              .duration(16)
              .returnItem(SurvivalItems.BLOODY_RAG)
              .build());

  public static final RegistryObject<BlockItemActionType> WASH_RAG =
      ACTION_TYPES.register("wash_rag",
          () -> BlockItemActionType.builder()
              .forItem(itemStack -> itemStack.is(SurvivalItems.DIRTY_RAG.get())
                  || itemStack.is(SurvivalItems.BLOODY_RAG.get()))
              .returnItem(SurvivalItems.CLEAN_RAG)
              .consumeItemInCreative(true)
              .finishSound(SoundEvents.BUCKET_FILL)
              .forBlock(blockState -> blockState.getFluidState().is(Fluids.WATER))
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_SYRINGE_ON_ZOMBIE =
      ACTION_TYPES.register("use_syringe_on_zombie",
          () -> EntityItemActionType.builder(TargetSelector.OTHERS_ONLY.ofEntityType(Zombie.class))
              .forItem(ModItems.SYRINGE)
              .duration(16)
              .customAction((performer, target) -> target.getEntity().hurt(
                  DamageSource.mobAttack(target.getEntity()), 2.0F), 0.25F)
              .returnItem(SurvivalItems.RBI_SYRINGE)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_CURE_SYRINGE =
      ACTION_TYPES.register("use_cure_syringe",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(SurvivalItems.CURE_SYRINGE)
              .duration(16)
              .returnItem(ModItems.SYRINGE)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_RBI_SYRINGE =
      ACTION_TYPES.register("use_rbi_syringe",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(SurvivalItems.RBI_SYRINGE)
              .duration(16)
              .effect(() -> new MobEffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999))
              .returnItem(ModItems.SYRINGE)
              .build());
}
