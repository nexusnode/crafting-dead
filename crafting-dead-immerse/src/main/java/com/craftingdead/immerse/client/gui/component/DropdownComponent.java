package com.craftingdead.immerse.client.gui.component;

import java.util.ArrayList;
import java.util.List;
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
    final int[] colour4i = this.itemColour.getColour4i();
    this.canvas.setRGBA(colour4i[0], colour4i[0], colour4i[0], colour4i[0]);
    this.canvas.drawRect(this.getXFloat(), y, this.getXFloat() + this.getWidthFloat(),
        y + this.itemHeight);
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
