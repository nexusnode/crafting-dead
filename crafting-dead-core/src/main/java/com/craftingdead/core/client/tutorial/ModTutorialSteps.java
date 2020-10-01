/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.client.tutorial;

import java.util.function.Function;
import com.craftingdead.core.client.ClientDist;
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
