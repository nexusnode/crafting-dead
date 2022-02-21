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

import com.craftingdead.core.client.ClientDist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class OpenEquipmentMenuTutorialStepInstance implements ModTutorialStepInstance {
  private static final Component TITLE =
      new TranslatableComponent("tutorial.open_equipment_menu.title");
  private static final Component DESCRIPTION =
      new TranslatableComponent("tutorial.open_equipment_menu.description",
          Tutorial.key("equipment_menu"));

  private final ClientDist client;
  private TutorialToast toast;
  private int timeWaiting;

  public OpenEquipmentMenuTutorialStepInstance(ClientDist client) {
    this.client = client;
  }

  @Override
  public void tick() {
    ++this.timeWaiting;
    if (this.timeWaiting >= 600 && this.toast == null) {
      this.toast = new TutorialToast(TutorialToast.Icons.RECIPE_BOOK, TITLE, DESCRIPTION, false);
      Minecraft.getInstance().getToasts().addToast(this.toast);
    }
  }

  @Override
  public void clear() {
    if (this.toast != null) {
      this.toast.hide();
      this.toast = null;
    }
  }

  @Override
  public void openEquipmentMenu() {
    this.client.setTutorialStep(ModTutorialSteps.NONE);
  }
}
