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
import net.minecraft.util.Mth;
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
        this.paragraph.layout(this.getContentWidth() * this.graphicsContext.scale());
      }
    });
    this.getStyle().fontFamily.addListener(__ -> this.buildParagraph());
    this.getStyle().fontSize.addListener(fontSize -> {
      if (this.paragraph != null && this.isAdded()) {
        this.paragraph.updateFontSize(0, this.textCount, fontSize);
        this.paragraph.layout(this.getContentWidth() * this.graphicsContext.scale());
      }
    });
    this.getStyle().textAlign.addListener(textAlign -> {
      if (this.paragraph != null && this.isAdded()) {
        this.paragraph.updateAlignment(textAlign);
        this.paragraph.layout(this.getContentWidth() * this.graphicsContext.scale());
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
        var color = style.getColor() == null
            ? this.getStyle().color.get().valueHex()
            : style.getColor().getValue() + (255 << 24);
        try (var textStyle = new TextStyle()
            .setFontSize(this.getStyle().fontSize.get() * this.graphicsContext.scale())
            .setFontFamilies(this.getStyle().fontFamily.get())
            .addShadows(this.getStyle().textShadow.get())
            .setColor(color)
            .setFontStyle(style.isBold() && style.isItalic()
                ? FontStyle.BOLD_ITALIC
                : style.isBold()
                    ? FontStyle.BOLD
                    : style.isItalic()
                        ? FontStyle.ITALIC
                        : FontStyle.NORMAL)
            .setDecorationStyle(DecorationStyle.NONE
                .withUnderline(style.isUnderlined())
                .withLineThrough(style.isStrikethrough())
                .withColor(color))) {
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
          this.getStyle().fontSize.get() * this.graphicsContext.scale());
      this.paragraph.layout(this.getContentWidth() * this.graphicsContext.scale());
    }
    super.layout();
  }

  private Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height) {
    this.paragraph.updateFontSize(0, this.textCount,
        this.getStyle().fontSize.get() * this.graphicsContext.scale());
    switch (widthMode) {
      case UNDEFINED:
      case AT_MOST:
        this.paragraph.layout(widthMode == MeasureMode.UNDEFINED
            ? Float.MAX_VALUE
            : Mth.ceil(width) * this.graphicsContext.scale());

        width = this.paragraph.getMaxIntrinsicWidth() / this.graphicsContext.scale();
        break;
      default:
        this.paragraph.layout(Mth.ceil(width) * this.graphicsContext.scale());
        break;
    }
    return new Vec2(width, this.paragraph.getHeight() / this.graphicsContext.scale());
  }

  @Override
  public float computeFullHeight() {
    return this.paragraph == null
        ? super.computeFullHeight()
        : this.paragraph.getHeight() / this.graphicsContext.scale();
  }

  @SuppressWarnings("resource")
  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(poseStack, mouseX, mouseY, partialTicks);
    if (this.paragraph != null) {
      var canvas = this.graphicsContext.canvas();

      canvas.translate(
          this.getScaledContentX() * this.graphicsContext.scale(),
          this.getScaledContentY() * this.graphicsContext.scale());

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
