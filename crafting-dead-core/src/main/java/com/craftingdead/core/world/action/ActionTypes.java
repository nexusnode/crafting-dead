/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.action;

import java.util.Optional;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.action.delegated.DelegatedEntityActionType;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.action.reload.MagazineReloadAction;
import com.craftingdead.core.world.action.reload.RefillableReloadAction;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ActionTypes {

  public static final DeferredRegister<ActionType> ACTION_TYPES =
      DeferredRegister.create(ActionType.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<ActionType>> REGISTRY =
      Lazy.of(ACTION_TYPES.makeRegistry("action_types", RegistryBuilder::new));

  public static final RegistryObject<ActionType> MAGAZINE_RELOAD =
      ACTION_TYPES.register("magazine_reload",
          () -> new ActionType(true, MagazineReloadAction::new));

  public static final RegistryObject<ActionType> REFILLABLE_RELOAD =
      ACTION_TYPES.register("refillable_reload",
          () -> new ActionType(true, RefillableReloadAction::new));

  public static final RegistryObject<ActionType> REMOVE_MAGAZINE =
      ACTION_TYPES.register("remove_magazine",
          () -> new ActionType(true, RemoveMagazineAction::new));

  public static final RegistryObject<ActionType> USE_SYRINGE =
      ACTION_TYPES.register("use_syringe",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == ModItems.SYRINGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector((performer, target) -> {
                    if (target == null
                        || performer == target
                        || target.getEntity() instanceof SkeletonEntity) {
                      return Optional.empty();
                    }

                    LivingEntity targetEntity = target.getEntity();
                    if (targetEntity.getHealth() > 4) {
                      return Optional.ofNullable(target);
                    }

                    if (performer.getEntity() instanceof PlayerEntity) {
                      ((PlayerEntity) performer.getEntity()).displayClientMessage(
                          new TranslationTextComponent("message.low_health",
                              targetEntity.getDisplayName()).withStyle(TextFormatting.RED),
                          true);
                    }

                    return Optional.empty();
                  })
                  .setCustomAction(extension -> extension.getEntity().hurt(
                      DamageSource.mobAttack(extension.getEntity()), 2.0F), 1.0F)
                  .setReturnItem(ModItems.BLOOD_SYRINGE)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_FIRST_AID_KIT =
      ACTION_TYPES.register("use_first_aid_kit",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == ModItems.FIRST_AID_KIT.get())
              .setFreezeMovement(true)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS)
                  .addEffect(() -> new EffectInstance(Effects.HEAL, 1, 1), 1.0F)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_ADRENALINE_SYRINGE =
      ACTION_TYPES.register("use_adrenaline_syringe",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == ModItems.ADRENALINE_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)
                  .setReturnItemInCreative(false)
                  .addEffect(
                      () -> new EffectInstance(ModMobEffects.ADRENALINE.get(), 20 * 20, 1), 1.0F)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_BLOOD_SYRINGE =
      ACTION_TYPES.register("use_blood_syringe",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == ModItems.BLOOD_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)
                  .setReturnItemInCreative(false)
                  .addEffect(() -> new EffectInstance(Effects.HEAL, 1, 0), 1.0F)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_BANDAGE =
      ACTION_TYPES.register("use_bandage",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == ModItems.BANDAGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS)
                  .addEffect(() -> new EffectInstance(Effects.HEAL, 1, 0), 1.0F)
                  .build())
              .build());
}
