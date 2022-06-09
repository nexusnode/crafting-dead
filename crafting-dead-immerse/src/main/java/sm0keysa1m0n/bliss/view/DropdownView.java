package sm0keysa1m0n.bliss.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.FontStyleSet;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.PaintStrokeCap;
import io.github.humbleui.skija.TextLine;
import io.github.humbleui.skija.shaper.Shaper;
import io.github.humbleui.skija.shaper.ShapingOptions;
import io.github.humbleui.types.Rect;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.layout.MeasureMode;
import sm0keysa1m0n.bliss.style.States;
import sm0keysa1m0n.bliss.view.event.ActionEvent;

public class DropdownView extends View {

  public static final int DEFAULT_HEIGHT = 14;
  public static final int DEFAULT_ITEM_BACKGROUND_COLOR = 0xFF444444;
  public static final int DEFAULT_SELECTED_ITEM_BACKGROUND_COLOR = 0xFF222222;
  public static final int DEFAULT_HOVERED_ITEM_BACKGROUND_COLOR = 0xFF333333;

  public static final float DEFAULT_ARROW_WIDTH = 6.0F;
  public static final float DEFAULT_ARROW_HEIGHT = 2.5F;
  public static final float DEFAULT_ARROW_LINE_WIDTH = 1F;
  public static final float DEFAULT_X_ARROW_OFFSET = 0.15F;

  private final List<Item> items = new ArrayList<>();

  private int itemBackgroundColor;
  private int selectedItemBackgroundColor;
  private int hoveredItemBackgroundColor;

  private boolean expanded = false;
  private int focusedItemIndex;
  private Item selectedItem;

  private float arrowWidth;
  private float arrowHeight;
  private float arrowLineWidth;
  private float xArrowOffset;

  private long fadeStartTimeMs;

  @Nullable
  private SoundEvent expandSound;
  @Nullable
  private SoundEvent itemHoverSound;

  @Nullable
  private Item lastHoveredListener;

  private FontMgr fontManager = FontMgr.getDefault();

  public DropdownView(Properties properties) {
    super(properties.focusable(true));
    this.addListener(ActionEvent.class, event -> {
      if (this.expanded) {
        this.getFocusedItem().select();
      }
      this.toggleExpanded();
    });

    this.itemBackgroundColor = DEFAULT_ITEM_BACKGROUND_COLOR;
    this.selectedItemBackgroundColor = DEFAULT_SELECTED_ITEM_BACKGROUND_COLOR;
    this.hoveredItemBackgroundColor = DEFAULT_HOVERED_ITEM_BACKGROUND_COLOR;
    this.arrowWidth = DEFAULT_ARROW_WIDTH;
    this.arrowHeight = DEFAULT_ARROW_HEIGHT;
    this.arrowLineWidth = DEFAULT_ARROW_LINE_WIDTH;
    this.xArrowOffset = DEFAULT_X_ARROW_OFFSET;

    this.getStyle().fontFamily.addListener(__ -> this.items.forEach(Item::refreshTextLine));
  }

  @Override
  public void styleRefreshed(FontMgr fontManager) {
    if (this.fontManager != FontMgr.getDefault()) {
      this.fontManager.close();
    }
    this.fontManager = fontManager;
    this.items.forEach(Item::refreshTextLine);
  }

  @Override
  public void close() {
    super.close();
    if (this.fontManager != FontMgr.getDefault()) {
      this.fontManager.close();
    }
  }

