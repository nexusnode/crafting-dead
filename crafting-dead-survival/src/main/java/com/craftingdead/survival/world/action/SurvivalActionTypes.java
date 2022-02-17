/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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
import com.craftingdead.core.world.action.delegate.DelegateBlockActionType;
import com.craftingdead.core.world.action.delegate.DelegateEntityActionType;
import com.craftingdead.core.world.action.item.ItemActionType;
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

  public static final DeferredRegister<ActionType> ACTION_TYPES =
      DeferredRegister.create(ActionType.class, CraftingDeadSurvival.ID);

  public static final RegistryObject<ActionType> SHRED_CLOTHING =
      ACTION_TYPES.register("shred_clothing",
          () -> ItemActionType.builder()
              .forItem(CapabilityUtil.capabilityPresent(Clothing.CAPABILITY))
              .delegate(DelegateEntityActionType.builder(TargetSelector.SELF_ONLY)
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
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_SPLINT =
      ACTION_TYPES.register("use_splint",
          () -> ItemActionType.builder()
              .forItem(SurvivalItems.SPLINT)
              .delegate(DelegateEntityActionType
                  .builder(TargetSelector.SELF_OR_OTHERS.hasEffect(SurvivalMobEffects.BROKEN_LEG))
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_CLEAN_RAG =
      ACTION_TYPES.register("use_clean_rag",
          () -> ItemActionType.builder()
              .forItem(SurvivalItems.CLEAN_RAG)
              .duration(16)
              .delegate(DelegateEntityActionType
                  .builder(TargetSelector.SELF_OR_OTHERS.hasEffect(SurvivalMobEffects.BLEEDING))
                  .returnItem(SurvivalItems.BLOODY_RAG)
                  .build())
              .build());

  public static final RegistryObject<ActionType> WASH_RAG =
      ACTION_TYPES.register("wash_rag",
          () -> ItemActionType.builder()
              .forItem(itemStack -> itemStack.is(SurvivalItems.DIRTY_RAG.get())
                  || itemStack.is(SurvivalItems.BLOODY_RAG.get()))
              .delegate(DelegateBlockActionType.builder()
                  .returnItem(SurvivalItems.CLEAN_RAG)
                  .finishSound(SoundEvents.BUCKET_FILL)
                  .forBlock(blockState -> blockState.getFluidState().is(Fluids.WATER))
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_SYRINGE_ON_ZOMBIE =
      ACTION_TYPES.register("use_syringe_on_zombie",
          () -> ItemActionType.builder()
              .forItem(ModItems.SYRINGE)
              .duration(16)
              .delegate(DelegateEntityActionType
                  .builder(TargetSelector.OTHERS_ONLY.ofEntityType(Zombie.class))
                  .customAction((performer, target) -> target.getEntity().hurt(
                      DamageSource.mobAttack(target.getEntity()), 2.0F), 0.25F)
                  .returnItem(SurvivalItems.RBI_SYRINGE)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_CURE_SYRINGE =
      ACTION_TYPES.register("use_cure_syringe",
          () -> ItemActionType.builder()
              .forItem(SurvivalItems.CURE_SYRINGE)
              .duration(16)
              .delegate(DelegateEntityActionType.builder(TargetSelector.SELF_OR_OTHERS)
                  .returnItem(ModItems.SYRINGE)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_RBI_SYRINGE =
      ACTION_TYPES.register("use_rbi_syringe",
          () -> ItemActionType.builder()
              .forItem(SurvivalItems.RBI_SYRINGE)
              .duration(16)
              .delegate(DelegateEntityActionType.builder(TargetSelector.SELF_OR_OTHERS)
                  .effect(() -> new MobEffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999))
                  .returnItem(ModItems.SYRINGE)
                  .build())
              .build());
}
