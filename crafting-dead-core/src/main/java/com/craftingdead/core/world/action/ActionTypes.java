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

import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.action.item.EntityActionEntry;
import com.craftingdead.core.world.action.item.ItemAction;
import com.craftingdead.core.world.action.reload.MagazineReloadAction;
import com.craftingdead.core.world.action.reload.RefillableReloadAction;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      DeferredRegister.create((Class<ActionType<?>>) (Class<?>) ActionType.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<ActionType<?>>> REGISTRY =
      Lazy.of(ACTION_TYPES.makeRegistry("action_types", RegistryBuilder::new));

  public static final RegistryObject<ActionType<?>> MAGAZINE_RELOAD =
      ACTION_TYPES.register("magazine_reload", () -> new ActionType<>(true,
          (actionType, performer, target) -> new MagazineReloadAction(performer)));

  public static final RegistryObject<ActionType<?>> REFILLABLE_RELOAD =
      ACTION_TYPES.register("refillable_reload", () -> new ActionType<>(true,
          (actionType, performer, target) -> new RefillableReloadAction(performer)));

  public static final RegistryObject<ActionType<?>> REMOVE_MAGAZINE =
      ACTION_TYPES.register("remove_magazine", () -> new ActionType<>(false,
          (actionType, performer, target) -> new RemoveMagazineAction(performer)));

  public static final RegistryObject<ActionType<ItemAction>> USE_SYRINGE =
      ACTION_TYPES.register("use_syringe", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == ModItems.SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(
                  () -> !ModItemTags.VIRUS_SYRINGE.getValues().isEmpty(),
                  new EntityActionEntry(new EntityActionEntry.Properties()
                      .setTargetSelector(
                          EntityActionEntry.TargetSelector.OTHERS_ONLY.ofType(ZombieEntity.class))
                      .setCustomAction(Pair.of(
                          extension -> extension.getEntity()
                              .hurt(DamageSource.mobAttack(extension.getEntity()), 2.0F),
                          0.25F))
                      .setReturnItem(ModItemTags.VIRUS_SYRINGE.getValues().get(0))))
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector((p, t) -> {
                    if (t == null || p == t) {
                      return null;
                    }
                    LivingEntity targetEntity = t.getEntity();
                    if (!(targetEntity instanceof ZombieEntity
                        || targetEntity instanceof SkeletonEntity)) {
                      if (targetEntity.getHealth() > 4) {
                        return t;
                      } else if (p.getEntity() instanceof PlayerEntity) {
                        ((PlayerEntity) p.getEntity()).displayClientMessage(
                            new TranslationTextComponent("message.low_health",
                                targetEntity.getDisplayName()).setStyle(
                                    Style.EMPTY.applyFormats(TextFormatting.RED)),
                            true);
                      }
                    }
                    return null;
                  })
                  .setCustomAction(Pair.of(
                      extension -> extension.getEntity().hurt(
                          DamageSource.mobAttack(extension.getEntity()), 2.0F),
                      1.0F))
                  .setReturnItem(ModItems.BLOOD_SYRINGE)))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_FIRST_AID_KIT =
      ACTION_TYPES.register("use_first_aid_kit", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction
              .builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == ModItems.FIRST_AID_KIT.get())
              .setFreezeMovement(true)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .addEffect(Pair.of(new EffectInstance(Effects.HEAL, 1, 1), 1.0F))))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_ADRENALINE_SYRINGE =
      ACTION_TYPES.register("use_adrenaline_syringe", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == ModItems.ADRENALINE_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)
                  .setReturnItemInCreative(false)
                  .addEffect(Pair.of(
                      new EffectInstance(ModMobEffects.ADRENALINE.get(), 20 * 20, 1),
                      1.0F))))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_BLOOD_SYRINGE =
      ACTION_TYPES.register("use_blood_syringe", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(
                  itemStack -> itemStack.getItem() == ModItems.BLOOD_SYRINGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .setReturnItem(ModItems.SYRINGE)
                  .setReturnItemInCreative(false)
                  .addEffect(Pair.of(new EffectInstance(Effects.HEAL, 1, 0), 1.0F))))
              .build()));

  public static final RegistryObject<ActionType<ItemAction>> USE_BANDAGE =
      ACTION_TYPES.register("use_bandage", () -> new ActionType<>(false,
          (actionType, performer, target) -> ItemAction.builder(actionType, performer, target)
              .setHeldItemPredicate(itemStack -> itemStack.getItem() == ModItems.BANDAGE.get())
              .setTotalDurationTicks(16)
              .addEntry(new EntityActionEntry(new EntityActionEntry.Properties()
                  .setTargetSelector(EntityActionEntry.TargetSelector.SELF_AND_OTHERS)
                  .addEffect(Pair.of(new EffectInstance(Effects.HEAL, 1, 0), 1.0F))))
              .build()));
}
