package sm0keysa1m0n.bliss.view;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import io.github.humbleui.skija.paragraph.Alignment;
import io.github.humbleui.skija.paragraph.Shadow;
import sm0keysa1m0n.bliss.BoxShadow;
import sm0keysa1m0n.bliss.BoxSizing;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.Display;
import sm0keysa1m0n.bliss.Filter;
import sm0keysa1m0n.bliss.ImageRendering;
import sm0keysa1m0n.bliss.Length;
import sm0keysa1m0n.bliss.ObjectFit;
import sm0keysa1m0n.bliss.Overflow;
import sm0keysa1m0n.bliss.PointerEvents;
import sm0keysa1m0n.bliss.Visibility;
import sm0keysa1m0n.bliss.layout.Align;
import sm0keysa1m0n.bliss.layout.FlexDirection;
import sm0keysa1m0n.bliss.layout.Justify;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.layout.LayoutParent;
import sm0keysa1m0n.bliss.layout.PositionType;
import sm0keysa1m0n.bliss.layout.Wrap;
import sm0keysa1m0n.bliss.property.StyleableProperty;
import sm0keysa1m0n.bliss.style.Percentage;
import sm0keysa1m0n.bliss.style.ShorthandArgMapper;
import sm0keysa1m0n.bliss.style.ShorthandDispatcher;
import sm0keysa1m0n.bliss.style.StyleManager;
import sm0keysa1m0n.bliss.style.selector.StyleNodeState;

public class ViewStyle {

  public final StyleableProperty<Display> display;
  public final StyleableProperty<FlexDirection> flexDirection;
  public final StyleableProperty<Wrap> flexWrap;
  public final StyleableProperty<Align> alignItems;
  public final StyleableProperty<Align> alignContent;
  public final StyleableProperty<Justify> justifyContent;
  public final StyleableProperty<Align> alignSelf;
  public final StyleableProperty<Float> flexGrow;
  public final StyleableProperty<Float> flexShrink;
  public final StyleableProperty<Length> flexBasis;
  public final StyleableProperty<Float> flex;
  public final StyleableProperty<Length> borderTopWidth;
  public final StyleableProperty<Length> borderRightWidth;
  public final StyleableProperty<Length> borderBottomWidth;
  public final StyleableProperty<Length> borderLeftWidth;
  public final StyleableProperty<Length> top;
  public final StyleableProperty<Length> right;
  public final StyleableProperty<Length> bottom;
  public final StyleableProperty<Length> left;
  public final StyleableProperty<Length> paddingTop;
  public final StyleableProperty<Length> paddingRight;
  public final StyleableProperty<Length> paddingBottom;
  public final StyleableProperty<Length> paddingLeft;
  public final StyleableProperty<Length> marginTop;
  public final StyleableProperty<Length> marginRight;
  public final StyleableProperty<Length> marginBottom;
  public final StyleableProperty<Length> marginLeft;
  public final StyleableProperty<PositionType> position;
  public final StyleableProperty<Float> aspectRatio;
  public final StyleableProperty<Length> width;
  public final StyleableProperty<Length> height;
  public final StyleableProperty<Length> minWidth;
  public final StyleableProperty<Length> minHeight;
  public final StyleableProperty<Length> maxWidth;
  public final StyleableProperty<Length> maxHeight;
  public final StyleableProperty<Overflow> overflow;
  public final StyleableProperty<Integer> zIndex;
  public final StyleableProperty<Float> xScale;
  public final StyleableProperty<Float> yScale;
  public final StyleableProperty<Float> xTranslation;
  public final StyleableProperty<Float> yTranslation;
  public final StyleableProperty<Percentage> opacity;
  public final StyleableProperty<Color> backgroundColor;
  public final StyleableProperty<Length> outlineWidth;
  public final StyleableProperty<Color> outlineColor;
  public final StyleableProperty<Float> borderTopLeftRadius;
  public final StyleableProperty<Float> borderTopRightRadius;
  public final StyleableProperty<Float> borderBottomRightRadius;
  public final StyleableProperty<Float> borderBottomLeftRadius;
  public final StyleableProperty<Color> borderLeftColor;
  public final StyleableProperty<Color> borderRightColor;
  public final StyleableProperty<Color> borderTopColor;
  public final StyleableProperty<Color> borderBottomColor;
  public final StyleableProperty<Color> color;
  public final StyleableProperty<ObjectFit> objectFit;
  public final StyleableProperty<Integer> fontSize;
  public final StyleableProperty<String[]> fontFamily;
  public final StyleableProperty<Alignment> textAlign;
  public final StyleableProperty<Shadow[]> textShadow;
  public final StyleableProperty<Filter[]> backdropFilter;
  public final StyleableProperty<Visibility> visibility;
  public final StyleableProperty<BoxSizing> boxSizing;
  public final StyleableProperty<PointerEvents> pointerEvents;
  public final StyleableProperty<BoxShadow[]> boxShadow;
  public final StyleableProperty<ImageRendering> imageRendering;

