/*
 * Crafting Dead
 * Copyright (C)  2021  Nexus Node
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

import net.minecraft.util.text.ITextComponent;

import java.util.HashMap;

public class ContentDropdownComponent extends DropdownComponent {

  private final HashMap<Integer, Component<?>> itemContent = new HashMap<>();
  private final ParentComponent<?> contentContainer;

  public ContentDropdownComponent(ParentComponent<?> contentContainer) {
    super();
    this.contentContainer = contentContainer;
    this.addSelectListener(this::onSelectionChanged);
  }

  @Override
  public DropdownComponent addItem(int id, ITextComponent text) {
    throw new UnsupportedOperationException("Unsupported operation for ContentDropdownComponent. " +
        "Use ContentDropdownComponent#addItem(int, ITextComponent, Component<?> instead");
  }

  public ContentDropdownComponent addItem(int id, ITextComponent text, Component<?> itemContent) {
    super.addItem(id, text);
    this.itemContent.put(id, itemContent);
    return this;
  }

  private void onSelectionChanged(int newSelectionId) {
    contentContainer.clearChildren();
    contentContainer.addChild(itemContent.get(newSelectionId));
    contentContainer.layout();
  }


}
