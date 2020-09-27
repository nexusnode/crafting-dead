package com.craftingdead.core.game.survival;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.game.ITeam;
import net.minecraft.util.ResourceLocation;

public class SurvivorsTeam implements ITeam {

  static final SurvivorsTeam INSTANCE = new SurvivorsTeam();

  @Override
  public ResourceLocation getId() {
    return new ResourceLocation(CraftingDead.ID, "survivors");
  }
}
