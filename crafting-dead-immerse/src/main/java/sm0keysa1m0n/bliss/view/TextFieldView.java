package sm0keysa1m0n.bliss.view;

import java.lang.ref.Reference;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.TextBlob;
import io.github.humbleui.skija.TextLine;
import io.github.humbleui.skija.Typeface;
import io.github.humbleui.skija.impl.Native;
import io.github.humbleui.skija.impl.Stats;
import io.github.humbleui.skija.shaper.Shaper;
import io.github.humbleui.types.Rect;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public class TextFieldView extends View {

  private static final int CARET_WIDTH = 1;

  private static long textSelectCursor = MemoryUtil.NULL;

  private FontMgr fontManager = FontMgr.getDefault();
  @Nullable
  private Typeface typeface;

  private String text = "";

  private Font font;
  private TextLine textLine;

  private int selectionOffset;

  private int caretIndex;

  private int caretBlinkTicks;

  private float xOffset = CARET_WIDTH;

  private int maxLength;

  @Nullable
  private Consumer<String> responder;

  @Nullable
  private String placeholder;

  public TextFieldView(Properties properties) {
    super(properties.focusable(true));
  }

  public String getText() {
    return this.text;
  }

  public TextFieldView setMaxLength(int maxLength) {
    this.maxLength = maxLength;
    if (maxLength != 0 && this.text.length() > maxLength) {
      this.setText(this.text);
    }
    return this;
  }

  public TextFieldView setResponder(@Nullable Consumer<String> responder) {
    this.responder = responder;
    return this;
  }

  public TextFieldView setPlaceholder(@Nullable String placeholder) {
    this.placeholder = placeholder;
    return this;
  }

  @Override
  public void styleRefreshed(FontMgr fontManager) {
    if (this.fontManager != FontMgr.getDefault()) {
      this.fontManager.close();
    }
    this.fontManager = fontManager;

    this.typeface = null;
    for (var family : this.getStyle().fontFamily.get()) {
      var fontSet = this.fontManager.matchFamily(family);
      if (fontSet.count() > 0) {
        this.typeface = fontSet.matchStyle(FontStyle.NORMAL);
        break;
      }
    }

    this.refreshTextLine();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.caretBlinkTicks++ >= SharedConstants.TICKS_PER_SECOND) {
      this.caretBlinkTicks = 0;
    }
  }

  @Override
  public boolean changeFocus(boolean forward) {
    if (super.changeFocus(forward)) {
      this.selectAll();
      return true;
    }
    return false;
  }

  @Override
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    super.renderContent(poseStack, mouseX, mouseY, partialTick);
    this.skia.begin();
    {
      var canvas = this.skia.canvas();
      var scale = (float) this.window.getGuiScale();

      canvas.clipRect(Rect.makeXYWH(
          this.getScaledContentX() * scale,
          this.getScaledContentY() * scale,
          this.getScaledContentWidth() * scale,
          this.getScaledContentHeight() * scale));

      var textColor = RenderUtil.multiplyAlpha(
          this.getStyle().color.get().valueHex(), this.getAlpha());

      canvas.translate((this.getScaledContentX() * scale) + this.xOffset, 0);

      this.drawCaretOrSelection(canvas, textColor);

      canvas.translate(0, this.getScaledContentY() * scale + this.textLine.getHeight()
          + (this.getScaledContentHeight() * scale) / 2.0F
          - this.textLine.getXHeight() * 2.0F);

      if (this.text.length() > 0) {
        try (var paint = new Paint()) {
          paint.setColor(textColor);

          if (this.selectionOffset == 0) {
            canvas.drawTextLine(this.textLine, 0, 0, paint);
          } else {
            var glyphs = this.textLine.getGlyphs();
            var positions = this.textLine.getPositions();

            var startIndex = Math.min(this.caretIndex, this.getOffsetCaretIndex());
            if (startIndex != 0) {
              var headGlyphs = Arrays.copyOfRange(glyphs, 0, startIndex);
              var headPositions = Arrays.copyOfRange(positions, 0, startIndex * 2);
              try (var blob = makeBlobFromPos(headGlyphs, headPositions, this.font)) {
                canvas.drawTextBlob(blob, 0, 0, paint);
              }
            }

            var endIndex = Math.max(this.caretIndex, this.getOffsetCaretIndex());
            if (endIndex != this.text.length()) {
              var tailGlyphs = Arrays.copyOfRange(glyphs, endIndex, glyphs.length);
              var tailPositions = Arrays.copyOfRange(positions, endIndex * 2, positions.length * 2);
              try (var blob = makeBlobFromPos(tailGlyphs, tailPositions, this.font)) {
                canvas.drawTextBlob(blob, 0, 0, paint);
              }
            }

            var selectedGlyphs = Arrays.copyOfRange(glyphs, startIndex, endIndex);
            var selectedPositions = Arrays.copyOfRange(positions, startIndex * 2, endIndex * 2);
            try (var blob = makeBlobFromPos(selectedGlyphs, selectedPositions, this.font);
                var selectedPaint = new Paint()) {
              selectedPaint.setColor(0xFFFFFF).setAlphaf(this.getAlpha());
              canvas.drawTextBlob(blob, 0, 0, selectedPaint);
            }
          }

        }
      } else if (this.placeholder != null) {
        try (var paint = new Paint()) {
          paint.setColor(0x808080).setAlphaf(this.getAlpha());
          canvas.drawString(this.placeholder, 0, 0, this.font, paint);
        }
      }

      canvas.resetMatrix();

    }
    this.skia.end();
  }

  private void drawCaretOrSelection(Canvas canvas, int textColor) {
    if (!this.isFocused()) {
      return;
    }

    var glyphs = this.textLine.getGlyphs();

    var scale = (float) this.window.getGuiScale();
    var caretX = 0.0F;
    var lineHeight = this.textLine.getHeight() - this.textLine.getXHeight() / 2.0F;

    if (glyphs.length != 0) {
      var positions = this.textLine.getPositions();
      var widths = this.font.getWidths(glyphs);
      caretX = positions[Math.min(this.caretIndex, glyphs.length - 1) * 2];
      if (this.caretIndex == glyphs.length) {
        caretX += widths[glyphs.length - 1];
      }

      if (this.selectionOffset != 0) {
        var selectLeft = 0.0F;
        var selectRight = 0.0F;

        var offsetIndex = this.getOffsetCaretIndex();

        if (this.caretIndex < offsetIndex) {
          selectLeft = caretX;
          selectRight = positions[Math.min(offsetIndex, glyphs.length - 1) * 2];
          if (offsetIndex == glyphs.length) {
            selectRight += widths[glyphs.length - 1];
          }
        } else {
          selectLeft = positions[offsetIndex * 2];
          selectRight = caretX;
        }

        try (var paint = new Paint()) {
          paint.setColor(0x3297FD).setAlphaf(this.getAlpha());
          paint.setMode(PaintMode.FILL);
          canvas.drawRect(Rect.makeLTRB(
              selectLeft,
              (this.getScaledContentY() * scale)
                  + ((this.getScaledContentHeight() * scale) - lineHeight) / 2.0F,
              selectRight, (this.getScaledContentY() * scale)
                  + ((this.getScaledContentHeight() * scale) - lineHeight) / 2.0F + lineHeight),
              paint);
        }
        return;
      }
    }

    if (this.caretBlinkTicks >= 10) {
      return;
    }

    // Draw caret
    try (var paint = new Paint()) {
      paint.setColor(textColor);
      paint.setMode(PaintMode.FILL);
      var caretWidth = CARET_WIDTH * scale;
      canvas.drawRect(Rect.makeXYWH(
          caretX - caretWidth / 2.0F,
          (this.getScaledContentY() * scale)
              + ((this.getScaledContentHeight() * scale) - lineHeight) / 2.0F,
          caretWidth, lineHeight), paint);
    }
  }

  @Override
  public boolean charTyped(char ch, int mods) {
    if (!SharedConstants.isAllowedChatCharacter(ch)) {
      return false;
    }

    if (this.selectionOffset != 0) {
      this.deleteChars(this.selectionOffset);
    }
    this.insertText(Character.toString(ch));

    return true;
  }

  @Override
  public boolean keyPressed(int key, int scancode, int mods) {
    if (Screen.isSelectAll(key)) {
      this.selectAll();
      return true;
    } else if (Screen.isCopy(key)) {
      this.getSelectedText().ifPresent(this.minecraft.keyboardHandler::setClipboard);
      return true;
    } else if (Screen.isPaste(key)) {
      if (this.selectionOffset != 0) {
        this.deleteChars(this.selectionOffset);
      }
      this.insertText(this.minecraft.keyboardHandler.getClipboard());
      return true;
    } else if (Screen.isCut(key)) {
      this.getSelectedText().ifPresent(selected -> {
        this.minecraft.keyboardHandler.setClipboard(selected);
        this.deleteChars(this.selectionOffset);
      });
      return true;
    }

    switch (key) {
      case GLFW.GLFW_KEY_BACKSPACE:
        this.deleteText(this.selectionOffset == 0 ? -1 : this.selectionOffset);
        return true;
      case GLFW.GLFW_KEY_DELETE:
        this.deleteText(this.selectionOffset == 0 ? 1 : this.selectionOffset);
        return true;
      case GLFW.GLFW_KEY_RIGHT: {
        var select = Screen.hasShiftDown();
        var word = Screen.hasControlDown();
        if (select) {
          this.setSelectionOffset(
              word ? this.getRightWordIndex(1) - this.caretIndex : this.selectionOffset + 1);
        } else {
          this.setCaretIndex(
              word ? this.getRightWordIndex(1)
                  : this.selectionOffset != 0
                      ? Math.max(this.getOffsetCaretIndex(), this.caretIndex)
                      : this.caretIndex + 1);
        }
        return true;
      }
      case GLFW.GLFW_KEY_LEFT: {
        var select = Screen.hasShiftDown();
        var word = Screen.hasControlDown();
        if (select) {
          this.setSelectionOffset(
              word ? this.getLeftWordIndex(1) - this.caretIndex : this.selectionOffset - 1);
        } else {
          this.setCaretIndex(
              word ? this.getLeftWordIndex(1)
                  : this.selectionOffset != 0
                      ? Math.min(this.getOffsetCaretIndex(), this.caretIndex)
                      : this.caretIndex - 1);
        }
        return true;
      }
      case GLFW.GLFW_KEY_HOME:
        this.setCaretIndex(0);
        return true;
      case GLFW.GLFW_KEY_END:
        this.setCaretIndex(this.text.length());
        return true;
      default:
        return false;
    }
  }

  private Optional<String> getSelectedText() {
    if (this.selectionOffset == 0) {
      return Optional.empty();
    }
    return Optional.of(this.selectionOffset < 0
        ? this.text.substring(this.getOffsetCaretIndex(), this.caretIndex)
        : this.text.substring(this.caretIndex, this.getOffsetCaretIndex()));
  }

  private void insertText(String text) {
    var head = this.text.substring(0, this.caretIndex);
    var tail = this.text.substring(this.caretIndex);
    this.caretIndex += text.length();
    this.setText(head + text + tail);
  }

  private void deleteText(int count) {
    if (Screen.hasControlDown()) {
      this.deleteWords(count);
    } else {
      this.deleteChars(count);
    }
  }

  public void deleteWords(int offset) {
    if (this.text.isEmpty()) {
      return;
    }

    if (offset < 0) {
      int wordIndex = this.getLeftWordIndex(offset);
      var head = this.text.substring(0, wordIndex);
      var tail = this.text.substring(this.caretIndex);
      this.caretIndex = wordIndex;
      this.setText(head + tail);
      return;
    }

    if (offset > 0) {
      var wordIndex = this.getRightWordIndex(offset);
      var head = this.text.substring(0, this.caretIndex);
      var tail = this.text.substring(wordIndex);
      this.setText(head + tail);
    }
  }

  private int getRightWordIndex(int count) {
    var wordCount = 0;
    var lastSpace = false;
    var startIndex = this.getOffsetCaretIndex();
    for (int i = startIndex; i < this.text.length(); i++) {
      var space = Character.isSpaceChar(this.text.charAt(i));
      if (!space && lastSpace && ++wordCount >= count) {
        return i;
      }
      lastSpace = space;

      if (space) {
        continue;
      }
    }

    return this.text.length();
  }

  private int getLeftWordIndex(int count) {
    int index = 0;
    var wordCount = 0;
    var lastSpace = true;
    var fromIndex = this.getOffsetCaretIndex();
    for (int i = fromIndex - 1; i >= 0; i--) {
      if (Character.isSpaceChar(this.text.charAt(i))) {
        if (!lastSpace && ++wordCount >= count) {
          break;
        }
        lastSpace = true;
        continue;
      } else {
        lastSpace = false;
      }

      index = i;
    }

    return index;
  }

  public void deleteChars(int offset) {
    if (this.text.isEmpty()) {
      return;
    }

    if (offset < 0) {
      var endIndex = Math.max(0, this.caretIndex + offset);
      var head = this.text.substring(0, endIndex);
      var tail = this.text.substring(this.caretIndex);
      this.caretIndex = endIndex;
      this.setText(head + tail);
      return;
    }

    if (offset > 0) {
      var endIndex = Math.min(this.caretIndex + offset, this.text.length());
      var head = this.text.substring(0, this.caretIndex);
      if (endIndex == this.text.length()) {
        this.setText(head);
        return;
      }
      var tail = this.text.substring(endIndex);
      this.setText(head + tail);
    }
  }

  private void setText(String text) {
    this.text = this.maxLength > 0 && text.length() > this.maxLength
        ? text.substring(0, this.maxLength)
        : text;
    this.refreshTextLine();
    this.setCaretIndex(this.caretIndex);
    this.clearSelection();
    if (this.responder != null) {
      this.responder.accept(this.text);
    }
  }

  private void refreshTextLine() {
    this.font = new Font(
        this.typeface, this.getStyle().fontSize.get() * (float) this.window.getGuiScale());
    this.textLine = Shaper.makeShapeDontWrapOrReorder(this.fontManager)
        .shapeLine(this.text, this.font);
  }

  @Override
  protected void layout() {
    this.refreshTextLine();
    super.layout();
  }

  @Override
  public void mouseEntered(double mouseX, double mouseY) {
    super.mouseEntered(mouseX, mouseY);
    this.updateCursor();
  }

  @Override
  public void mouseLeft(double mouseX, double mouseY) {
    super.mouseLeft(mouseX, mouseY);
    this.updateCursor();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!super.mouseClicked(mouseX, mouseY, button)) {
      return false;
    }

    if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
      return false;
    }

    if (Screen.hasShiftDown()) {
      this.setSelectionOffset(this.getCharIndexAt((float) mouseX) - this.caretIndex);
    } else {
      this.clearSelection();
      this.setCaretIndex(this.getCharIndexAt((float) mouseX));
    }

    return true;
  }

  private void setCaretIndex(int index) {
    this.caretBlinkTicks = 0;
    this.caretIndex = Mth.clamp(index, 0, this.text.length());
    if (this.selectionOffset == 0) {
      this.updateScrollOffset();
    } else {
      // This also calls updateScrollOffset.
      this.clearSelection();
    }
  }

  private int getOffsetCaretIndex() {
    return this.caretIndex + this.selectionOffset;
  }

  private void updateScrollOffset() {
    var positions = this.textLine.getPositions();
    if (positions.length == 0) {
      this.xOffset = CARET_WIDTH;
      return;
    }

    var offsetIndex = this.getOffsetCaretIndex();
    var widths = this.font.getWidths(this.textLine.getGlyphs());
    var scale = (float) this.window.getGuiScale();
    var glyphIndex = Math.min(offsetIndex, this.text.length() - 1);
    var glyphWidth = widths[glyphIndex];
    var glyphX = positions[glyphIndex * 2];
    if (offsetIndex == this.text.length()) {
      glyphX += glyphWidth;
    }
    if (glyphX + this.xOffset > this.getScaledContentWidth() * scale) {
      this.xOffset = (this.getScaledContentWidth() * scale) - glyphX - CARET_WIDTH;
    } else if (glyphX + this.xOffset < 0) {
      this.xOffset = -glyphX + CARET_WIDTH;
    }
  }

  private void selectAll() {
    this.setCaretIndex(0);
    this.setSelectionOffset(this.text.length());
  }

  private void clearSelection() {
    this.setSelectionOffset(0);
  }

  private void setSelectionOffset(int offset) {
    this.selectionOffset = offset;

    if (this.selectionOffset != 0) {
      var offsetIndex = this.getOffsetCaretIndex();
      if (offsetIndex > this.text.length()) {
        this.selectionOffset = this.text.length() - this.caretIndex;
      } else if (offsetIndex < 0) {
        this.selectionOffset = -this.caretIndex;
      }
    }

    this.updateScrollOffset();
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
      return true;
    }

    this.setSelectionOffset(this.getCharIndexAt((float) (mouseX + deltaX)) - this.caretIndex);

    return true;
  }

  private int getCharIndexAt(float x) {
    var scale = (float) this.window.getGuiScale();
    var glyphs = this.textLine.getGlyphs();
    if (glyphs.length == 0) {
      return 0;
    }

    var positions = this.textLine.getPositions();
    var widths = this.font.getWidths(glyphs);

    for (int i = 0; i < glyphs.length; i++) {
      var width = widths[i] / scale;
      var position = (positions[i * 2] / scale) + this.getScaledContentX() + this.xOffset / scale;

      if (x >= position) {
        if (x <= position + (width / 2.0F)) {
          return i;
        }

        if (x <= position + width) {
          return i + 1;
        }
      } else if (i == 0) {
        return 0;
      }
    }

    return glyphs.length;
  }

  private void updateCursor() {
    if (!this.isHovered()) {
      GLFW.glfwSetCursor(this.window.getWindow(), MemoryUtil.NULL);
      return;
    }

    if (textSelectCursor == MemoryUtil.NULL) {
      textSelectCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    }

    GLFW.glfwSetCursor(this.window.getWindow(), textSelectCursor);
  }

  private static TextBlob makeBlobFromPos(short[] glyphs, float[] floatPos, Font font) {
    try {
      Stats.onNativeCall();
      var ptr = TextBlob._nMakeFromPos(glyphs, floatPos, Native.getPtr(font));
      return ptr == 0 ? null : new TextBlob(ptr);
    } finally {
      Reference.reachabilityFence(font);
    }
  }
}