  private final View view;

  private final StyleManager styleManager;

  public ViewStyle(View view) {
    this.view = view;
    this.styleManager = new StyleManager(view);

    this.registerProperty(this.display =
        new StyleableProperty<>(view, "display", Display.class, Display.FLEX));
    this.registerProperty(this.flexDirection =
        new StyleableProperty<>(view, "flex-direction", FlexDirection.class,
            FlexDirection.COLUMN, this.forLayoutParent(LayoutParent::setFlexDirection)));
    this.registerProperty(this.flexWrap =
        new StyleableProperty<>(view, "flex-wrap", Wrap.class,
            Wrap.NO_WRAP, this.forLayoutParent(LayoutParent::setFlexWrap)));
    this.registerProperty(this.alignItems =
        new StyleableProperty<>(view, "align-items", Align.class,
            Align.STRETCH, this.forLayoutParent(LayoutParent::setAlignItems)));
    this.registerProperty(this.alignContent =
        new StyleableProperty<>(view, "align-content", Align.class,
            Align.FLEX_START, this.forLayoutParent(LayoutParent::setAlignContent)));
    this.registerProperty(this.justifyContent =
        new StyleableProperty<>(view, "justify-content", Justify.class,
            Justify.FLEX_START, this.forLayoutParent(LayoutParent::setJustifyContent)));
    this.registerProperty(this.alignSelf =
        new StyleableProperty<>(view, "align-self", Align.class, Align.AUTO,
            this.forLayout(Layout::setAlignSelf)));
    this.registerProperty(this.flexGrow =
        new StyleableProperty<>(view, "flex-grow", Float.class,
            0.0F, this.forLayout(Layout::setFlexGrow)));
    this.registerProperty(this.flexShrink =
        new StyleableProperty<>(view, "flex-shrink", Float.class,
            1.0F, this.forLayout(Layout::setFlexShrink)));
    this.registerProperty(this.flexBasis =
        new StyleableProperty<>(view, "flex-basis", Length.class, Length.AUTO,
            value -> value.dispatch(
                view.getLayout()::setFlexBasis,
                view.getLayout()::setFlexBasisPercent,
                view.getLayout()::setFlexBasisAuto)));
    this.registerProperty(this.flex =
        new StyleableProperty<>(view, "flex", Float.class, Float.NaN,
            this.forLayout(Layout::setFlex)));

    this.registerProperty(this.borderTopWidth =
        new StyleableProperty<>(view, "border-top-width", Length.class, Length.ZERO,
            this.forLayout((layout, length) -> layout.setTopBorderWidth(length.fixed()))));
    this.registerProperty(this.borderRightWidth =
        new StyleableProperty<>(view, "border-right-width", Length.class, Length.ZERO,
            this.forLayout((layout, length) -> layout.setRightBorderWidth(length.fixed()))));
    this.registerProperty(this.borderBottomWidth =
        new StyleableProperty<>(view, "border-bottom-width", Length.class, Length.ZERO,
            this.forLayout((layout, length) -> layout.setBottomBorderWidth(length.fixed()))));
    this.registerProperty(this.borderLeftWidth =
        new StyleableProperty<>(view, "border-left-width", Length.class, Length.ZERO,
            this.forLayout((layout, length) -> layout.setLeftBorderWidth(length.fixed()))));
    this.styleManager.registerProperty(ShorthandDispatcher.create("border-width", Length.class,
        ShorthandArgMapper.BOX_MAPPER, this.borderTopWidth, this.borderRightWidth,
        this.borderBottomWidth, this.borderLeftWidth));

    this.registerProperty(this.top =
        new StyleableProperty<>(view, "top", Length.class, Length.AUTO));
    this.registerProperty(this.right =
        new StyleableProperty<>(view, "right", Length.class, Length.AUTO));
    this.registerProperty(this.bottom =
        new StyleableProperty<>(view, "bottom", Length.class, Length.AUTO));
    this.registerProperty(this.left =
        new StyleableProperty<>(view, "left", Length.class, Length.AUTO));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("inset", Length.class, ShorthandArgMapper.BOX_MAPPER,
            this.top, this.right, this.bottom, this.left));

