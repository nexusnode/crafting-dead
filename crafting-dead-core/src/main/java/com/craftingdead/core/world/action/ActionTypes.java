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

package com.craftingdead.core.world.action;

import java.util.Optional;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.action.item.EntityItemActionType;
import com.craftingdead.core.world.action.reload.MagazineReloadAction;
import com.craftingdead.core.world.action.reload.RefillableReloadAction;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
import com.google.common.base.Predicates;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      DeferredRegister.create((Class<ActionType<?>>) (Object) ActionType.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<ActionType<?>>> REGISTRY =
      Lazy.of(ACTION_TYPES.makeRegistry("action_type", RegistryBuilder::new));

  public static final RegistryObject<ActionType<?>> MAGAZINE_RELOAD =
      ACTION_TYPES.register("magazine_reload",
          () -> new SimpleActionType<>(MagazineReloadAction::new, true));

  public static final RegistryObject<ActionType<?>> REFILLABLE_RELOAD =
      ACTION_TYPES.register("refillable_reload",
          () -> new SimpleActionType<>(RefillableReloadAction::new, true));

  public static final RegistryObject<ActionType<?>> REMOVE_MAGAZINE =
      ACTION_TYPES.register("remove_magazine",
          () -> new SimpleActionType<>(RemoveMagazineAction::new, true));

  public static final RegistryObject<EntityItemActionType<?>> USE_SYRINGE =
      ACTION_TYPES.register("use_syringe",
          () -> EntityItemActionType
              .builder((performer, target) -> {
                if (target == null
                    || performer == target
                    || target.getEntity() instanceof Skeleton) {
                  return Optional.empty();
                }

                var targetEntity = target.getEntity();
                if (targetEntity.getHealth() > 4) {
                  return Optional.ofNullable(target);
                }

                if (performer.getEntity() instanceof Player player) {
                  player.displayClientMessage(
                      new TranslatableComponent("message.low_health",
                          targetEntity.getDisplayName()).withStyle(ChatFormatting.RED),
                      true);
                }

                return Optional.empty();
              })
              .forItem(ModItems.SYRINGE)
              .duration(16)
              .customAction((performer, target) -> target.getEntity().hurt(
                  DamageSource.mobAttack(target.getEntity()), 2.0F), 1.0F)
              .returnItem(ModItems.BLOOD_SYRINGE)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_FIRST_AID_KIT =
      ACTION_TYPES.register("use_first_aid_kit",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(ModItems.FIRST_AID_KIT)
              .setFreezeMovement(true)
              .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 1))
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_ADRENALINE_SYRINGE =
      ACTION_TYPES.register("use_adrenaline_syringe",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(ModItems.ADRENALINE_SYRINGE)
              .duration(16)
              .returnItem(ModItems.SYRINGE)
              .useResultItemInCreative(false)
              .effect(() -> new MobEffectInstance(ModMobEffects.ADRENALINE.get(), 20 * 20, 1))
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_BLOOD_SYRINGE =
      ACTION_TYPES.register("use_blood_syringe",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(ModItems.BLOOD_SYRINGE)
              .duration(16)
              .returnItem(ModItems.SYRINGE)
              .useResultItemInCreative(false)
              .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0))
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_BANDAGE =
      ACTION_TYPES.register("use_bandage",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(ModItems.BANDAGE)
              .duration(16)
              .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0))
              .build());

  public static final RegistryObject<EntityItemActionType<?>> APPLY_HANDCUFFS =
      ACTION_TYPES.register("apply_handcuffs",
          () -> EntityItemActionType.builder(TargetSelector.OTHERS_ONLY
              .players()
              .filter(Predicates.not(PlayerExtension::isHandcuffed)))
              .forItem(ModItems.HANDCUFFS)
              .customAction((performer, target) -> {
                target.setHandcuffs(performer.getMainHandItem().copy());
                target.getEntity().displayClientMessage(
                    new TranslatableComponent("handcuffs.handcuffed",
                        performer.getEntity().getDisplayName())
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
                    true);
              }, 1.0F)
              .build());
}
