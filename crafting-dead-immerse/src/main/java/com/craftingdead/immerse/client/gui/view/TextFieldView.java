package com.craftingdead.immerse.client.gui.view;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class TextFieldView<L extends Layout> extends View<TextFieldView<L>, L> {

  private final Font font;

  private String value = "";
  private int maxLength = 32;
  private int frame;
  private boolean editable = true;
  private boolean shiftPressed;
  private int displayIndex;
  private int cursorIndex;
  private int highlightIndex;

  private final ValueStyleProperty<Color> textColor = Util.make(
      ValueStyleProperty.create("text_color", Color.class, new Color(0xFFE0E0E0)),
      this::registerValueProperty);

  private final ValueStyleProperty<Color> textColorUneditable = Util.make(
      ValueStyleProperty.create("text_color_uneditable", Color.class, new Color(0xFF707070)),
      this::registerValueProperty);

  private String suggestion;
  private Consumer<String> responder;
  private Predicate<String> filter = Objects::nonNull;
  private BiFunction<String, Integer, FormattedCharSequence> formatter =
      (value, p_195610_1_) -> FormattedCharSequence.forward(value, Style.EMPTY);

  public TextFieldView(L layout) {
    super(layout);
    this.setFocusable(true);
    this.font = this.minecraft.font;
  }

  public TextFieldView<L> setResponder(Consumer<String> responder) {
    this.responder = responder;
    return this;
  }

  public TextFieldView<L> setFormatter(
      BiFunction<String, Integer, FormattedCharSequence> formatter) {
    this.formatter = formatter;
    return this;
  }

  @Override
  public void tick() {
    super.tick();
    ++this.frame;
  }

  public void setValue(String value) {
    if (this.filter.test(value)) {
      if (value.length() > this.maxLength) {
        this.value = value.substring(0, this.maxLength);
      } else {
        this.value = value;
      }

      this.moveCursorToEnd();
      this.setHighlightIndex(this.cursorIndex);
      this.onValueChange(value);
    }
  }

  public String getValue() {
    return this.value;
  }

  public String getHighlighted() {
    int startIndex =
        this.cursorIndex < this.highlightIndex ? this.cursorIndex : this.highlightIndex;
    int endIndex =
        this.cursorIndex < this.highlightIndex ? this.highlightIndex : this.cursorIndex;
    return this.value.substring(startIndex, endIndex);
  }

  public TextFieldView<L> setFilter(Predicate<String> filter) {
    this.filter = filter;
    return this;
  }

  public void insertText(String text) {
    int startIndex =
        this.cursorIndex < this.highlightIndex ? this.cursorIndex : this.highlightIndex;
    int endIndex =
        this.cursorIndex < this.highlightIndex ? this.highlightIndex : this.cursorIndex;
    int maxLength = this.maxLength - this.value.length() - (startIndex - endIndex);
    String filteredText = SharedConstants.filterText(text);
    int filteredLength = filteredText.length();
    if (maxLength < filteredLength) {
      filteredText = filteredText.substring(0, maxLength);
      filteredLength = maxLength;
    }

    String newValue =
        new StringBuilder(this.value).replace(startIndex, endIndex, filteredText).toString();
    if (this.filter.test(newValue)) {
      this.value = newValue;
      this.setCursorIndex(startIndex + filteredLength);
      this.setHighlightIndex(this.cursorIndex);
      this.onValueChange(this.value);
    }
  }

  private void onValueChange(String value) {
    if (this.responder != null) {
      this.responder.accept(value);
    }
  }

  private void deleteText(int count) {
    if (Screen.hasControlDown()) {
      this.deleteWords(count);
    } else {
      this.deleteChars(count);
    }
  }

  public void deleteWords(int offset) {
    if (!this.value.isEmpty()) {
      if (this.highlightIndex != this.cursorIndex) {
        this.insertText("");
      } else {
        this.deleteChars(this.getWordPosition(offset) - this.cursorIndex);
      }
    }
  }

  public void deleteChars(int offset) {
    if (!this.value.isEmpty()) {
      if (this.highlightIndex != this.cursorIndex) {
        this.insertText("");
      } else {
        int index = this.getCursorIndex(offset);
        int minIndex = Math.min(index, this.cursorIndex);
        int maxIndex = Math.max(index, this.cursorIndex);
        if (minIndex != maxIndex) {
          String newValue = new StringBuilder(this.value).delete(minIndex, maxIndex).toString();
          if (this.filter.test(newValue)) {
            this.value = newValue;
            this.moveCursorTo(minIndex);
          }
        }
      }
    }
  }

  public int getWordPosition(int offset) {
    return this.getWordPosition(offset, this.getCursorPosition());
  }

  private int getWordPosition(int offset, int index) {
    return this.getWordPosition(offset, index, true);
  }

  private int getWordPosition(int offset, int fromIndex, boolean p_146197_3_) {
    int index = fromIndex;
    boolean backward = offset < 0;
    int count = Math.abs(offset);

    for (int i = 0; i < count; ++i) {
      if (!backward) {
        int length = this.value.length();
        index = this.value.indexOf(' ', index);
        if (index == -1) {
          index = length;
        } else {
          while (p_146197_3_ && index < length && this.value.charAt(index) == ' ') {
            ++index;
          }
        }
      } else {
        while (p_146197_3_ && index > 0 && this.value.charAt(index - 1) == ' ') {
          --index;
        }

        while (index > 0 && this.value.charAt(index - 1) != ' ') {
          --index;
        }
      }
    }

    return index;
  }

  public void moveCursor(int offset) {
    this.moveCursorTo(this.getCursorIndex(offset));
  }

  private int getCursorIndex(int offset) {
    return Util.offsetByCodepoints(this.value, this.cursorIndex, offset);
  }

  public void moveCursorTo(int cursorIndex) {
    this.setCursorIndex(cursorIndex);
    if (!this.shiftPressed) {
      this.setHighlightIndex(this.cursorIndex);
    }

    this.onValueChange(this.value);
  }

  public void setCursorIndex(int cursorIndex) {
    this.cursorIndex = Mth.clamp(cursorIndex, 0, this.value.length());
  }

  public void moveCursorToStart() {
    this.moveCursorTo(0);
  }

  public void moveCursorToEnd() {
    this.moveCursorTo(this.value.length());
  }

  @Override
  public boolean keyPressed(int key, int scancode, int mods) {
    if (!this.canConsumeInput()) {
      return false;
    }

    this.shiftPressed = Screen.hasShiftDown();
    if (Screen.isSelectAll(key)) {
      this.moveCursorToEnd();
      this.setHighlightIndex(0);
      return true;
    } else if (Screen.isCopy(key)) {
      this.minecraft.keyboardHandler.setClipboard(this.getHighlighted());
      return true;
    } else if (Screen.isPaste(key)) {
      if (this.editable) {
        this.insertText(this.minecraft.keyboardHandler.getClipboard());
      }

      return true;
    } else if (Screen.isCut(key)) {
      this.minecraft.keyboardHandler.setClipboard(this.getHighlighted());
      if (this.editable) {
        this.insertText("");
      }

      return true;
    }

    switch (key) {
      case GLFW.GLFW_KEY_BACKSPACE:
        if (this.editable) {
          this.shiftPressed = false;
          this.deleteText(-1);
          this.shiftPressed = Screen.hasShiftDown();
        }

        return true;
      case GLFW.GLFW_KEY_INSERT:
      case GLFW.GLFW_KEY_DOWN:
      case GLFW.GLFW_KEY_UP:
      case GLFW.GLFW_KEY_PAGE_UP:
      case GLFW.GLFW_KEY_PAGE_DOWN:
      default:
        return false;
      case GLFW.GLFW_KEY_DELETE:
        if (this.editable) {
          this.shiftPressed = false;
          this.deleteText(1);
          this.shiftPressed = Screen.hasShiftDown();
        }

        return true;
      case GLFW.GLFW_KEY_RIGHT:
        if (Screen.hasControlDown()) {
          this.moveCursorTo(this.getWordPosition(1));
        } else {
          this.moveCursor(1);
        }

        return true;
      case GLFW.GLFW_KEY_LEFT:
        if (Screen.hasControlDown()) {
          this.moveCursorTo(this.getWordPosition(-1));
        } else {
          this.moveCursor(-1);
        }

        return true;
      case GLFW.GLFW_KEY_HOME:
        this.moveCursorToStart();
        return true;
      case GLFW.GLFW_KEY_END:
        this.moveCursorToEnd();
        return true;
    }
  }

  public boolean canConsumeInput() {
    return this.isFocused() && this.isEditable();
  }

  @Override
  public boolean charTyped(char ch, int mods) {
    if (!this.canConsumeInput()) {
      return false;
    } else if (SharedConstants.isAllowedChatCharacter(ch)) {
      if (this.editable) {
        this.insertText(Character.toString(ch));
      }

      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    super.mouseClicked(mouseX, mouseY, button);

    if (this.isFocused() && this.isHovered() && button == GLFW.GLFW_MOUSE_BUTTON_1) {
      int i = Mth.ceil(mouseX) - Mth.ceil(this.getScaledContentX());
      String s = this.font.plainSubstrByWidth(this.value.substring(this.displayIndex),
          Mth.floor(this.getScaledContentWidth()));
      this.moveCursorTo(this.font.plainSubstrByWidth(s, i).length() + this.displayIndex);
      return true;
    }

    return false;
  }

  @Override
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    int textColor =
        this.editable ? this.textColor.get().getHex() : this.textColorUneditable.get().getHex();
    int cursorIndex = this.cursorIndex - this.displayIndex;
    int highlightMaxIndex = this.highlightIndex - this.displayIndex;
    var text = this.font.plainSubstrByWidth(this.value.substring(this.displayIndex),
        Mth.floor(this.getScaledContentX()));
    var cursorVisisble = cursorIndex >= 0 && cursorIndex <= text.length();
    var cursorBlink = this.isFocused() && this.frame / 6 % 2 == 0 && cursorVisisble;
    var x = this.getScaledContentX() + 2;
    var y = this.getScaledContentY()
        + this.getScaledContentHeight() / 2.0F
        - this.font.lineHeight / 2.0F;
    var remainingX = x;
    if (highlightMaxIndex > text.length()) {
      highlightMaxIndex = text.length();
    }

    if (!text.isEmpty()) {
      String highlightedText = cursorVisisble ? text.substring(0, cursorIndex) : text;
      remainingX = this.font.drawShadow(poseStack,
          this.formatter.apply(highlightedText, this.displayIndex), x,
          y, textColor);
    }

    var pipeCursor =
        this.cursorIndex < this.value.length() || this.value.length() >= this.getMaxLength();
    var highlightX = remainingX;
    if (!cursorVisisble) {
      highlightX = cursorIndex > 0 ? x + this.getScaledContentWidth() : x;
    } else if (pipeCursor) {
      highlightX = remainingX - 1;
      --remainingX;
    }

    if (!text.isEmpty() && cursorVisisble && cursorIndex < text.length()) {
      this.font.drawShadow(poseStack,
          this.formatter.apply(text.substring(cursorIndex), this.cursorIndex), remainingX, y,
          textColor);
    }

    if (!pipeCursor && this.suggestion != null) {
      this.font.drawShadow(poseStack, this.suggestion, highlightX - 1.0F, y, 0xFF808080);
    }

    if (cursorBlink) {
      if (pipeCursor) {
        RenderUtil.fill(poseStack, highlightX, y - 1, highlightX + 1, y + 1 + 9, 0xFFD0D0D0);
      } else {
        this.font.drawShadow(poseStack, "_", (float) highlightX, (float) y, textColor);
      }
    }

    if (highlightMaxIndex != cursorIndex) {
      float highlightX2 = x + this.font.width(text.substring(0, highlightMaxIndex));
      this.renderHighlight(highlightX, y - 1, highlightX2 - 1, y + 1 + 9);
    }
  }

  private void renderHighlight(float x, float y, float x2, float y2) {
    if (x < x2) {
      float i = x;
      x = x2;
      x2 = i;
    }

    if (y < y2) {
      float i = y;
      y = y2;
      y2 = i;
    }

    float maxX = this.getScaledContentX() + this.getScaledContentWidth();
    if (x2 > maxX) {
      x2 = maxX;
    }

    if (x > maxX) {
      x = maxX;
    }

    final var tessellator = Tesselator.getInstance();
    final var builder = tessellator.getBuilder();
    RenderSystem.setShader(GameRenderer::getPositionShader);
    RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
    RenderSystem.disableTexture();
    RenderSystem.enableColorLogicOp();
    RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
    builder.vertex(x, y2, 0.0D).endVertex();
    builder.vertex(x2, y2, 0.0D).endVertex();
    builder.vertex(x2, y, 0.0D).endVertex();
    builder.vertex(x, y, 0.0D).endVertex();
    tessellator.end();
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableColorLogicOp();
    RenderSystem.enableTexture();
  }

  public TextFieldView<L> setMaxLength(int maxLength) {
    this.maxLength = maxLength;
    if (this.value.length() > maxLength) {
      this.value = this.value.substring(0, maxLength);
      this.onValueChange(this.value);
    }
    return this;
  }

  private int getMaxLength() {
    return this.maxLength;
  }

  public int getCursorPosition() {
    return this.cursorIndex;
  }

  public ValueStyleProperty<Color> getTextColorProperty() {
    return this.textColor;
  }

  public ValueStyleProperty<Color> getTextColorUneditableProperty() {
    return this.textColorUneditable;
  }

  @Override
  public boolean changeFocus(boolean forward) {
    if (this.editable && super.changeFocus(forward)) {
      if (forward) {
        this.frame = 0;
      }
      return true;
    }

    return false;
  }

  private boolean isEditable() {
    return this.editable;
  }

  public TextFieldView<L> setEditable(boolean editable) {
    this.editable = editable;
    return this;
  }

  public void setHighlightIndex(int highlightIndex) {
    int length = this.value.length();
    this.highlightIndex = Mth.clamp(highlightIndex, 0, length);
    if (this.font != null) {
      if (this.displayIndex > length) {
        this.displayIndex = length;
      }

      int width = Mth.floor(this.getScaledContentWidth());
      String s = this.font.plainSubstrByWidth(this.value.substring(this.displayIndex), width);
      int k = s.length() + this.displayIndex;
      if (this.highlightIndex == this.displayIndex) {
        this.displayIndex -= this.font.plainSubstrByWidth(this.value, width, true).length();
      }

      if (this.highlightIndex > k) {
        this.displayIndex += this.highlightIndex - k;
      } else if (this.highlightIndex <= this.displayIndex) {
        this.displayIndex -= this.displayIndex - this.highlightIndex;
      }

      this.displayIndex = Mth.clamp(this.displayIndex, 0, length);
    }
  }

  public TextFieldView<L> setSuggestion(@Nullable String suggestion) {
    this.suggestion = suggestion;
    return this;
  }
}
