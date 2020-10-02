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
package com.craftingdead.immerse.client.gui.component;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.immerse.client.util.RenderUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class DropdownComponent extends Component<DropdownComponent> {

  private static final int DEFAULT_ITEM_HEIGHT = 10;

  private final List<Item> items = new ArrayList<>();

  private final int itemHeight;

  private final Colour itemColour;

  private boolean expanded;

  private int selectedItemIndex = 0;

  public DropdownComponent(Colour itemColour) {
    this(DEFAULT_ITEM_HEIGHT, itemColour);
  }

  public DropdownComponent(int itemHeight, Colour itemColour) {
    this.itemHeight = itemHeight;
    this.itemColour = itemColour;
  }

  public DropdownComponent addItem(int id, ITextComponent text) {
    this.items.add(new Item(id, text));
    return this;
  }

  public int getSelectedItemId() {
    return this.items.get(this.selectedItemIndex).id;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!super.mouseClicked(mouseX, mouseY, button)) {
      if (this.expanded) {
        for (int i = 0; i < this.items.size(); i++) {
          final double itemY = this.getY() + (this.itemHeight * (i + 1));
          if (i != this.selectedItemIndex && mouseY >= itemY && mouseY <= itemY + this.itemHeight) {
            this.selectedItemIndex = i;
          }
        }
      }
      this.expanded = !this.expanded;
      return true;
    }
    return false;
  }

  @Override
  public boolean changeFocus(boolean focused) {
    this.expanded = super.changeFocus(focused);
    return this.expanded;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.renderItem(this.getYFloat(), this.items.get(this.selectedItemIndex), false);
    if (this.expanded) {
      for (int i = 0; i < this.items.size(); i++) {
        final float itemY = this.getYFloat() + (this.itemHeight * (i + 1));
        this.renderItem(itemY, this.items.get(i), i == this.selectedItemIndex);
      }
    }
  }

  private void renderItem(float y, Item item, boolean selected) {
    RenderUtil.fill(this.getX(), y, this.getX() + this.getWidth(), y + this.itemHeight,
        this.itemColour.getHexColour());
    this.minecraft.fontRenderer.drawStringWithShadow(item.text.getFormattedText(),
        (float) this.getX(), (float) (y + this.itemHeight / 2.0D),
        selected ? TextFormatting.GRAY.getColor() : TextFormatting.WHITE.getColor());
  }

  private class Item {
    private final int id;
    private final ITextComponent text;

    public Item(int id, ITextComponent text) {
      this.id = id;
      this.text = text;
    }
  }
}
