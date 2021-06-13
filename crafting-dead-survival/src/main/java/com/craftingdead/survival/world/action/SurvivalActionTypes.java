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

package com.craftingdead.survival.world.action;

import java.util.Optional;
import java.util.Random;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.TargetSelector;
import com.craftingdead.core.world.action.delegated.DelegatedBlockActionType;
import com.craftingdead.core.world.action.delegated.DelegatedEntityActionType;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class SurvivalActionTypes {

  public static final DeferredRegister<ActionType> ACTION_TYPES =
      DeferredRegister.create(ActionType.class, CraftingDeadSurvival.ID);

  public static final RegistryObject<ActionType> SHRED_CLOTHING =
      ACTION_TYPES.register("shred_clothing",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getCapability(Capabilities.CLOTHING).isPresent())
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setCustomAction(extension -> {
                    Random random = extension.getEntity().getRandom();
                    int randomRagAmount = random.nextInt(3) + 3;

                    for (int i = 0; i < randomRagAmount; i++) {
                      if (random.nextBoolean()) {
                        extension.getEntity().spawnAtLocation(
                            new ItemStack(SurvivalItems.CLEAN_RAG::get));
                      } else {
                        extension.getEntity().spawnAtLocation(
                            new ItemStack(SurvivalItems.DIRTY_RAG::get));
                      }
                    }
                  }, 1.0F)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_SPLINT =
      ACTION_TYPES.register("use_splint",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == SurvivalItems.SPLINT.get())
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS
                      .andThen(extension -> (extension == null
                          || !extension.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get()))
                              ? Optional.empty()
                              : Optional.of(extension)))
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_CLEAN_RAG =
      ACTION_TYPES.register("use_clean_rag",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.CLEAN_RAG.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS
                      .andThen(extension -> (extension == null
                          || !extension.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get()))
                              ? Optional.empty()
                              : Optional.of(extension)))
                  .setReturnItem(SurvivalItems.BLOODY_RAG)
                  .build())
              .build());

  public static final RegistryObject<ActionType> WASH_RAG =
      ACTION_TYPES.register("wash_rag",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.DIRTY_RAG.get()
                      || itemStack.getItem() == SurvivalItems.BLOODY_RAG.get())
              .addDelegatedAction(DelegatedBlockActionType.builder()
                  .setReturnItem(SurvivalItems.CLEAN_RAG)
                  .setFinishSound(SoundEvents.BUCKET_FILL)
                  .setPredicate(
                      blockState -> blockState.getFluidState().getType() == Fluids.WATER)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_SYRINGE_ON_ZOMBIE =
      ACTION_TYPES.register("use_syringe_on_zombie",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == ModItems.SYRINGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.OTHERS_ONLY.ofType(ZombieEntity.class))
                  .setCustomAction(extension -> extension.getEntity().hurt(
                      DamageSource.mobAttack(extension.getEntity()), 2.0F), 0.25F)
                  .setReturnItem(() -> SurvivalItems.RBI_SYRINGE.get())
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_CURE_SYRINGE =
      ACTION_TYPES.register("use_cure_syringe",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.CURE_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)
                  .build())
              .build());

  public static final RegistryObject<ActionType> USE_RBI_SYRINGE =
      ACTION_TYPES.register("use_rbi_syringe",
          () -> ItemActionType.builder()
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.RBI_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addDelegatedAction(DelegatedEntityActionType.builder()
                  .setTargetSelector(TargetSelector.SELF_OR_OTHERS)
                  .addEffect(
                      () -> new EffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999), 1.0F)
                  .setReturnItem(ModItems.SYRINGE)
                  .build())
              .build());
}
