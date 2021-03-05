package com.craftingdead.immerse.client.gui.component;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.craftingdead.immerse.client.gui.component.event.DropdownItemSelectEvent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class DropdownComponent extends Component<DropdownComponent> {
  public static final int DEFAULT_ITEM_HEIGHT = 14;
  public static final int DEFAULT_ITEM_BACKGROUND_COLOUR = 0xEA666666;
  public static final int DEFAULT_Z_LEVEL = 5;
  public static final double DEFAULT_ARROW_WIDTH = 12D;
  public static final double DEFAULT_ARROW_HEIGHT = 5D;
  public static final double DEFAULT_ARROW_LINE_WIDTH = 1.6D;
  public static final double DEFAULT_X_ARROW_OFFSET = 0.18D;

  private static final Logger logger = LogManager.getLogger();

  //Need ordering for #mouseClicked, could alternatively use LinkedHashMap for insertion ordering
  private final Map<Integer, Item> items = new TreeMap<>();

  private int itemHeight;
  private int itemBgColour;

  private boolean expanded = false;
  private int selectedItemId = -1;
  private boolean init = false;

  private double arrowWidth;
  private double arrowHeight ;
  private double arrowLineWidth;
  private double arrowLineWidthX;
  private double arrowLineWidthY;
  private double xArrowOffset;

  public DropdownComponent() {
    this.itemHeight = DEFAULT_ITEM_HEIGHT;
    this.itemBgColour = DEFAULT_ITEM_BACKGROUND_COLOUR;
    this.setZLevel(DEFAULT_Z_LEVEL);
    this.setHeight(itemHeight);
    this.arrowWidth = DEFAULT_ARROW_WIDTH;
    this.arrowHeight = DEFAULT_ARROW_HEIGHT;
    this.arrowLineWidth = DEFAULT_ARROW_LINE_WIDTH;
    this.calculateArrowLineWidthProjections();
    this.xArrowOffset = DEFAULT_X_ARROW_OFFSET;
  }

  public DropdownComponent setArrowWidth(double arrowWidth) {
    this.arrowWidth = arrowWidth;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  public DropdownComponent setArrowHeight(double arrowHeight) {
    this.arrowHeight = arrowHeight;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  public DropdownComponent setArrowLineWidth(double arrowLineWidth) {
    this.arrowLineWidth = arrowLineWidth;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  /**
   * @param xArrowOffset RTL offset from 0.0D to 1.0D (something like percent margin right)
   */
  public DropdownComponent setXArrowOffset(double xArrowOffset) {
    this.xArrowOffset = xArrowOffset;
    return this;
  }

  public DropdownComponent setItemHeight(int itemHeight) {
    this.itemHeight = itemHeight;
    return this;
  }

  public DropdownComponent setItemBgColour(int bgColour) {
    this.itemBgColour = bgColour;
    return this;
  }

  public DropdownComponent addItem(int id, ITextComponent text) {
    this.items.put(id, new Item(id, text));
    return this;
  }

  public DropdownComponent setDisabled(int itemId, boolean disabled) {
    items.get(itemId).setDisabled(disabled);
    return this;
  }

  public DropdownComponent selectItem(int itemId) {
    this.onItemSelected(items.get(itemId));
    return this;
  }

  public Item getSelectedItem() {
    return this.items.get(this.selectedItemId);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!super.mouseClicked(mouseX, mouseY, button)) {
      if (this.expanded) {
        int i = 0;
        for (Item item : items.values()) {
          if (item.isDisabled()) {
            continue;
          }
          final double itemY = this.getY() + this.getHeight() + this.itemHeight * i;
          if (item.getId() != this.selectedItemId && mouseY >= itemY && mouseY <= itemY + this.itemHeight) {
            this.onItemSelected(item);
          }
          i++;
        }
      }
      this.expanded = !this.expanded;
      return true;
    }
    return false;
  }

  public void addSelectListener(Consumer<Integer> listener) {
    this.addListener(DropdownItemSelectEvent.class, (dropdownComponent, dropdownItemSelectEvent) -> {
      listener.accept(dropdownItemSelectEvent.getItem().getId());
    });
  }

  protected void onItemSelected(Item item) {
    if (item == null) {
      logger.warn("Tried to select null Item in DropdownComponent");
      return;
    }

    if (this.selectedItemId != item.getId()) {
      this.selectedItemId = item.getId();
      this.post(new DropdownItemSelectEvent(item));
    }
  }

  @Override
  protected void layout() {
    super.layout();
    init();
  }

  private void init() {
    if (init) {
      return;
    }
    init = true;
    if (selectedItemId == -1 && items.size() > 0) {
      this.selectItem(items.keySet().stream().findFirst().get());
    }
  }

  @Override
  public int getZLevel() {
    return super.getZLevel() + (expanded ? 1 : 0);
  }

  @Override
  public boolean changeFocus(boolean focused) {
    boolean changeFocus = super.changeFocus(focused);
    if (changeFocus != expanded) {
      switchExpanded();
    }
    return this.expanded;
  }

  protected void switchExpanded() {
    this.expanded = !this.expanded;
    this.zLevelChanged();
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    if (expanded) {
      //so clicks elsewhere get passed to #mouseClicked to minimize the dropdown
      //might be buggy with components with higher Z level
      //also mouse enter and leave events will not work for other components (with lower Z) while a dropdown is expanded
      //TODO look for a better solution, maybe separate event when a component is clicked and general click event?
      return true;
    }
    return mouseX > this.getX() && mouseX < this.getX() + this.getWidth() &&
        mouseY > this.getY() && mouseY < this.getY() + this.getHeight()
        + (this.expanded ? this.items.size() * itemHeight : 0);
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderItem(this.getScaledContentY(), (int) this.getScaledContentHeight(),
        this.items.get(this.selectedItemId), false, false);
    renderArrow();
    if (this.expanded) {
      int i = 0;
      for (Item item : items.values()) {
        final double itemY = this.getScaledContentY() + this.getHeight() + this.itemHeight * i;
        this.renderItem(itemY, this.itemHeight, item, item.getId() == this.selectedItemId, item.isDisabled());
        i++;
      }
    }
  }

  @SuppressWarnings("deprecation")
  private void renderArrow() {
    //TODO make it smoother around the edges?
    RenderSystem.pushMatrix();
    {
      double xOffset = this.getScaledContentX() + this.getScaledContentWidth() * (1 - xArrowOffset);
      double yOffset = (this.getScaledContentY() + (this.getScaledContentHeight() - arrowHeight) / 2d);
      RenderSystem.translated(xOffset, yOffset, 0);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
      buffer.pos(0, 0, 0.0D).endVertex();
      buffer.pos(arrowWidth / 2.0D, arrowHeight, 0.0D).endVertex();
      buffer.pos(arrowWidth / 2.0D, arrowHeight - arrowLineWidthY, 0.0D).endVertex();
      buffer.pos(arrowLineWidthX, 0, 0.0D).endVertex();
      tessellator.draw();
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
      buffer.pos(arrowWidth - arrowLineWidthX, 0, 0.0D).endVertex();
      buffer.pos(arrowWidth / 2.0D, arrowHeight - arrowLineWidthY, 0.0D).endVertex();
      buffer.pos(arrowWidth / 2.0D, arrowHeight, 0.0D).endVertex();
      buffer.pos(arrowWidth, 0, 0.0D).endVertex();
      tessellator.draw();
    }
    RenderSystem.popMatrix();
  }

  private void calculateArrowLineWidthProjections() {
    double arrowLinePitchRad = Math.toRadians(90) - Math.atan(arrowWidth / 2D / arrowHeight);
    arrowLineWidthX = arrowLineWidth / Math.sin(arrowLinePitchRad);
    arrowLineWidthY = arrowHeight - Math.tan(arrowLinePitchRad) * (arrowWidth / 2D - arrowLineWidthX);
  }

  private void renderItem(double y, int height, Item item, boolean selected, boolean disabled) {
    RenderUtil.fill(this.getScaledContentX(), y,
        this.getScaledContentX() + this.getScaledContentWidth(), y + height,
        this.itemBgColour);
    int colour;
    if (disabled) {
      colour = TextFormatting.GRAY.getColor();
    } else if (selected) {
      colour = TextFormatting.GRAY.getColor();
    } else {
      colour = TextFormatting.WHITE.getColor();
    }

    this.minecraft.fontRenderer.func_238418_a_(item.text,
        (int) this.getScaledContentX() + 3, (int) y + (height - this.minecraft.fontRenderer.FONT_HEIGHT) / 2 + 1,
        (int) this.getScaledContentWidth(), colour);
  }

  public static class Item {
    private final int id;
    private final ITextComponent text;
    private boolean disabled = false;

    public Item(int id, ITextComponent text) {
      this.id = id;
      this.text = text;
    }

    public int getId() {
      return id;
    }

    public ITextComponent getText() {
      return text;
    }

    public boolean isDisabled() {
      return disabled;
    }

    public void setDisabled(boolean disabled) {
      this.disabled = disabled;
    }
  }
}
