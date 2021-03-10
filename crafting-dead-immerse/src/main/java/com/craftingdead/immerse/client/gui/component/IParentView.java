/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.component;

import net.minecraft.client.gui.INestedGuiEventHandler;

public interface IParentView extends IView, INestedGuiEventHandler {

  @Override
  default void mouseMoved(double mouseX, double mouseY) {
    this.children().forEach(listener -> listener.mouseMoved(mouseX, mouseY));
  }

  @Override
  default boolean isMouseOver(double mouseX, double mouseY) {
    return INestedGuiEventHandler.super.isMouseOver(mouseX, mouseY)
        || this.children().stream()
            .anyMatch(listener -> listener.isMouseOver(mouseX, mouseY));
  }
}
