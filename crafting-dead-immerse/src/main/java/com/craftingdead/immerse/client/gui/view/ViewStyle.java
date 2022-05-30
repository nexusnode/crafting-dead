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
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;
import com.craftingdead.immerse.client.gui.view.style.shorthand.ShorthandArgMapper;
import com.craftingdead.immerse.client.gui.view.style.shorthand.ShorthandDispatcher;
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

  private final StyleHolder styleHolder;

  public ViewStyle(View view) {
    this.view = view;
    this.styleHolder = new StyleHolder(view);

    this.registerProperty(this.display =
        new StyleableProperty<>("display", Display.class, Display.FLEX));
    this.registerProperty(this.flexDirection =
        new StyleableProperty<>("flex-direction", FlexDirection.class,
            FlexDirection.COLUMN, this.forLayoutParent(LayoutParent::setFlexDirection)));
    this.registerProperty(this.flexWrap =
        new StyleableProperty<>("flex-wrap", Wrap.class,
            Wrap.NO_WRAP, this.forLayoutParent(LayoutParent::setFlexWrap)));
    this.registerProperty(this.alignItems =
        new StyleableProperty<>("align-items", Align.class,
            Align.STRETCH, this.forLayoutParent(LayoutParent::setAlignItems)));
    this.registerProperty(this.alignContent =
        new StyleableProperty<>("align-content", Align.class,
            Align.FLEX_START, this.forLayoutParent(LayoutParent::setAlignContent)));
    this.registerProperty(this.justifyContent =
        new StyleableProperty<>("justify-content", Justify.class,
            Justify.FLEX_START, this.forLayoutParent(LayoutParent::setJustifyContent)));
    this.registerProperty(this.alignSelf =
        new StyleableProperty<>("align-self", Align.class, Align.AUTO,
            this.forLayout(Layout::setAlignSelf)));
    this.registerProperty(this.flexGrow =
        new StyleableProperty<>("flex-grow", Float.class,
            0.0F, this.forLayout(Layout::setFlexGrow)));
    this.registerProperty(this.flexShrink =
        new StyleableProperty<>("flex-shrink", Float.class,
            1.0F, this.forLayout(Layout::setFlexShrink)));
    this.registerProperty(this.flexBasis =
        new StyleableProperty<>("flex-basis", Point.class, Point.AUTO, value -> value.dispatch(
            view.getLayout()::setFlexBasis,
            view.getLayout()::setFlexBasisPercent,
            view.getLayout()::setFlexBasisAuto)));
    this.registerProperty(this.flex =
        new StyleableProperty<>("flex", Float.class, Float.NaN, this.forLayout(Layout::setFlex)));

    this.registerProperty(this.borderTopWidth =
        new StyleableProperty<>("border-top-width", Float.class, 0.0F,
            this.forLayout(Layout::setTopBorderWidth)));
    this.registerProperty(this.borderRightWidth =
        new StyleableProperty<>("border-right-width", Float.class, 0.0F,
            this.forLayout(Layout::setRightBorderWidth)));
    this.registerProperty(this.borderBottomWidth =
        new StyleableProperty<>("border-bottom-width", Float.class, 0.0F,
            this.forLayout(Layout::setBottomBorderWidth)));
    this.registerProperty(this.borderLeftWidth =
        new StyleableProperty<>("border-left-width", Float.class, 0.0F,
            this.forLayout(Layout::setLeftBorderWidth)));
    this.styleHolder.registerDispatcher(ShorthandDispatcher.create("border-width", Float.class,
        ShorthandArgMapper.BOX_MAPPER, this.borderTopWidth, this.borderRightWidth,
        this.borderBottomWidth, this.borderLeftWidth));

    this.registerProperty(this.top =
        new StyleableProperty<>("top", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setTop,
                view.getLayout()::setTopPercent)));
    this.registerProperty(this.right =
        new StyleableProperty<>("right", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setRight,
                view.getLayout()::setRightPercent)));
    this.registerProperty(this.bottom =
        new StyleableProperty<>("bottom", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setBottom,
                view.getLayout()::setBottomPercent)));
    this.registerProperty(this.left =
        new StyleableProperty<>("left", Point.class, Point.UNDEFINED,
            value -> value.dispatch(
                view.getLayout()::setLeft,
                view.getLayout()::setLeftPercent)));
    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("inset", Point.class, ShorthandArgMapper.BOX_MAPPER,
            this.top, this.right, this.bottom, this.left));

    this.registerProperty(this.paddingTop =
        new StyleableProperty<>("padding-top", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setTopPadding,
                view.getLayout()::setTopPaddingPercent)));
    this.registerProperty(this.paddingRight =
        new StyleableProperty<>("padding-right", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setRightPadding,
                view.getLayout()::setRightPaddingPercent)));
    this.registerProperty(this.paddingBottom =
        new StyleableProperty<>("padding-bottom", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setBottomPadding,
                view.getLayout()::setBottomPaddingPercent)));
    this.registerProperty(this.paddingLeft =
        new StyleableProperty<>("padding-left", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setLeftPadding,
                view.getLayout()::setLeftPaddingPercent)));
    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("padding", Point.class, ShorthandArgMapper.BOX_MAPPER,
            this.paddingTop, this.paddingRight, this.paddingBottom, this.paddingLeft));

    this.registerProperty(this.marginTop =
        new StyleableProperty<>("margin-top", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setTopMargin,
                view.getLayout()::setTopMarginPercent,
                view.getLayout()::setTopMarginAuto)));
    this.registerProperty(this.marginRight =
        new StyleableProperty<>("margin-right", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setRightMargin,
                view.getLayout()::setRightMarginPercent,
                view.getLayout()::setRightMarginAuto)));
    this.registerProperty(this.marginBottom =
        new StyleableProperty<>("margin-bottom", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setBottomMargin,
                view.getLayout()::setBottomMarginPercent,
                view.getLayout()::setBottomMarginAuto)));
    this.registerProperty(this.marginLeft =
        new StyleableProperty<>("margin-left", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setLeftMargin,
                view.getLayout()::setLeftMarginPercent,
                view.getLayout()::setLeftMarginAuto)));
    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("margin", Point.class, ShorthandArgMapper.BOX_MAPPER,
            this.marginTop, this.marginRight, this.marginBottom, this.marginLeft));

    this.registerProperty(this.position =
        new StyleableProperty<>("position", PositionType.class,
            PositionType.RELATIVE, this.forLayout(Layout::setPositionType)));

    this.registerProperty(this.aspectRatio =
        new StyleableProperty<>("aspect-ratio", Float.class, Float.NaN,
            this.forLayout(Layout::setAspectRatio)));
    this.registerProperty(this.width =
        new StyleableProperty<>("width", Point.class, Point.AUTO,
            value -> value.dispatch(
                view.getLayout()::setWidth,
                view.getLayout()::setWidthPercent,
                view.getLayout()::setWidthAuto)));
    this.registerProperty(this.height =
        new StyleableProperty<>("height", Point.class, Point.AUTO,
            value -> value.dispatch(
                view.getLayout()::setHeight,
                view.getLayout()::setHeightPercent,
                view.getLayout()::setHeightAuto)));
    this.registerProperty(this.minWidth =
        new StyleableProperty<>("min-width", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMinWidth,
                view.getLayout()::setMinWidthPercent)));
    this.registerProperty(this.minHeight =
        new StyleableProperty<>("min-height", Point.class, Point.ZERO,
            value -> value.dispatch(
                view.getLayout()::setMinHeight,
                view.getLayout()::setMinHeightPercent)));
    this.registerProperty(this.overflow = new StyleableProperty<>("overflow", Overflow.class,
        Overflow.VISIBLE, this.forLayout(Layout::setOverflow)));

    this.registerProperty(
        this.zIndex = new StyleableProperty<>("z-index", Integer.class, 1, value -> {
          if (view.getParent() != null) {
            view.getParent().sortChildren();
          }
        }));
    this.registerProperty(this.xScale =
        new StyleableProperty<>("x-scale", Float.class, 1.0F));
    this.registerProperty(this.yScale =
        new StyleableProperty<>("y-scale", Float.class, 1.0F));
    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("scale", Float.class, ShorthandArgMapper.TWO,
            this.xScale, this.yScale));

    this.registerProperty(this.xTranslation =
        new StyleableProperty<>("x-translation", Float.class, 0.0F));
    this.registerProperty(this.yTranslation =
        new StyleableProperty<>("y-translation", Float.class, 0.0F));
    this.registerProperty(this.opacity =
        new StyleableProperty<>("alpha", Percentage.class, Percentage.ONE_HUNDRED));
    this.registerProperty(this.backgroundColor =
        new StyleableProperty<>("background-color", Color.class, Color.TRANSPARENT));
    this.registerProperty(this.outlineWidth =
        new StyleableProperty<>("outline-width", Float.class, 0.0F));
    this.registerProperty(this.outlineColor =
        new StyleableProperty<>("outline-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderTopLeftRadius =
        new StyleableProperty<>("border-top-left-radius", Float.class, 0.0F));
    this.registerProperty(this.borderTopRightRadius =
        new StyleableProperty<>("border-top-right-radius", Float.class, 0.0F));
    this.registerProperty(this.borderBottomRightRadius =
        new StyleableProperty<>("border-bottom-right-radius", Float.class, 0.0F));
    this.registerProperty(this.borderBottomLeftRadius =
        new StyleableProperty<>("border-bottom-left-radius", Float.class, 0.0F));
    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("border-radius", Float.class, ShorthandArgMapper.BOX_MAPPER,
            this.borderTopLeftRadius, this.borderTopRightRadius,
            this.borderBottomRightRadius, this.borderBottomLeftRadius));
    this.registerProperty(this.borderTopColor =
        new StyleableProperty<>("border-top-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderRightColor =
        new StyleableProperty<>("border-right-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderBottomColor =
        new StyleableProperty<>("border-bottom-color", Color.class, Color.BLACK));
    this.registerProperty(this.borderLeftColor =
        new StyleableProperty<>("border-left-color", Color.class, Color.BLACK));
    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("border-color", Color.class, ShorthandArgMapper.BOX_MAPPER,
            this.borderTopColor, this.borderRightColor,
            this.borderBottomColor, this.borderLeftColor));

    this.registerProperty(this.color = new StyleableProperty<>("color", Color.class, Color.WHITE));
    this.registerProperty(
        this.objectFit = new StyleableProperty<>("object-fit", FitType.class, FitType.FILL));
    this.registerProperty(this.fontSize = new StyleableProperty<>("font-size", Integer.class, 11));
    this.registerProperty(this.fontFamily =
        new StyleableProperty<>("font-family", String[].class, new String[0]));
    this.registerProperty(this.textAlign =
        new StyleableProperty<>("text-align", Alignment.class, Alignment.START));
    this.registerProperty(this.textShadow =
        new StyleableProperty<>("text-shadow", Shadow[].class, new Shadow[0]));
    this.registerProperty(this.backdropFilter =
        new StyleableProperty<>("backdrop-filter", Filter[].class, new Filter[0]));
  }

  public StyleHolder getStyleHolder() {
    return this.styleHolder;
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
    this.view.stateManager.addListener(property);
    this.styleHolder.registerDispatcher(property);
  }

  public final void defineBorderColorState(Color color, int... states) {
    this.borderTopColor.defineState(color, states);
    this.borderRightColor.defineState(color, states);
    this.borderBottomColor.defineState(color, states);
    this.borderLeftColor.defineState(color, states);
  }
}