  protected Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    return new Vec2(width, DEFAULT_HEIGHT);
  }

  public DropdownView setArrowWidth(float arrowWidth) {
    this.arrowWidth = arrowWidth;
    return this;
  }

  public DropdownView setArrowHeight(float arrowHeight) {
    this.arrowHeight = arrowHeight;
    return this;
  }

  public DropdownView setArrowLineWidth(float arrowLineWidth) {
    this.arrowLineWidth = arrowLineWidth;
    return this;
  }

  /**
   * @param xArrowOffset RTL offset from 0.0F to 1.0F (something like percent margin right)
   */
  public DropdownView setXArrowOffset(float xArrowOffset) {
    this.xArrowOffset = xArrowOffset;
    return this;
  }

  public DropdownView setItemBackgroundColour(int itemBackgroundColour) {
    this.itemBackgroundColor = itemBackgroundColour;
    return this;
  }

  public DropdownView setSelectedItemBackgroundColour(int selectedItemBackgroundColour) {
    this.selectedItemBackgroundColor = selectedItemBackgroundColour;
    return this;
  }

  public DropdownView setHoveredItemBackgroundColour(int hoveredItemBackgroundColour) {
    this.hoveredItemBackgroundColor = hoveredItemBackgroundColour;
    return this;
  }

  public DropdownView addItem(Component text, Runnable actionListener) {
    this.items.add(new Item(this.items.size(), text, actionListener));
    return this;
  }

  public DropdownView setDisabled(int itemId, boolean disabled) {
    this.items.get(itemId).setDisabled(disabled);
    return this;
  }

  public DropdownView setExpandSound(@Nullable SoundEvent expandSound) {
    this.expandSound = expandSound;
    return this;
  }

  public DropdownView setItemHoverSound(@Nullable SoundEvent itemHoverSound) {
    this.itemHoverSound = itemHoverSound;
    return this;
  }

  private Item getFocusedItem() {
    return this.items.get(this.focusedItemIndex);
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    var hoveredListener = this.items.stream()
        .filter(item -> item.isMouseOver(mouseX, mouseY))
        .findFirst()
        .orElse(null);
    if (hoveredListener instanceof DropdownView.Item
        && hoveredListener != this.lastHoveredListener
        && this.itemHoverSound != null) {
      this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(this.itemHoverSound, 1.0F));
    }
    this.lastHoveredListener = hoveredListener;
  }

  @Override
  public boolean mousePressed(double mouseX, double mouseY, int button) {
    if (this.expanded) {
      for (var item : this.items) {
        if (item.isMouseOver(mouseX, mouseY) && !item.disabled) {
          this.focusedItemIndex = item.index;
          break;
        }
      }
    }
    return super.mousePressed(mouseX, mouseY, button);
  }

  @Override
  protected void focusChanged() {
    if (!this.isFocused() && this.expanded) {
      this.toggleExpanded();
    }
  }

  @Override
  public Optional<View> changeFocus(boolean forward) {
    if (this.isFocused() && this.expanded) {
      this.getStyleManager().addState(States.FOCUS_VISIBLE);
      this.getStyleManager().notifyListeners();
      this.toggleExpanded();
      return Optional.of(this);
    }
    return super.changeFocus(forward);
  }

  @Override
  protected void layout() {
    super.layout();
    this.items.forEach(Item::refreshTextLine);
  }

  @Override
  protected void added() {
    super.added();
    if (this.items.size() > 0) {
      this.items.get(0).select();
    }
  }

  @Override
  public void keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_DOWN) {
      this.focusedItemIndex = Math.min(this.focusedItemIndex + 1, this.items.size() - 1);
    } else if (keyCode == GLFW.GLFW_KEY_UP) {
      this.focusedItemIndex = Math.max(this.focusedItemIndex - 1, 0);
    }

    if (!this.expanded) {
      this.getFocusedItem().select();
    }

    super.keyPressed(keyCode, scanCode, modifiers);
  }

  protected void toggleExpanded() {
    this.fadeStartTimeMs = Util.getMillis();
    this.expanded = !this.expanded;
    if (this.expandSound != null) {
      this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(this.expandSound, 1.0F));
    }
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX > this.getScaledX() && mouseX < this.getScaledX() + this.getScaledWidth() &&
        mouseY > this.getScaledY() && mouseY < this.getScaledY() + this.getScaledHeight()
            + (this.expanded ? this.items.size() * this.getItemHeight() : 0);
  }

  protected int getItemHeight() {
    return (int) this.getScaledContentHeight();
  }

  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(poseStack, mouseX, mouseY, partialTicks);

    this.selectedItem.render(poseStack, Type.SELECTED, this.getAlpha());
    this.renderArrow(poseStack);

    float alpha = Mth.clamp((Util.getMillis() - this.fadeStartTimeMs) / 100.0F, 0.0F, 1.0F);
    if (!this.expanded) {
      alpha = 1.0F - alpha;
    }

    if (alpha == 0.0F) {
      return;
    }

    for (var item : this.items) {
      Type type;
      if (item.disabled) {
        type = Type.DISABLED;
      } else if (item == this.selectedItem) {
        type = Type.HIGHLIGHTED;
      } else if (item.isMouseOver(mouseX, mouseY) || this.focusedItemIndex == item.index) {
        type = Type.HOVERED;
      } else {
        type = Type.NONE;
      }

      item.render(poseStack, type, alpha * this.getAlpha());
    }
  }

  @SuppressWarnings("resource")
  private void renderArrow(PoseStack poseStack) {
    var xOffset =
        this.getScaledContentX() + this.getScaledContentWidth() * (1 - this.xArrowOffset);
    var yOffset =
        (this.getScaledContentY() + (this.getScaledContentHeight() - this.arrowHeight) / 2.0F);

    var canvas = this.graphicsContext.canvas();
    var scale = this.graphicsContext.scale();
    try (var paint = new Paint().setStrokeCap(PaintStrokeCap.ROUND)
        .setStrokeWidth(this.arrowLineWidth * this.graphicsContext.scale())
        .setColor(0xFFFFFFFF)
        .setMode(PaintMode.FILL)) {
      canvas.drawLine(
          xOffset * scale,
          yOffset * scale,
          (xOffset + this.arrowWidth / 2.0F) * scale,
          (yOffset + this.arrowHeight) * scale,
          paint);
      canvas.drawLine(
          (xOffset + this.arrowWidth / 2.0F) * scale,
          (yOffset + this.arrowHeight) * scale,
          (xOffset + this.arrowWidth) * scale,
          yOffset * scale,
          paint);
    }
  }

  private enum Type {

    HIGHLIGHTED, SELECTED, DISABLED, HOVERED, NONE;
  }

  public class Item {

    private final int index;
    private final Component text;
    private final Runnable actionListener;

    private boolean disabled = false;

    private TextLine textLine;

    public Item(int index, Component text, Runnable actionListener) {
      this.index = index;
      this.text = text;
      this.actionListener = actionListener;
      this.refreshTextLine();
    }

    private void refreshTextLine() {
      var style = this.text.getStyle();
      var fontStyle = style.isBold() && style.isItalic()
          ? FontStyle.BOLD_ITALIC
          : style.isBold()
              ? FontStyle.BOLD
              : style.isItalic()
                  ? FontStyle.ITALIC
                  : FontStyle.NORMAL;

      var fontSet = FontStyleSet.makeEmpty();
      for (var family : DropdownView.this.getStyle().fontFamily.get()) {
        fontSet = DropdownView.this.fontManager.matchFamily(family);
        if (fontSet.count() > 0) {
          break;
        }
      }

      this.textLine = Shaper.make(DropdownView.this.fontManager).shapeLine(this.text.getString(),
          new Font(fontSet.matchStyle(fontStyle), DropdownView.this.getStyle().fontSize.get()
              * DropdownView.this.graphicsContext.scale()),
          ShapingOptions.DEFAULT);
    }

    private void render(PoseStack poseStack, Type type, float alpha) {
      float y = this.getY();

      int backgroundColor = DropdownView.this.itemBackgroundColor;
      int textColor = ChatFormatting.GRAY.getColor();

      switch (type) {
        case SELECTED:
          y = DropdownView.this.getScaledContentY();
          backgroundColor ^= 0x000000;
          backgroundColor += 128 << 24;
          textColor = ChatFormatting.WHITE.getColor();
          break;
        case HIGHLIGHTED:
          backgroundColor = DropdownView.this.selectedItemBackgroundColor;
          break;
        case DISABLED:
          textColor = ChatFormatting.DARK_GRAY.getColor();
          break;
        case HOVERED:
          backgroundColor = DropdownView.this.hoveredItemBackgroundColor;
          break;
        default:
          break;
      }

      this.render(poseStack, DropdownView.this.getScaledContentX(), y,
          DropdownView.this.getScaledContentWidth(), DropdownView.this.getItemHeight(),
          backgroundColor, textColor, alpha);
    }

    private void render(PoseStack poseStack, float x, float y, float width, float height,
        int backgroundColor, int textColor, float alpha) {

      var canvas = DropdownView.this.graphicsContext.canvas();

      var style = this.text.getStyle();
      var scale = DropdownView.this.graphicsContext.scale();

      canvas.translate(x * scale, y * scale);
      canvas.scale(DropdownView.this.getXScale(), DropdownView.this.getYScale());

      try (var paint = new Paint()) {
        paint.setMode(PaintMode.FILL);
        paint.setColor(Color.multiplyAlpha(backgroundColor, alpha));
        canvas.drawRect(Rect.makeWH(width * scale, height * scale), paint);
      }

      try (var paint = new Paint()) {
        paint.setColor(Color.multiplyAlpha(style.getColor() == null
            ? DropdownView.this.getStyle().color.get().valueHex()
            : style.getColor().getValue() + Color.FULL_ALPHA, alpha));
        canvas.translate(4 * scale,
            this.textLine.getHeight() + (height * scale) / 2.0F
                - this.textLine.getXHeight() * 2.0F);
        canvas.drawTextLine(this.textLine, 0, 0, paint);
      }

      canvas.resetMatrix();
    }

    private void select() {
      if (DropdownView.this.selectedItem != this) {
        this.actionListener.run();
        DropdownView.this.selectedItem = this;
      }
    }

    public void setDisabled(boolean disabled) {
      this.disabled = disabled;
    }

    private float getY() {
      return DropdownView.this.getScaledContentY()
          + DropdownView.this.getScaledContentHeight()
          + DropdownView.this.getItemHeight() * this.index;
    }

    private boolean isMouseOver(double mouseX, double mouseY) {
      final float y = this.getY();
      return DropdownView.this.isMouseOver(mouseX, mouseY) && mouseY >= y
          && mouseY <= y + DropdownView.this.getItemHeight();
    }
  }
}
