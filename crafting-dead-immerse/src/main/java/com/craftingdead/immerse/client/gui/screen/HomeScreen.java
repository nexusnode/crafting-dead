/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.client.gui.screen;

import net.minecraft.util.text.TranslationTextComponent;

public class HomeScreen extends MenuScreen {

  public HomeScreen() {
    super(new TranslationTextComponent("narrator.screen.title"));
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
  }
}
