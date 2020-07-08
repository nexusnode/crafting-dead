package com.craftingdead.core.action;

import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.util.LazyForgeRegistry;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      makeDeferredRegister(LazyForgeRegistry.of(ActionType.class), CraftingDead.ID);

  private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> makeDeferredRegister(
      IForgeRegistry<T> registry, String modid) {
    return new DeferredRegister<>(registry, modid);
  }

  public static final RegistryObject<ActionType<?>> RELOAD = ACTION_TYPES
      .register("reload",
          () -> new ActionType<>((actionType, performer, target) -> new ReloadAction(performer),
              true));

  public static final RegistryObject<ActionType<?>> REMOVE_MAGAZINE =
      ACTION_TYPES.register("remove_magazine",
          () -> new ActionType<>(
              (actionType, performer, target) -> new RemoveMagazineAction(performer),
              true));

  public static final RegistryObject<ActionType<UseItemAction>> USE_ITEM = ACTION_TYPES
      .register("use_item", () -> new ActionType<>(UseItemAction::new, false));

  public static final RegistryObject<ActionType<UseItemAction>> HEAL = ACTION_TYPES
      .register("heal",
          () -> new ActionType<>(
              (actionType, performer, target) -> new UseItemEffectAction(actionType,
                  performer, target, new UseItemEffectAction.Properties()
                      .setFreezeMovement(true)
                      .setApplicableToOthers(true)
                      .addEffect(Pair.of(new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 1.0F))),
              false));

  public static final RegistryObject<ActionType<UseItemAction>> ADRENALINE = ACTION_TYPES
      .register("adrenaline", () -> new ActionType<>(
          (actionType, performer, target) -> new UseItemEffectAction(actionType,
              performer, target, new UseItemEffectAction.Properties()
                  .addEffect(Pair.of(new EffectInstance(ModEffects.ADRENALINE.get(), (20 * 20), 1),
                      1.0F))),
          false));

  public static final RegistryObject<ActionType<UseItemAction>> BLOOD_SYRINGE = ACTION_TYPES
      .register("blood_syringe", () -> new ActionType<>(
          (actionType, performer, target) -> new UseItemEffectAction(actionType,
              performer, target, new UseItemEffectAction.Properties()
                  .addEffect(Pair.of(new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F))),
          false));

  public static final RegistryObject<ActionType<UseItemAction>> BANDAGE = ACTION_TYPES
      .register("bandage", () -> new ActionType<>(
          (actionType, performer, target) -> new UseItemEffectAction(actionType,
              performer, target, new UseItemEffectAction.Properties()
                  .setFreezeMovement(true)
                  .setApplicableToOthers(true)
                  .addEffect(Pair.of(new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F))),
          false));
}