    this.registerProperty(this.paddingTop =
        new StyleableProperty<>(view, "padding-top", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setTopPadding,
                view.getLayout()::setTopPaddingPercent)));
    this.registerProperty(this.paddingRight =
        new StyleableProperty<>(view, "padding-right", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setRightPadding,
                view.getLayout()::setRightPaddingPercent)));
    this.registerProperty(this.paddingBottom =
        new StyleableProperty<>(view, "padding-bottom", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setBottomPadding,
                view.getLayout()::setBottomPaddingPercent)));
    this.registerProperty(this.paddingLeft =
        new StyleableProperty<>(view, "padding-left", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setLeftPadding,
                view.getLayout()::setLeftPaddingPercent)));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("padding", Length.class, ShorthandArgMapper.BOX_MAPPER,
            this.paddingTop, this.paddingRight, this.paddingBottom, this.paddingLeft));

    this.registerProperty(this.marginTop =
        new StyleableProperty<>(view, "margin-top", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setTopMargin,
                view.getLayout()::setTopMarginPercent,
                view.getLayout()::setTopMarginAuto)));
    this.registerProperty(this.marginRight =
        new StyleableProperty<>(view, "margin-right", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setRightMargin,
                view.getLayout()::setRightMarginPercent,
                view.getLayout()::setRightMarginAuto)));
    this.registerProperty(this.marginBottom =
        new StyleableProperty<>(view, "margin-bottom", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setBottomMargin,
                view.getLayout()::setBottomMarginPercent,
                view.getLayout()::setBottomMarginAuto)));
    this.registerProperty(this.marginLeft =
        new StyleableProperty<>(view, "margin-left", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setLeftMargin,
                view.getLayout()::setLeftMarginPercent,
                view.getLayout()::setLeftMarginAuto)));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("margin", Length.class, ShorthandArgMapper.BOX_MAPPER,
            this.marginTop, this.marginRight, this.marginBottom, this.marginLeft));

    this.registerProperty(this.position =
        new StyleableProperty<>(view, "position", PositionType.class,
            PositionType.RELATIVE, this.forLayout(Layout::setPositionType)));

    this.registerProperty(this.aspectRatio =
        new StyleableProperty<>(view, "aspect-ratio", Float.class, Float.NaN,
            this.forLayout(Layout::setAspectRatio)));
    this.registerProperty(this.width =
        new StyleableProperty<>(view, "width", Length.class, Length.AUTO,
            value -> value.dispatch(
                view.getLayout()::setWidth,
                view.getLayout()::setWidthPercent,
                view.getLayout()::setWidthAuto)));
    this.registerProperty(this.height =
        new StyleableProperty<>(view, "height", Length.class, Length.AUTO,
            value -> value.dispatch(
                view.getLayout()::setHeight,
                view.getLayout()::setHeightPercent,
                view.getLayout()::setHeightAuto)));
    this.registerProperty(this.minWidth =
        new StyleableProperty<>(view, "min-width", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMinWidth,
                view.getLayout()::setMinWidthPercent)));
    this.registerProperty(this.minHeight =
        new StyleableProperty<>(view, "min-height", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMinHeight,
                view.getLayout()::setMinHeightPercent)));
    this.registerProperty(this.maxWidth =
        new StyleableProperty<>(view, "max-width", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMaxWidth,
                view.getLayout()::setMaxWidthPercent)));
    this.registerProperty(this.maxHeight =
        new StyleableProperty<>(view, "max-height", Length.class, Length.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMaxHeight,
                view.getLayout()::setMaxHeightPercent)));
    this.registerProperty(this.overflow = new StyleableProperty<>(view, "overflow", Overflow.class,
        Overflow.VISIBLE, this.forLayout(Layout::setOverflow)));

    this.registerProperty(
        this.zIndex = new StyleableProperty<>(view, "z-index", Integer.class, 1, value -> {
          if (view.getParent() != null) {
            view.getParent().sortChildren();
          }
        }));
    this.registerProperty(this.xScale =
        new StyleableProperty<>(view, "-bliss-x-scale", Float.class, 1.0F));
    this.registerProperty(this.yScale =
        new StyleableProperty<>(view, "-bliss-y-scale", Float.class, 1.0F));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("scale", Float.class, ShorthandArgMapper.TWO,
            this.xScale, this.yScale));

    this.registerProperty(this.xTranslation =
        new StyleableProperty<>(view, "-bliss-x-translation", Float.class, 0.0F));
    this.registerProperty(this.yTranslation =
        new StyleableProperty<>(view, "-bliss-y-translation", Float.class, 0.0F));
    this.registerProperty(this.opacity =
        new StyleableProperty<>(view, "opacity", Percentage.class, Percentage.ONE_HUNDRED));
    this.registerProperty(this.backgroundColor =
        new StyleableProperty<>(view, "background-color", Color.class, Color.TRANSPARENT));
    this.registerProperty(this.outlineWidth =
        new StyleableProperty<>(view, "outline-width", Length.class, Length.ZERO));
    this.registerProperty(this.outlineColor =
        new StyleableProperty<>(view, "outline-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderTopLeftRadius =
        new StyleableProperty<>(view, "border-top-left-radius", Float.class, 0.0F));
    this.registerProperty(this.borderTopRightRadius =
        new StyleableProperty<>(view, "border-top-right-radius", Float.class, 0.0F));
    this.registerProperty(this.borderBottomRightRadius =
        new StyleableProperty<>(view, "border-bottom-right-radius", Float.class, 0.0F));
    this.registerProperty(this.borderBottomLeftRadius =
        new StyleableProperty<>(view, "border-bottom-left-radius", Float.class, 0.0F));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("border-radius", Float.class, ShorthandArgMapper.BOX_MAPPER,
            this.borderTopLeftRadius, this.borderTopRightRadius,
            this.borderBottomRightRadius, this.borderBottomLeftRadius));
    this.registerProperty(this.borderTopColor =
        new StyleableProperty<>(view, "border-top-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderRightColor =
        new StyleableProperty<>(view, "border-right-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderBottomColor =
        new StyleableProperty<>(view, "border-bottom-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderLeftColor =
        new StyleableProperty<>(view, "border-left-color", Color.class, Color.BLACK));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("border-color", Color.class, ShorthandArgMapper.BOX_MAPPER,
            this.borderTopColor, this.borderRightColor,
            this.borderBottomColor, this.borderLeftColor));

    this.registerProperty(
        this.color = new StyleableProperty<>(view, "color", Color.class, Color.WHITE));
    this.registerProperty(
        this.objectFit =
            new StyleableProperty<>(view, "object-fit", ObjectFit.class, ObjectFit.FILL));
    this.registerProperty(
        this.fontSize = new StyleableProperty<>(view, "font-size", Integer.class, 11));
    this.registerProperty(this.fontFamily =
        new StyleableProperty<>(view, "font-family", String[].class, new String[0]));
    this.registerProperty(this.textAlign =
        new StyleableProperty<>(view, "text-align", Alignment.class, Alignment.START));
    this.registerProperty(this.textShadow =
        new StyleableProperty<>(view, "text-shadow", Shadow[].class, new Shadow[0]));
    this.registerProperty(this.backdropFilter =
        new StyleableProperty<>(view, "backdrop-filter", Filter[].class, new Filter[0]));
    this.registerProperty(this.visibility =
        new StyleableProperty<>(view, "visibility", Visibility.class, Visibility.VISIBLE));
    this.registerProperty(this.boxSizing =
        new StyleableProperty<>(view, "box-sizing", BoxSizing.class, BoxSizing.CONTENT_BOX));
    this.registerProperty(this.pointerEvents =
        new StyleableProperty<>(view, "pointer-events", PointerEvents.class, PointerEvents.AUTO));
    this.registerProperty(this.boxShadow =
        new StyleableProperty<>(view, "box-shadow", BoxShadow[].class, new BoxShadow[0]));
    this.registerProperty(this.imageRendering =
        new StyleableProperty<>(view, "image-rendering", ImageRendering.class,
            ImageRendering.AUTO));
  }

  public StyleManager getStyleManager() {
    return this.styleManager;
  }

  private <T> Consumer<T> forLayout(BiConsumer<Layout, T> consumer) {
    return value -> consumer.accept(this.view.getLayout(), value);
  }

  private <T> Consumer<T> forLayoutParent(BiConsumer<LayoutParent, T> consumer) {
    return value -> {
      // Layout parent can be null here because ParentView might not be fully constructed.
      if (this.view instanceof ParentView view && view.getLayoutParent() != null) {
        consumer.accept(view.getLayoutParent(), value);
      }
    };
  }

  private <T> void registerProperty(StyleableProperty<T> property) {
    this.styleManager.registerProperty(property);
  }

  public final void defineBorderColorState(Color color, StyleNodeState... states) {
    this.borderTopColor.defineState(color, states);
    this.borderRightColor.defineState(color, states);
    this.borderBottomColor.defineState(color, states);
    this.borderLeftColor.defineState(color, states);
  }
}
