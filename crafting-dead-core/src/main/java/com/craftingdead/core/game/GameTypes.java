package com.craftingdead.core.game;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.game.survival.SurvivalClient;
import com.craftingdead.core.game.survival.SurvivalGame;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GameTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<GameType> GAME_TYPES =
      DeferredRegister.create((Class<GameType>) (Class<?>) GameType.class, CraftingDead.ID);

  public static final RegistryObject<GameType> SURVIVAL = GAME_TYPES
      .register("vanilla",
          () -> new GameType(logicalServer -> new SurvivalGame(), SurvivalClient::new));
}
