package com.craftingdead.immerse.client.gui.view;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.craftingdead.immerse.client.gui.view.layout.Align;
import com.craftingdead.immerse.client.gui.view.layout.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.Justify;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.client.gui.view.layout.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.Wrap;
import com.craftingdead.immerse.client.gui.view.property.StyleableProperty;
import com.craftingdead.immerse.client.gui.view.style.Percentage;
import com.craftingdead.immerse.client.gui.view.style.ShorthandArgMapper;
import com.craftingdead.immerse.client.gui.view.style.ShorthandDispatcher;
import com.craftingdead.immerse.client.gui.view.style.StyleManager;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;
import com.craftingdead.immerse.client.util.FitType;
import io.github.humbleui.skija.paragraph.Alignment;
import io.github.humbleui.skija.paragraph.Shadow;

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
  public final StyleableProperty<Point> flexBasis;
  public final StyleableProperty<Float> flex;
  public final StyleableProperty<Float> borderTopWidth;
  public final StyleableProperty<Float> borderRightWidth;
  public final StyleableProperty<Float> borderBottomWidth;
  public final StyleableProperty<Float> borderLeftWidth;
  public final StyleableProperty<Point> top;
  public final StyleableProperty<Point> right;
  public final StyleableProperty<Point> bottom;
  public final StyleableProperty<Point> left;
  public final StyleableProperty<Point> paddingTop;
  public final StyleableProperty<Point> paddingRight;
  public final StyleableProperty<Point> paddingBottom;
  public final StyleableProperty<Point> paddingLeft;
  public final StyleableProperty<Point> marginTop;
  public final StyleableProperty<Point> marginRight;
  public final StyleableProperty<Point> marginBottom;
  public final StyleableProperty<Point> marginLeft;
  public final StyleableProperty<PositionType> position;
  public final StyleableProperty<Float> aspectRatio;
  public final StyleableProperty<Point> width;
  public final StyleableProperty<Point> height;
  public final StyleableProperty<Point> minWidth;
  public final StyleableProperty<Point> minHeight;
  public final StyleableProperty<Overflow> overflow;
  public final StyleableProperty<Integer> zIndex;
  public final StyleableProperty<Float> xScale;
  public final StyleableProperty<Float> yScale;
  public final StyleableProperty<Float> xTranslation;
  public final StyleableProperty<Float> yTranslation;
  public final StyleableProperty<Percentage> opacity;
  public final StyleableProperty<Color> backgroundColor;
  public final StyleableProperty<Float> outlineWidth;
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
  public final StyleableProperty<FitType> objectFit;
  public final StyleableProperty<Integer> fontSize;
  public final StyleableProperty<String[]> fontFamily;
  public final StyleableProperty<Alignment> textAlign;
  public final StyleableProperty<Shadow[]> textShadow;
  public final StyleableProperty<Filter[]> backdropFilter;

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
        new StyleableProperty<>(view, "flex-basis", Point.class, Point.AUTO,
            value -> value.dispatch(
                view.getLayout()::setFlexBasis,
                view.getLayout()::setFlexBasisPercent,
                view.getLayout()::setFlexBasisAuto)));
    this.registerProperty(this.flex =
        new StyleableProperty<>(view, "flex", Float.class, Float.NaN,
            this.forLayout(Layout::setFlex)));

    this.registerProperty(this.borderTopWidth =
        new StyleableProperty<>(view, "border-top-width", Float.class, 0.0F,
            this.forLayout(Layout::setTopBorderWidth)));
    this.registerProperty(this.borderRightWidth =
        new StyleableProperty<>(view, "border-right-width", Float.class, 0.0F,
            this.forLayout(Layout::setRightBorderWidth)));
    this.registerProperty(this.borderBottomWidth =
        new StyleableProperty<>(view, "border-bottom-width", Float.class, 0.0F,
            this.forLayout(Layout::setBottomBorderWidth)));
    this.registerProperty(this.borderLeftWidth =
        new StyleableProperty<>(view, "border-left-width", Float.class, 0.0F,
            this.forLayout(Layout::setLeftBorderWidth)));
    this.styleManager.registerProperty(ShorthandDispatcher.create("border-width", Float.class,
        ShorthandArgMapper.BOX_MAPPER, this.borderTopWidth, this.borderRightWidth,
        this.borderBottomWidth, this.borderLeftWidth));

    this.registerProperty(this.top =
        new StyleableProperty<>(view, "top", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setTop,
                view.getLayout()::setTopPercent)));
    this.registerProperty(this.right =
        new StyleableProperty<>(view, "right", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setRight,
                view.getLayout()::setRightPercent)));
    this.registerProperty(this.bottom =
        new StyleableProperty<>(view, "bottom", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setBottom,
                view.getLayout()::setBottomPercent)));
    this.registerProperty(this.left =
        new StyleableProperty<>(view, "left", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setLeft,
                view.getLayout()::setLeftPercent)));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("inset", Point.class, ShorthandArgMapper.BOX_MAPPER,
            this.top, this.right, this.bottom, this.left));

    this.registerProperty(this.paddingTop =
        new StyleableProperty<>(view, "padding-top", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setTopPadding,
                view.getLayout()::setTopPaddingPercent)));
    this.registerProperty(this.paddingRight =
        new StyleableProperty<>(view, "padding-right", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setRightPadding,
                view.getLayout()::setRightPaddingPercent)));
    this.registerProperty(this.paddingBottom =
        new StyleableProperty<>(view, "padding-bottom", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setBottomPadding,
                view.getLayout()::setBottomPaddingPercent)));
    this.registerProperty(this.paddingLeft =
        new StyleableProperty<>(view, "padding-left", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setLeftPadding,
                view.getLayout()::setLeftPaddingPercent)));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("padding", Point.class, ShorthandArgMapper.BOX_MAPPER,
            this.paddingTop, this.paddingRight, this.paddingBottom, this.paddingLeft));

    this.registerProperty(this.marginTop =
        new StyleableProperty<>(view, "margin-top", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setTopMargin,
                view.getLayout()::setTopMarginPercent,
                view.getLayout()::setTopMarginAuto)));
    this.registerProperty(this.marginRight =
        new StyleableProperty<>(view, "margin-right", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setRightMargin,
                view.getLayout()::setRightMarginPercent,
                view.getLayout()::setRightMarginAuto)));
    this.registerProperty(this.marginBottom =
        new StyleableProperty<>(view, "margin-bottom", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setBottomMargin,
                view.getLayout()::setBottomMarginPercent,
                view.getLayout()::setBottomMarginAuto)));
    this.registerProperty(this.marginLeft =
        new StyleableProperty<>(view, "margin-left", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setLeftMargin,
                view.getLayout()::setLeftMarginPercent,
                view.getLayout()::setLeftMarginAuto)));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("margin", Point.class, ShorthandArgMapper.BOX_MAPPER,
            this.marginTop, this.marginRight, this.marginBottom, this.marginLeft));

    this.registerProperty(this.position =
        new StyleableProperty<>(view, "position", PositionType.class,
            PositionType.RELATIVE, this.forLayout(Layout::setPositionType)));

    this.registerProperty(this.aspectRatio =
        new StyleableProperty<>(view, "aspect-ratio", Float.class, Float.NaN,
            this.forLayout(Layout::setAspectRatio)));
    this.registerProperty(this.width =
        new StyleableProperty<>(view, "width", Point.class, Point.AUTO,
            value -> value.dispatch(
                view.getLayout()::setWidth,
                view.getLayout()::setWidthPercent,
                view.getLayout()::setWidthAuto)));
    this.registerProperty(this.height =
        new StyleableProperty<>(view, "height", Point.class, Point.AUTO,
            value -> value.dispatch(
                view.getLayout()::setHeight,
                view.getLayout()::setHeightPercent,
                view.getLayout()::setHeightAuto)));
    this.registerProperty(this.minWidth =
        new StyleableProperty<>(view, "min-width", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMinWidth,
                view.getLayout()::setMinWidthPercent)));
    this.registerProperty(this.minHeight =
        new StyleableProperty<>(view, "min-height", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMinHeight,
                view.getLayout()::setMinHeightPercent)));
    this.registerProperty(this.overflow = new StyleableProperty<>(view, "overflow", Overflow.class,
        Overflow.VISIBLE, this.forLayout(Layout::setOverflow)));

    this.registerProperty(
        this.zIndex = new StyleableProperty<>(view, "z-index", Integer.class, 1, value -> {
          if (view.getParent() != null) {
            view.getParent().sortChildren();
          }
        }));
    this.registerProperty(this.xScale =
        new StyleableProperty<>(view, "x-scale", Float.class, 1.0F));
    this.registerProperty(this.yScale =
        new StyleableProperty<>(view, "y-scale", Float.class, 1.0F));
    this.styleManager.registerProperty(
        ShorthandDispatcher.create("scale", Float.class, ShorthandArgMapper.TWO,
            this.xScale, this.yScale));

    this.registerProperty(this.xTranslation =
        new StyleableProperty<>(view, "x-translation", Float.class, 0.0F));
    this.registerProperty(this.yTranslation =
        new StyleableProperty<>(view, "y-translation", Float.class, 0.0F));
    this.registerProperty(this.opacity =
        new StyleableProperty<>(view, "alpha", Percentage.class, Percentage.ONE_HUNDRED));
    this.registerProperty(this.backgroundColor =
        new StyleableProperty<>(view, "background-color", Color.class, Color.TRANSPARENT));
    this.registerProperty(this.outlineWidth =
        new StyleableProperty<>(view, "outline-width", Float.class, 0.0F));
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
        this.objectFit = new StyleableProperty<>(view, "object-fit", FitType.class, FitType.FILL));
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
  }

  public StyleManager getStyleHolder() {
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

  public void setStyle(String style) {
    this.styleManager.parseInlineCSS(style);
  }
}
