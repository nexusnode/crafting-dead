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

import java.util.HashMap;

public class ContentTabsComponent extends TabsComponent {

  private final HashMap<Tab, Component<?>> tabContent = new HashMap<>();
  private ParentComponent<?> contentContainer;

  public ContentTabsComponent(ParentComponent<?> contentContainer) {
    super();
    this.contentContainer = contentContainer;
    this.addTabChangeListener(this::onTabChanged);
  }

  @Override
  public TabsComponent addTab(Tab tab) {
    throw new UnsupportedOperationException("Unsupported operation for ContentTabsComponent. " +
        "Use ContentTabsComponent#addTab(Tab, Component<?> instead");
  }

  public ContentTabsComponent addTab(Tab tab, Component<?> content) {
    super.addTab(tab);
    tabContent.put(tab, content);
    return this;
  }

  public void onTabChanged(Tab newTab) {
      contentContainer.clearChildren();
      contentContainer.addChild(tabContent.get(newTab));
      contentContainer.layout();
  }

}
