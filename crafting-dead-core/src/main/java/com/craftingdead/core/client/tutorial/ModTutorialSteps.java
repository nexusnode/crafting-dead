/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.tutorial;

import java.util.function.Function;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.client.tutorial.TutorialStepInstance;

public enum ModTutorialSteps {
  OPEN_EQUIPMENT_MENU("open_equipment_menu", OpenEquipmentMenuTutorialStepInstance::new), NONE("none",
      CompletedModTutorialStepInstance::new);

  private final String name;
  private final Function<ClientDist, ? extends TutorialStepInstance> tutorial;

  private <T extends TutorialStepInstance> ModTutorialSteps(String name,
      Function<ClientDist, T> tutorial) {
    this.name = name;
    this.tutorial = tutorial;
  }

  public TutorialStepInstance create(ClientDist client) {
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
