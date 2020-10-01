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

import com.craftingdead.core.client.ClientDist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OpenModInventoryStep implements IModTutorialStep {
  private static final ITextComponent TITLE =
      new TranslationTextComponent("tutorial.craftingdead.open_inventory.title");
  private static final ITextComponent DESCRIPTION =
      new TranslationTextComponent("tutorial.open_inventory.description",
          Tutorial.createKeybindComponent("craftingdead.inventory"));

  private final ClientDist client;
  private TutorialToast toast;
  private int timeWaiting;

  public OpenModInventoryStep(ClientDist client) {
    this.client = client;
  }

  @Override
  public void tick() {
    ++this.timeWaiting;
    if (this.timeWaiting >= 600 && this.toast == null) {
      this.toast = new TutorialToast(TutorialToast.Icons.RECIPE_BOOK, TITLE, DESCRIPTION, false);
      Minecraft.getInstance().getToastGui().add(this.toast);
    }
  }

  @Override
  public void onStop() {
    if (this.toast != null) {
      this.toast.hide();
      this.toast = null;
    }
  }

  @Override
  public void openModInventory() {
    this.client.setTutorialStep(ModTutorialSteps.NONE);
  }
}
