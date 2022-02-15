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

package com.craftingdead.immerse.client.gui.view.layout;

import java.util.function.Consumer;
import com.craftingdead.immerse.client.gui.view.state.StateListener;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;

public interface LayoutParent {

  Layout addChild(int index);

  void removeChild(Layout layout);

  void layout(float width, float height);

  void close();

  default void gatherDispatchers(Consumer<PropertyDispatcher<?>> consumer) {}

  default void gatherListeners(Consumer<StateListener> consumer) {}
}
