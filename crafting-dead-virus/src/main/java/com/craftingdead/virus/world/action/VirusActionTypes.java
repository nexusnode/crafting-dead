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

package com.craftingdead.virus.world.action;

import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.item.EntityActionEntry;
import com.craftingdead.core.world.action.item.UseItemAction;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.virus.CraftingDeadVirus;
import com.craftingdead.virus.world.effect.VirusEffects;
import com.craftingdead.virus.world.item.VirusItems;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class VirusActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      DeferredRegister.create((Class<ActionType<?>>) (Object) ActionType.class,
          CraftingDeadVirus.ID);

  public static final RegistryObject<ActionType<UseItemAction>> USE_CURE_SYRINGE =
      ACTION_TYPES.register("use_cure_syringe", () -> new ActionType<>(
          (actionType, performer, target) -> UseItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(item -> item == VirusItems.CURE_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)))
              .build(),
          false));

  public static final RegistryObject<ActionType<UseItemAction>> USE_RBI_SYRINGE =
      ACTION_TYPES.register("use_rbi_syringe", () -> new ActionType<>(
          (actionType, performer, target) -> UseItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(item -> item == VirusItems.RBI_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .addEffect(
                      Pair.of(new EffectInstance(VirusEffects.INFECTION.get(), 9999999), 1.0F))
                  .setReturnItem(ModItems.SYRINGE)))
              .build(),
          false));
}
