package com.craftingdead.virus.action;

import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.action.item.EntityActionEntry;
import com.craftingdead.core.action.item.UseItemAction;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.virus.CraftingDeadVirus;
import com.craftingdead.virus.item.VirusItems;
import com.craftingdead.virus.potion.VirusEffects;
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
