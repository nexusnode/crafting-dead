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

package com.craftingdead.core.client.renderer.item;

import net.minecraft.item.Item;

/**
 * An interface that allows an {@link Item} to provide an {@link CustomItemRenderer}.
 */
public interface IRendererProvider {

  /**
   * A singleton instance of the {@link CustomItemRenderer} to use.
   * 
   * @return the {@link CustomItemRenderer} instance
   */
  CustomItemRenderer getRenderer();
}
