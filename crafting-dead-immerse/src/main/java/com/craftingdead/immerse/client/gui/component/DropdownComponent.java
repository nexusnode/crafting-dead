package com.craftingdead.immerse.client.gui.component;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class DropdownComponent extends Component<DropdownComponent> {

  private static final int DEFAULT_ITEM_HEIGHT = 10;

  private final List<Item> items = new ArrayList<>();

  private final int itemHeight;

  private final int itemColour;

  private boolean expanded;

  private int selectedItemIndex = 0;

  public DropdownComponent(int itemColour) {
    this(DEFAULT_ITEM_HEIGHT, itemColour);
  }

  public DropdownComponent(int itemHeight, int itemColour) {
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
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderItem(this.getY(), this.items.get(this.selectedItemIndex), false);
    if (this.expanded) {
      for (int i = 0; i < this.items.size(); i++) {
        final double itemY = this.getScaledContentY() + (this.itemHeight * (i + 1));
        this.renderItem(itemY, this.items.get(i), i == this.selectedItemIndex);
      }
    }
  }

  private void renderItem(double y, Item item, boolean selected) {
    RenderUtil.fill(this.getScaledContentX(), y,
        this.getScaledContentX() + this.getScaledContentWidth(), y + this.itemHeight,
        this.itemColour);
    this.minecraft.fontRenderer.func_238418_a_(item.text,
        (int) this.getScaledContentX(), (int) (y + this.itemHeight / 2.0D),
        (int) this.getScaledContentWidth(),
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
