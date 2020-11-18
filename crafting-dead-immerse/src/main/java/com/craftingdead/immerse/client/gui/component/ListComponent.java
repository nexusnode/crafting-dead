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
import java.util.ListIterator;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ListComponent extends Component<ListComponent> {

  protected static final float FADE_DURATION_MS = 150;

  private static final TweenType<ListItem> ITEM_COLOUR = new SimpleTweenType<>(4,
      item -> item.colour.getColour4f(), (item, colour) -> item.colour.setColour4f(colour));


  protected final List<ListItem> items = new ArrayList<>();

  protected final Colour itemColour;
  protected final Colour itemHoverColour;

  protected int selectedItemIndex = 0;

  public ListComponent(Colour itemColour, Colour itemHoverColour) {
    this.itemColour = itemColour;
    this.itemHoverColour = itemHoverColour;
  }

  public ListComponent addItem(int id, ITextComponent text) {
    this.items.add(new ListItem(id, text));
    return this;
  }

  public int getSelectedItemId() {
    return this.items.get(this.selectedItemIndex).id;
  }


  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!super.mouseClicked(mouseX, mouseY, button)) {
      for (int i = 0; i < this.items.size(); i++) {
        final double itemY = this.getY() + (this.getHeight() * i) + this.getListYOffset();
        if (i != this.selectedItemIndex && mouseY >= itemY
            && mouseY <= itemY + this.getHeight()) {
          this.selectedItemIndex = i;
        }
      }
      return true;
    }
    return false;
  }

  protected double getListYOffset() {
    return 0.0D;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    for (int i = 0; i < this.items.size(); i++) {
      final ListItem item = this.items.get(i);
      final double itemY = this.getY() + (this.getHeight() * i) + this.getListYOffset();
      if (mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth()
          && mouseY >= itemY && mouseY <= itemY + this.getHeight()) {
        if (!item.wasMouseOver) {
          item.wasMouseOver = true;
          Tween.to(item, ITEM_COLOUR, FADE_DURATION_MS)
              .target(this.itemHoverColour.getColour4f())
              .start(this.getTweenManager());
        }
      } else if (item.wasMouseOver) {
        item.wasMouseOver = false;
        Tween.to(item, ITEM_COLOUR, FADE_DURATION_MS)
            .target(this.itemColour.getColour4f())
            .start(this.getTweenManager());
      }
    }
  }

  @Override
  public boolean changeFocus(boolean toggleForward) {
    final ListIterator<ListItem> it = this.items.listIterator(this.selectedItemIndex + 1);
    if (toggleForward) {
      if (it.hasNext()) {
        this.selectedItemIndex = it.nextIndex();
        return true;
      } else {
        this.selectedItemIndex = 0;
        return false;
      }
    } else {
      if (it.hasPrevious()) {
        this.selectedItemIndex = it.previousIndex();
        return true;
      } else {
        this.selectedItemIndex = this.items.size() - 1;
        return false;
      }
    }
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    for (int i = 0; i < this.items.size(); i++) {
      final double itemY =
          this.getY() + (this.getHeight() * i) + this.getListYOffset();
      RenderUtil.fill(this.getX(), itemY, this.getX() + this.getWidth(),
          itemY + 1.0F / this.mainWindow.getGuiScaleFactor(), 0x80888888);
      this.renderItem(itemY, this.items.get(i), i == this.selectedItemIndex);
    }
  }

  private void renderItem(double y, ListItem item, boolean selected) {
    RenderUtil.fill(this.getX(), y, this.getX() + this.getWidth(),
        y + this.getHeight(), item.colour.getHexColour());
    RenderSystem.pushMatrix();
    {
      final String text = item.text.getFormattedText();
      RenderSystem.translated(
          this.getX() + this.getWidth() / 2.0F
              - this.minecraft.fontRenderer.getStringWidth(text) / 2.0F,
          y + this.getHeight() / 2.0F - this.minecraft.fontRenderer.FONT_HEIGHT / 2.0F, 100.0D);
      RenderSystem.scaled(this.getXScale(), this.getYScale(), 1.0D);
      this.minecraft.fontRenderer.drawStringWithShadow(text,
          0.0F, 0.0F, selected ? TextFormatting.GRAY.getColor() : TextFormatting.WHITE.getColor());
    }
    RenderSystem.popMatrix();
  }

  protected class ListItem {

    private final int id;
    private final ITextComponent text;

    private final Colour colour = new Colour(ListComponent.this.itemColour);

    private boolean wasMouseOver;

    private ListItem(int id, ITextComponent text) {
      this.id = id;
      this.text = text;
    }

    public int getId() {
      return id;
    }

    public ITextComponent getText() {
      return text;
    }

    public Colour getColour() {
      return colour;
    }
  }
}
