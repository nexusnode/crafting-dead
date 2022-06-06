package sm0keysa1m0n.bliss.view;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PictureRecorder;
import io.github.humbleui.skija.paragraph.DecorationStyle;
import io.github.humbleui.skija.paragraph.FontCollection;
import io.github.humbleui.skija.paragraph.Paragraph;
import io.github.humbleui.skija.paragraph.ParagraphBuilder;
import io.github.humbleui.skija.paragraph.ParagraphStyle;
import io.github.humbleui.skija.paragraph.TextStyle;
import io.github.humbleui.types.Rect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec2;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.layout.MeasureMode;

public class TextView extends View {

  private FormattedText text = TextComponent.EMPTY;
  private boolean wrap = true;

  @Nullable
  private Paragraph paragraph = null;
  private int textCount;

  private FontMgr fontManager = FontMgr.getDefault();

  public TextView(Properties properties) {
    super(properties);
    this.getStyle().color.addListener(color -> {
      if (this.paragraph != null && this.isAdded()) {
        this.paragraph.updateForegroundPaint(0, this.textCount,
            new Paint().setColor(color.valueHex()));
        this.paragraph.layout(this.getContentWidth() * (float) this.window.getGuiScale());
      }
    });
    this.getStyle().fontFamily.addListener(__ -> this.buildParagraph());
    this.getStyle().fontSize.addListener(fontSize -> {
      if (this.paragraph != null && this.isAdded()) {
        this.paragraph.updateFontSize(0, this.textCount, fontSize);
        this.paragraph.layout(this.getContentWidth() * (float) this.window.getGuiScale());
      }
    });
    this.getStyle().textAlign.addListener(textAlign -> {
      if (this.paragraph != null && this.isAdded()) {
        this.paragraph.updateAlignment(textAlign);
        this.paragraph.layout(this.getContentWidth() * (float) this.window.getGuiScale());
      }
    });
    this.getStyle().textShadow.addListener(__ -> this.buildParagraph());
  }

  @Override
  public void styleRefreshed(FontMgr fontManager) {
    if (this.fontManager != FontMgr.getDefault()) {
      this.fontManager.close();
    }
    this.fontManager = fontManager;
    this.buildParagraph();
  }

  @Override
  protected void setLayout(Layout layout) {
    super.setLayout(layout);
    layout.setMeasureFunction(this::measure);
  }

  public TextView setWrap(boolean wrap) {
    this.wrap = wrap;
    if (this.isAdded()) {
      this.getLayout().markDirty();
      this.parent.layout();
    }
    return this;
  }

  public TextView setText(@Nullable String text) {
    return this.setText(Component.nullToEmpty(text));
  }

  public TextView setText(FormattedText text) {
    this.text = text;
    this.buildParagraph();
    if (this.isAdded()) {
      this.getLayout().markDirty();
      this.parent.layout();
    }
    return this;
  }

  @SuppressWarnings("resource")
  private void buildParagraph() {
    if (this.paragraph != null) {
      this.paragraph.close();
      this.paragraph = null;
    }
    this.textCount = 0;

    try (var paragraphStyle = new ParagraphStyle()
        .setAlignment(this.getStyle().textAlign.get())
        .setEllipsis("...")
        .setMaxLinesCount(this.wrap ? Integer.MAX_VALUE : 1);
        var fontCollection = new FontCollection()
            .setDefaultFontManager(this.fontManager)
            .setDynamicFontManager(FontMgr.getDefault());
        var builder = new ParagraphBuilder(paragraphStyle, fontCollection)) {
      this.text.visit((style, content) -> {
        try (var textStyle = new TextStyle()
            .setFontSize(this.getStyle().fontSize.get() * (float) this.window.getGuiScale())
            .setFontFamilies(this.getStyle().fontFamily.get())
            .addShadows(this.getStyle().textShadow.get())
            .setColor(style.getColor() == null
                ? this.getStyle().color.get().valueHex()
                : style.getColor().getValue() + (255 << 24))
            .setFontStyle(style.isBold() && style.isItalic()
                ? FontStyle.BOLD_ITALIC
                : style.isBold()
                    ? FontStyle.BOLD
                    : style.isItalic()
                        ? FontStyle.ITALIC
                        : FontStyle.NORMAL)
            .setDecorationStyle(DecorationStyle.NONE
                .withUnderline(style.isUnderlined())
                .withLineThrough(style.isStrikethrough()))) {
          builder.pushStyle(textStyle);
          builder.addText(content);
          this.textCount += content.length();
          builder.popStyle();
        }
        return Optional.empty();
      }, Style.EMPTY);
      this.paragraph = builder.build();
    }
  }

  @Override
  public void layout() {
    if (this.paragraph != null) {
      this.paragraph.updateFontSize(0, this.textCount,
          this.getStyle().fontSize.get() * (float) this.window.getGuiScale());
      this.paragraph.layout(this.getContentWidth() * (float) this.window.getGuiScale());
    }
    super.layout();
  }

  private Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height) {
    this.paragraph.updateFontSize(0, this.textCount,
        this.getStyle().fontSize.get() * (float) this.window.getGuiScale());
    switch (widthMode) {
      case UNDEFINED:
      case AT_MOST:
        this.paragraph.layout(widthMode == MeasureMode.UNDEFINED ? Float.MAX_VALUE
            : width * (float) this.window.getGuiScale());
        width = (this.paragraph.getMaxIntrinsicWidth()) / (float) this.window.getGuiScale();
        break;
      default:
        this.paragraph.layout(width * (float) this.window.getGuiScale());
        break;
    }
    return new Vec2(width, this.paragraph.getHeight() / (float) this.window.getGuiScale());
  }

  @Override
  public float computeFullHeight() {
    return this.paragraph == null
        ? super.computeFullHeight()
        : this.paragraph.getHeight() / (float) this.window.getGuiScale();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @SuppressWarnings("resource")
  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(poseStack, mouseX, mouseY, partialTicks);
    if (this.paragraph != null) {
      this.skia.begin();
      {
        var canvas = this.skia.canvas();

        canvas.translate(
            this.getScaledContentX() * (float) this.window.getGuiScale(),
            this.getScaledContentY() * (float) this.window.getGuiScale());

        canvas.scale(this.getXScale(), this.getYScale());

        try (var recorder = new PictureRecorder()) {
          var recordingCanvas = recorder.beginRecording(
              Rect.makeWH(this.paragraph.getMaxWidth(), this.paragraph.getHeight()));
          this.paragraph.paint(recordingCanvas, 0, 0);
          var picture = recorder.finishRecordingAsPicture();
          try (var paint = new Paint().setAlphaf(this.getAlpha())) {
            canvas.drawPicture(picture, null, paint);
          }
        }

        canvas.resetMatrix();
      }
      this.skia.end();
    }
  }

  @Override
  public void close() {
    super.close();
    if (this.paragraph != null) {
      this.paragraph.close();
      this.paragraph = null;
    }
    if (this.fontManager != FontMgr.getDefault()) {
      this.fontManager.close();
    }
  }
}
