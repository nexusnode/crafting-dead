package com.craftingdead.mod.client.tutorial;

import java.util.function.Function;
import com.craftingdead.mod.client.ClientDist;
import net.minecraft.client.tutorial.ITutorialStep;

public enum ModTutorialSteps {
  OPEN_INVENTORY("open_inventory", OpenModInventoryStep::new), NONE("none",
      CompletedModTutorialStep::new);

  private final String name;
  private final Function<ClientDist, ? extends ITutorialStep> tutorial;

  private <T extends ITutorialStep> ModTutorialSteps(String name,
      Function<ClientDist, T> tutorial) {
    this.name = name;
    this.tutorial = tutorial;
  }

  public ITutorialStep create(ClientDist client) {
    return this.tutorial.apply(client);
  }

  public String getName() {
    return this.name;
  }

  public static ModTutorialSteps byName(String name) {
    for (ModTutorialSteps step : values()) {
      if (step.name.equals(name)) {
        return step;
      }
    }
    return NONE;
  }
}
