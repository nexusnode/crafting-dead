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

import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.item.BlockActionEntry;
import com.craftingdead.core.world.action.item.EntityActionEntry;
import com.craftingdead.core.world.action.item.ItemAction;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class SurvivalActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      DeferredRegister.create((Class<ActionType<?>>) (Object) ActionType.class,
          CraftingDeadSurvival.ID);

  public static final RegistryObject<ActionType<ItemAction>> SHRED_CLOTHING =
      ACTION_TYPES.register("shred_clothing", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getCapability(ModCapabilities.CLOTHING).isPresent())
              .addEntry(new EntityActionEntry(
                  new EntityActionEntry.Properties().setCustomAction(Pair.of(extension -> {

                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    int randomRagAmount = random.nextInt(3) + 3;

                    for (int i = 0; i < randomRagAmount; i++) {
                      if (random.nextBoolean()) {
                        extension.getEntity()
                            .spawnAtLocation(new ItemStack(SurvivalItems.CLEAN_RAG::get));
                      } else {
                        extension.getEntity()
                            .spawnAtLocation(new ItemStack(SurvivalItems.DIRTY_RAG::get));
                      }
                    }
                  }, 1.0F))))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_SPLINT =
      ACTION_TYPES.register("use_splint", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == SurvivalItems.SPLINT.get())
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS
                      .andThen(extension -> (extension == null
                          || !extension.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get()))
                              ? null
                              : extension))))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_CLEAN_RAG =
      ACTION_TYPES.register("use_clean_rag", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.CLEAN_RAG.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS
                      .andThen(extension -> (extension == null
                          || !extension.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get()))
                              ? null
                              : extension))
                  .setReturnItem(SurvivalItems.BLOODY_RAG)))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> WASH_RAG =
      ACTION_TYPES.register("wash_rag", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.DIRTY_RAG.get()
                      || itemStack.getItem() == SurvivalItems.BLOODY_RAG.get())
              .addEntry(new BlockActionEntry(new BlockActionEntry.Properties()
                  .setReturnItem(SurvivalItems.CLEAN_RAG)
                  .setFinishSound(SoundEvents.BUCKET_FILL)
                  .setPredicate(
                      blockState -> blockState.getFluidState().getType() == Fluids.WATER)))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_CURE_SYRINGE =
      ACTION_TYPES.register("use_cure_syringe", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.CURE_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_RBI_SYRINGE =
      ACTION_TYPES.register("use_rbi_syringe", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == SurvivalItems.RBI_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .addEffect(
                      Pair.of(new EffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999), 1.0F))
                  .setReturnItem(ModItems.SYRINGE)))
              .build()));
}
