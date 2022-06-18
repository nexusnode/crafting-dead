package sm0keysa1m0n.bliss.view;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.FilterBlurMode;
import io.github.humbleui.skija.MaskFilter;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.RRect;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import sm0keysa1m0n.bliss.Bliss;
import sm0keysa1m0n.bliss.BoxSide;
import sm0keysa1m0n.bliss.BoxSizing;
import sm0keysa1m0n.bliss.Length;
import sm0keysa1m0n.bliss.Overflow;
import sm0keysa1m0n.bliss.PointerEvents;
import sm0keysa1m0n.bliss.Visibility;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.layout.PositionType;
import sm0keysa1m0n.bliss.platform.GraphicsContext;
import sm0keysa1m0n.bliss.platform.ScissorStack;
import sm0keysa1m0n.bliss.style.States;
import sm0keysa1m0n.bliss.style.StyleList;
import sm0keysa1m0n.bliss.style.StyleManager;
import sm0keysa1m0n.bliss.style.StyleNode;
import sm0keysa1m0n.bliss.view.event.ActionEvent;
import sm0keysa1m0n.bliss.view.event.AddedEvent;
import sm0keysa1m0n.bliss.view.event.CharTypeEvent;
import sm0keysa1m0n.bliss.view.event.KeyEvent;
import sm0keysa1m0n.bliss.view.event.MouseEnterEvent;
import sm0keysa1m0n.bliss.view.event.MouseEvent;
import sm0keysa1m0n.bliss.view.event.MouseLeaveEvent;
import sm0keysa1m0n.bliss.view.event.RemovedEvent;

public class View implements Comparable<View>, StyleNode {

  public static final boolean DEBUG = false;

  private static final int SCROLLBAR_WIDTH = 4;

  private static final float SCROLL_CHUNK = 50F;

  private static final float SCROLL_MOMENTUM_DAMPING = 3F;

  private static final int DOUBLE_CLICK_DURATION_MS = 500;

  /**
   * Coupling to Minecraft is being phased out.
   */
  @Deprecated(forRemoval = true)
  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final GraphicsContext graphicsContext = Bliss.instance().getGraphicsContext();

  /**
   * To be replaced by an alternative library.
   */
  @Deprecated(forRemoval = true)
  private final IEventBus eventBus = BusBuilder.builder().build();

  private final ViewStyle style;

  /*
   * ========== State ==========
   */

  @Nullable
  ViewScreen screen;
  @Nullable
  ParentView parent;
  int index;
  private Layout layout = Layout.NILL;
  private float lastScrollOffset;
  private float scrollOffset;
  private float scrollVelocity;
  private float fullHeight;
  private long lastClickTimeMs;
  private boolean closed;
  private boolean added;

  /*
   * ========== Properties ==========
   */

  @Nullable
  private final String id;
  private final Set<String> styleClasses;
  private final boolean doubleClick;
  private final boolean unscaleWidth;
  private final boolean unscaleHeight;
  private final boolean unscaleBorder;
  private final boolean unscaleOutline;
  private final boolean focusable;

  public View(Properties properties) {
    this.id = properties.id;
    this.styleClasses = properties.styleClasses.build();
    this.doubleClick = properties.doubleClick;
    this.unscaleWidth = properties.unscaleWidth;
    this.unscaleHeight = properties.unscaleHeight;
    this.unscaleBorder = properties.unscaleBorder;
    this.unscaleOutline = properties.unscaleOutline;
    this.focusable = properties.focusable;

    this.style = new ViewStyle(this);

    this.eventBus.start();
    this.getStyleManager().addState(States.ENABLED);
  }

  protected float computeFullHeight() {
    return this.getContentHeight();
  }

  protected void added() {
    this.style.getStyleManager().refresh();
    this.added = true;
    this.post(new AddedEvent());
  }

  protected void removed() {
    this.added = false;
    this.post(new RemovedEvent());
  }

  protected void setLayout(Layout layout) {
    Objects.requireNonNull(layout, "layout cannot be null.");
    if (this.layout == layout) {
      return;
    }
    this.layout.close();
    this.layout = layout;
    this.layout.setAll(this.getStyle());
  }

  protected void layout() {
    this.fullHeight = this.computeFullHeight();
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);
  }

  public void tick() {
    this.lastScrollOffset = this.scrollOffset;

    this.scrollVelocity *= (1.0F / SCROLL_MOMENTUM_DAMPING);
    if (Math.abs(this.scrollVelocity) < 0.08F) {
      this.scrollVelocity = 0.0F;
    }

    this.scrollOffset += this.scrollVelocity;

    if (this.scrollOffset < 0.0F
        || this.scrollOffset > this.fullHeight - this.getContentHeight()) {
      this.scrollVelocity = 0.0F;
    }
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);
  }

  public final void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    if (!this.isVisible()) {
      return;
    }

    var canvas = this.graphicsContext.canvas();
    canvas.save();
    {
      var clip = this.layout.getOverflow().isClipped();

      this.drawAndClipBox(canvas, clip);

      if (clip) {
        var scale = this.graphicsContext.scale();
        ScissorStack.push(
            (int) (this.getScaledX() * scale),
            // Convert to OpenGL coordinate
            (int) (minecraft.getWindow().getHeight() - ((this.getScaledY() * scale) + (this.getScaledHeight() * scale))),
            (int) (this.getScaledWidth() * scale),
            (int) (this.getScaledHeight() * scale));
      }

      this.renderContent(poseStack, mouseX, mouseY, partialTick);

      if (clip) {
        ScissorStack.pop();
      }

      this.drawScrollbar();
    }
    canvas.restore();
  }

  private void drawScrollbar() {
    if (!this.isScrollbarEnabled()) {
      return;
    }

    final var scale = this.graphicsContext.scale();

    final var scrollbarX = this.getScrollbarX();
    final var scaledY = this.getScaledY();

    final var scrollbarWidth = SCROLLBAR_WIDTH * this.getXScale();

    final var scrollbarRadius = (scrollbarWidth / 2.0F) * scale;

    final var scrollbarX2 = scrollbarX + scrollbarWidth;
    final var scaledY2 = scaledY + this.getScaledHeight();

    var canvas = this.graphicsContext.canvas();
    try (var paint = new Paint()) {
      paint.setMode(PaintMode.FILL);
      paint.setColor(0x40000000);
      canvas.drawRRect(
          RRect.makeLTRB(
              scrollbarX * scale,
              scaledY * scale,
              scrollbarX2 * scale,
              scaledY2 * scale,
              scrollbarRadius),
          paint);
    }

    final var scrollbarY = this.getScrollbarY();
    final var scrollbarY2 = scrollbarY + this.getScrollbarHeight();
    try (var paint = new Paint()) {
      paint.setMode(PaintMode.FILL);
      paint.setColor(0x4CFFFFFF);
      canvas.drawRRect(
          RRect.makeLTRB(
              scrollbarX * scale,
              scrollbarY * scale,
              scrollbarX2 * scale,
              scrollbarY2 * scale,
              scrollbarRadius),
          paint);
    }
  }

  private void drawAndClipBox(Canvas canvas, boolean clip) {
    final var scale = this.graphicsContext.scale();

    final var topBorderWidth = this.unscale(this.layout.getTopBorder(), this.unscaleBorder);
    final var bottomBorderWidth = this.unscale(this.layout.getBottomBorder(), this.unscaleBorder);
    final var leftBorderWidth = this.unscale(this.layout.getLeftBorder(), this.unscaleBorder);
    final var rightBorderWidth = this.unscale(this.layout.getRightBorder(), this.unscaleBorder);

    final var borderTopLeftRadius =
        this.unscale(this.style.borderTopLeftRadius.get(), this.unscaleBorder);
    final var borderTopRightRadius =
        this.unscale(this.style.borderTopRightRadius.get(), this.unscaleBorder);
    final var borderBottomRightRadius =
        this.unscale(this.style.borderBottomRightRadius.get(), this.unscaleBorder);
    final var borderBottomLeftRadius =
        this.unscale(this.style.borderBottomLeftRadius.get(), this.unscaleBorder);

    final var outlineWidth =
        this.unscale(this.style.outlineWidth.get().fixed(), this.unscaleOutline);

    final var radii = new float[] {
        borderTopLeftRadius * scale,
        borderTopRightRadius * scale,
        borderBottomRightRadius * scale,
        borderBottomLeftRadius * scale
    };

    final var rect = RRect.makeComplexXYWH(
        this.getScaledX() * scale,
        this.getScaledY() * scale,
        this.getScaledWidth() * scale,
        this.getScaledHeight() * scale,
        radii);

    if (outlineWidth > 0.0F) {
      try (var paint = new Paint()) {
        paint.setMode(PaintMode.STROKE);
        paint.setStrokeWidth(outlineWidth * scale);
        paint.setColor(this.style.outlineColor.get().multiplied(this.getAlpha()));
        paint.setAntiAlias(false);
        canvas.drawRRect(rect.inflate(outlineWidth).withRadii(radii), paint);
      }
    }

    this.drawBackdrop(canvas, rect);

    if (clip) {
      canvas.clipRRect(rect, true);
    }

    var innerRect = RRect.makeComplexLTRB(
        rect.getLeft() + leftBorderWidth * scale,
        rect.getTop() + topBorderWidth * scale,
        rect.getRight() - rightBorderWidth * scale,
        rect.getBottom() - bottomBorderWidth * scale,
        radii);

    try (var paint = new Paint()) {
      if (topBorderWidth > 0.0F) {
        paint.setColor(this.style.borderTopColor.get().multiplied(this.getAlpha()));
        this.drawBorderEdge(canvas, rect, innerRect, BoxSide.TOP, paint);
      }
      if (rightBorderWidth > 0.0F) {
        paint.setColor(this.style.borderRightColor.get().multiplied(this.getAlpha()));
        this.drawBorderEdge(canvas, rect, innerRect, BoxSide.RIGHT, paint);
      }
      if (bottomBorderWidth > 0.0F) {
        paint.setColor(this.style.borderBottomColor.get().multiplied(this.getAlpha()));
        this.drawBorderEdge(canvas, rect, innerRect, BoxSide.BOTTOM, paint);
      }
      if (leftBorderWidth > 0.0F) {
        paint.setColor(this.style.borderLeftColor.get().multiplied(this.getAlpha()));
        this.drawBorderEdge(canvas, rect, innerRect, BoxSide.LEFT, paint);
      }
    }

    try (var paint = new Paint()) {
      paint.setMode(PaintMode.FILL);
      paint.setColor(this.style.backgroundColor.get().multiplied(this.getAlpha()));
      canvas.drawRRect(rect, paint);
    }

    if (clip) {
      this.drawBoxShadows(canvas, rect, true);
    } else {
      canvas.save();
      {
        canvas.clipRRect(rect, true);
        this.drawBoxShadows(canvas, rect, true);
      }
      canvas.restore();
    }
  }

  private static RRect inflate(RRect rect, float spreadX, float spreadY) {
    var midSpread = (spreadX + spreadY) / 2.0F;
    float[] radii = Arrays.copyOf(rect._radii, rect._radii.length);
    for (int i = 0; i < radii.length; ++i) {
      radii[i] = Math.max(0.0F, radii[i] + midSpread);
    }
    return new RRect(rect._left - spreadX, rect._top - spreadY,
        Math.max(rect._left - spreadX, rect._right + spreadX),
        Math.max(rect._top - spreadY, rect._bottom + spreadY),
        radii);
  }

  private void drawBoxShadows(Canvas canvas, RRect rect, boolean inset) {
    var shadows = this.style.boxShadow.get();
    for (var shadow : shadows) {
      try (var paint = new Paint()) {
        paint.setColor(shadow.color().multiplied(this.getAlpha()));
        if (shadow.blurRadius() > 0) {
          paint.setMaskFilter(MaskFilter.makeBlur(FilterBlurMode.NORMAL, shadow.blurRadius()));
        }
        if (shadow.inset() && inset) {
          var delta = new Point(10 + Math.abs(shadow.offsetX()), 10 + Math.abs(shadow.offsetY()));
          var inner = inflate(rect, -shadow.spreadRadius(), -shadow.spreadRadius())
              .offset(shadow.offsetX(), shadow.offsetY());
          var outer = inflate(rect, delta.getX(), delta.getY());
          canvas.drawDRRect(outer, inner, paint);
        } else if (!shadow.inset() && !inset) {
          canvas.drawRRect(inflate(rect, shadow.spreadRadius(), shadow.spreadRadius())
              .offset(shadow.offsetX(), shadow.offsetY()), paint);
        }
      }
    }
  }

  private void drawBackdrop(Canvas canvas, RRect rect) {
    var backdropFilters = this.style.backdropFilter.get();
    if (backdropFilters.length == 0) {
      this.drawBoxShadows(canvas, rect, false);
      return;
    }

    var intRect = rect.toIRect();

    try (var image = this.graphicsContext.surface().makeImageSnapshot(intRect);
        var paint = new Paint()) {
      for (var filter : backdropFilters) {
        filter.apply(paint);
      }

      canvas.save();
      {
        canvas.clipRRect(rect, ClipMode.DIFFERENCE);
        this.drawBoxShadows(canvas, rect, false);
      }
      canvas.restore();

      canvas.save();
      {
        canvas.clipRRect(rect, true);
        canvas.drawImageRect(image, intRect.toRect(), paint);
      }
      canvas.restore();
    } catch (RuntimeException e) {
      // TODO consume this until makeImageSnapshot can return null.
    }
  }

  private void drawBorderEdge(Canvas canvas, RRect rect, RRect innerRect, BoxSide side,
      Paint paint) {
    canvas.save();
    {
      this.clipBorderSidePolygon(canvas, rect, innerRect, side, false, false);
      canvas.drawDRRect(rect, innerRect, paint);
    }
    canvas.restore();
  }

  private void clipBorderSidePolygon(Canvas canvas, RRect outerRect, RRect innerRect,
      BoxSide side, boolean firstEdgeMatches, boolean secondEdgeMatches) {
    // @formatter:off
    // For each side, create a quad that encompasses all parts of that side that may draw,
    // including areas inside the innerBorder.
    //
    //         0----------------3
    //       0  \              /  0
    //       |\  1----------- 2  /|
    //       | 1                1 |   
    //       | |                | |
    //       | |                | |  
    //       | 2                2 |  
    //       |/  1------------2  \| 
    //       3  /              \  3   
    //         0----------------3
    //
    // @formatter:on

    var finalQuad = switch (side) {
      case TOP -> {
        var quad = new Point[] {
            GeometryUtil.getTopLeft(outerRect),
            GeometryUtil.getTopLeft(innerRect),
            GeometryUtil.getTopRight(innerRect),
            GeometryUtil.getTopRight(outerRect)};

        // top left
        if (innerRect._radii[0] != 0) {
          GeometryUtil
              .findIntersection(
                  GeometryUtil.getTopLeft(outerRect),
                  GeometryUtil.getTopLeft(innerRect),
                  GeometryUtil.getBottomLeft(innerRect),
                  GeometryUtil.getTopRight(innerRect))
              .ifPresent(point -> quad[1] = point);
        }

        // top right
        if (innerRect._radii[1] != 0) {
          GeometryUtil
              .findIntersection(
                  GeometryUtil.getTopRight(outerRect),
                  GeometryUtil.getTopRight(innerRect),
                  GeometryUtil.getTopLeft(innerRect),
                  GeometryUtil.getBottomRight(innerRect))
              .ifPresent(point -> quad[2] = point);
        }
        yield quad;
      }
      case LEFT -> {
        var quad = new Point[] {
            GeometryUtil.getTopLeft(outerRect),
            GeometryUtil.getTopLeft(innerRect),
            GeometryUtil.getBottomLeft(innerRect),
            GeometryUtil.getBottomLeft(outerRect)};

        // top left
        if (innerRect._radii[0] != 0) {
          GeometryUtil
              .findIntersection(GeometryUtil.getTopLeft(outerRect),
                  GeometryUtil.getTopLeft(innerRect),
                  GeometryUtil.getBottomLeft(innerRect), GeometryUtil.getTopRight(innerRect))
              .ifPresent(point -> quad[1] = point);
        }

        // bottom left
        if (innerRect._radii[3] != 0) {
          GeometryUtil
              .findIntersection(GeometryUtil.getBottomLeft(outerRect),
                  GeometryUtil.getBottomLeft(innerRect),
                  GeometryUtil.getTopLeft(innerRect), GeometryUtil.getBottomRight(innerRect))
              .ifPresent(point -> quad[2] = point);
        }
        yield quad;
      }
      case BOTTOM -> {
        var quad = new Point[] {
            GeometryUtil.getBottomLeft(outerRect),
            GeometryUtil.getBottomLeft(innerRect),
            GeometryUtil.getBottomRight(innerRect),
            GeometryUtil.getBottomRight(outerRect)};

        // bottom left
        if (innerRect._radii[3] != 0) {
          GeometryUtil
              .findIntersection(GeometryUtil.getBottomLeft(outerRect),
                  GeometryUtil.getBottomLeft(innerRect),
                  GeometryUtil.getTopLeft(innerRect), GeometryUtil.getBottomRight(innerRect))
              .ifPresent(point -> quad[1] = point);
        }

        // bottom right
        if (innerRect._radii[2] != 0) {
          GeometryUtil
              .findIntersection(GeometryUtil.getBottomRight(outerRect),
                  GeometryUtil.getBottomRight(innerRect),
                  GeometryUtil.getTopRight(innerRect), GeometryUtil.getBottomLeft(innerRect))
              .ifPresent(point -> quad[2] = point);
        }
        yield quad;
      }
      case RIGHT -> {
        var quad = new Point[] {
            GeometryUtil.getTopRight(outerRect),
            GeometryUtil.getTopRight(innerRect),
            GeometryUtil.getBottomRight(innerRect),
            GeometryUtil.getBottomRight(outerRect)};

        // top right
        if (innerRect._radii[1] != 0) {
          GeometryUtil
              .findIntersection(
                  GeometryUtil.getTopRight(outerRect),
                  GeometryUtil.getTopRight(innerRect),
                  GeometryUtil.getTopLeft(innerRect),
                  GeometryUtil.getBottomRight(innerRect))
              .ifPresent(point -> quad[1] = point);
        }

        // bottom right
        if (innerRect._radii[2] != 0) {
          GeometryUtil
              .findIntersection(
                  GeometryUtil.getBottomRight(outerRect),
                  GeometryUtil.getBottomRight(innerRect),
                  GeometryUtil.getTopRight(innerRect),
                  GeometryUtil.getBottomLeft(innerRect))
              .ifPresent(point -> quad[2] = point);
        }
        yield quad;
      }
    };

    // If the border matches both of its adjacent sides, don't anti-alias the clip, and
    // if neither side matches, anti-alias the clip.
    if (firstEdgeMatches == secondEdgeMatches) {
      canvas.clipPath(GeometryUtil.polygonPathFromPoints(finalQuad), ClipMode.INTERSECT,
          !firstEdgeMatches);
      return;
    }

    // Square off the end which shouldn't be affected by antialiasing, and clip.
    Point[] firstQuad = {
        finalQuad[0],
        finalQuad[1],
        finalQuad[2],
        side == BoxSide.TOP || side == BoxSide.BOTTOM
            ? new Point(finalQuad[3].getX(), finalQuad[2].getY())
            : new Point(finalQuad[2].getX(), finalQuad[3].getY()),
        finalQuad[3]
    };
    canvas.clipPath(GeometryUtil.polygonPathFromPoints(firstQuad), ClipMode.INTERSECT,
        !firstEdgeMatches);

    Point[] secondQuad = {
        finalQuad[0],
        side == BoxSide.TOP || side == BoxSide.BOTTOM
            ? new Point(finalQuad[0].getX(), finalQuad[1].getY())
            : new Point(finalQuad[1].getX(), finalQuad[0].getY()),
        finalQuad[1],
        finalQuad[2],
        finalQuad[3]
    };


    // Antialiasing affects the second side.
    canvas.clipPath(GeometryUtil.polygonPathFromPoints(secondQuad), ClipMode.INTERSECT,
        false);

  }

  protected final boolean isScrollbarEnabled() {
    return this.layout.getOverflow() == Overflow.SCROLL
        && this.fullHeight > this.getContentHeight();
  }

  @SuppressWarnings("unused")
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    if (DEBUG && this.isHovered()) {
      this.graphicsContext.exitManaged();
      RenderUtil.fillWidthHeight(poseStack, this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledContentWidth(), this.getScaledContentHeight(), 0x333495eb);

      // Left border
      RenderUtil.fill(poseStack, this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Left padding
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledContentX(),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);

      // Right border
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Right padding
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder)
              - this.layout.getRightPadding(),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);

      // Top border
      RenderUtil.fill(poseStack, this.getScaledX(),
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          0x33e3b352);
      // Top padding
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledContentY(), 0x3384ab05);

      // Bottom border
      RenderUtil.fill(poseStack, this.getScaledX(),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Bottom padding
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledContentY() + this.getScaledContentHeight(),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);
      this.graphicsContext.enterManaged();
    }
  }

  private final float getScrollbarX() {
    return this.getScaledX() + this.getScaledWidth() - SCROLLBAR_WIDTH * this.getXScale();
  }

  private final float getScrollbarY() {
    return this.getScaledY()
        + (this.getScrollOffset(this.minecraft.getFrameTime()) / this.fullHeight)
            * this.getScaledHeight();
  }

  private final float getScrollOffset(float partialTicks) {
    return Mth.lerp(partialTicks, this.lastScrollOffset, this.scrollOffset);
  }

  // Visible for property getter in scrollTo animation.
  @VisibleForTesting
  public final float getScrollOffset() {
    return this.scrollOffset;
  }

  // Visible for property setter in scrollTo animation.
  @VisibleForTesting
  public final void setScrollOffset(float scrollOffset) {
    this.scrollOffset = scrollOffset;
  }

  private final float getScrollbarHeight() {
    return Mth.clamp(
        this.getScaledHeight() * (this.getContentHeight() / this.fullHeight),
        10.0F * this.getYScale(),
        this.getScaledHeight());
  }

  private final void scrollTo(float y, long durationMs) {
    new Animator.Builder()
        .addTarget(PropertySetter.getTargetTo(this, "scrollOffset",
            new SplineInterpolator(0.25, 0.1, 0.25, 1),
            this.clampScrollOffset(y)))
        .setDuration(durationMs, TimeUnit.MILLISECONDS)
        .build()
        .start();
  }

  private final float clampScrollOffset(float scrollOffset) {
    return Mth.clamp(scrollOffset, 0.0F, this.fullHeight - this.getContentHeight());
  }

  public void mouseEntered(double mouseX, double mouseY) {
    this.getStyleManager().addState(States.HOVER);
    this.getStyleManager().notifyListeners();

    this.post(new MouseEnterEvent());
  }

  public void mouseLeft(double mouseX, double mouseY) {
    this.getStyleManager().removeState(States.HOVER);
    this.getStyleManager().notifyListeners();

    this.post(new MouseLeaveEvent());
  }

  public void mouseMoved(double mouseX, double mouseY) {
    this.post(new MouseEvent.MoveEvent(mouseX, mouseY));
  }

  public boolean tryFocus(boolean visible) {
    if (this.isFocused()) {
      return true;
    }

    if (!this.focusable || this.getStyleManager().hasState(States.DISABLED)) {
      return false;
    }

    if (visible) {
      this.getStyleManager().addState(States.FOCUS_VISIBLE);
    }
    this.getStyleManager().addState(States.FOCUS);
    this.getStyleManager().notifyListeners();
    this.focusChanged();

    if (this.parent != null) {
      this.parent.focusedView = this;
    }

    return true;
  }

  public void removeFocus() {
    if (this.isFocused()) {
      this.getStyleManager().removeState(States.FOCUS);
      this.getStyleManager().removeState(States.FOCUS_VISIBLE);
      this.getStyleManager().notifyListeners();
      this.focusChanged();

      if (this.parent != null) {
        this.parent.focusedView = null;
      }
    }
  }

  /**
   * Called when the mouse is pressed on this component.
   * 
   * @param mouseX - the mouse's x coordinate
   * @param mouseY - the mouse's y coordinate
   * @param button - the button that was pressed
   * @return <code>true</code> if drag events should be captured
   */
  public boolean mousePressed(double mouseX, double mouseY, int button) {
    if (this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button,
        GLFW.GLFW_PRESS)) == Event.Result.ALLOW) {
      return false;
    }

    // Clicked on scrollbar region
    if (this.isScrollbarEnabled() && button == GLFW.GLFW_MOUSE_BUTTON_LEFT
        && mouseX >= this.getScaledX() + this.getScaledWidth() - SCROLLBAR_WIDTH
        && mouseX <= this.getScaledX() + this.getScaledWidth()
        && mouseY >= this.getScaledY()
        && mouseY <= this.getScaledY() + this.getScaledHeight()) {
      // Clicked outside of scrollbar
      if (mouseY < this.getScrollbarY()
          || mouseY > this.getScrollbarY() + this.getScrollbarHeight()) {
        this.scrollTo(this.scrollOffset
            + (mouseY > this.getScrollbarY() ? this.getScaledHeight() : -this.getScaledHeight()),
            200);
      }
      return true;
    }

    if (!this.isFocused()) {
      return false;
    }

    var currentTime = Util.getMillis();
    var deltaTime = currentTime - this.lastClickTimeMs;
    this.lastClickTimeMs = currentTime;
    if (!this.doubleClick || deltaTime < DOUBLE_CLICK_DURATION_MS) {
      this.post(new ActionEvent());
    }

    return false;
  }

  public void mouseReleased(double mouseX, double mouseY, int button) {
    this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button, GLFW.GLFW_RELEASE));
  }

  public void mouseDragged(double mouseX, double mouseY, int button,
      double deltaX, double deltaY) {
    if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT
        && this.isScrollbarEnabled()
        && mouseY >= this.getScaledY()
        && mouseY <= this.getScaledY() + this.getScaledHeight()) {
      this.scrollOffset = this.clampScrollOffset(this.scrollOffset
          + ((float) deltaY) * this.fullHeight / this.getScaledHeight());
      return;
    }
    this.post(new MouseEvent.DragEvent(mouseX, mouseY, button, deltaX, deltaY));
  }

  public void mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    if (this.isScrollbarEnabled()) {
      this.scrollVelocity += (float) (-scrollDelta * SCROLL_CHUNK);
    }
    this.post(new MouseEvent.ScrollEvent(mouseX, mouseY, scrollDelta));
  }

  public void keyPressed(int keyCode, int scanCode, int modifiers) {
    if (this.post(
        new KeyEvent(keyCode, scanCode, GLFW.GLFW_PRESS, modifiers)) == Event.Result.ALLOW) {
      return;
    }

    if (keyCode == GLFW.GLFW_KEY_ENTER && this.post(new ActionEvent()) == Event.Result.ALLOW) {
      return;
    }
  }

  public void keyReleased(int keyCode, int scanCode, int modifiers) {
    this.post(new KeyEvent(keyCode, scanCode, GLFW.GLFW_RELEASE, modifiers));
  }

  public void charTyped(char codePoint, int modifiers) {
    this.post(new CharTypeEvent(codePoint, modifiers));
  }

  public Optional<View> changeFocus(boolean forward) {
    if (!this.focusable || this.getStyleManager().hasState(States.DISABLED)) {
      return Optional.empty();
    }

    var focused = this.getStyleManager().toggleState(States.FOCUS);
    if (focused) {
      this.getStyleManager().addState(States.FOCUS_VISIBLE);
    } else {
      this.getStyleManager().removeState(States.FOCUS_VISIBLE);
    }
    this.getStyleManager().notifyListeners();
    this.focusChanged();

    return focused ? Optional.of(this) : Optional.empty();
  }

  protected void focusChanged() {}

  /**
   * Checks if mouse is over a component based on mouse position. Doesn't check whether there are
   * other components over this one.
   * 
   * @see #isHovered()
   */
  public boolean isMouseOver(double mouseX, double mouseY) {
    return this.style.pointerEvents.get() != PointerEvents.NONE
        && mouseX > this.getScaledX() && mouseX < this.getScaledX() + this.getScaledWidth()
        && mouseY > this.getScaledY() && mouseY < this.getScaledY() + this.getScaledHeight();
  }

  public void close() {
    if (this.closed) {
      throw new IllegalStateException("Already closed.");
    }
    this.layout.close();
    this.closed = true;
  }

  public final void addActionSound(SoundEvent soundEvent) {
    this.addListener(ActionEvent.class, event -> this.minecraft.getSoundManager()
        .play(SimpleSoundInstance.forUI(soundEvent, 1.0F)));
  }

  public final void addHoverSound(SoundEvent soundEvent) {
    this.addListener(MouseEnterEvent.class, event -> this.minecraft.getSoundManager()
        .play(SimpleSoundInstance.forUI(soundEvent, 1.0F)));
  }

  public final <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer) {
    this.addListener(eventType, consumer, true);
  }

  public final <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer,
      boolean consumeEvent) {
    this.eventBus.addListener(EventPriority.NORMAL, true, eventType, event -> {
      if (event.hasResult() && consumeEvent) {
        event.setResult(Event.Result.ALLOW);
      }
      consumer.accept(event);
    });
  }

  /**
   * Submit the event for dispatch to appropriate listeners.
   *
   * @param event - The event to dispatch to listeners
   * @return true if the event was cancelled
   */
  public final Event.Result post(Event event) {
    this.eventBus.post(event);
    return event.getResult();
  }

  public final float getScaledContentX() {
    return this.getContentX()
        + (this.getContentWidth() - (this.getContentWidth() * this.style.xScale.get())) / 2.0F
        + (this.layout.getLeftPadding()
            - (this.layout.getLeftPadding() * this.style.xScale.get()))
            / 2.0F
        + (this.layout.getRightPadding()
            - (this.layout.getRightPadding() * this.style.xScale.get()))
            / 2.0F
        + (this.isScrollbarEnabled()
            ? (SCROLLBAR_WIDTH - (SCROLLBAR_WIDTH * this.style.xScale.get())) / 2.0F
            : 0.0F);
  }

  public float getContentX() {
    return this.getX()
        + this.layout.getLeftPadding() * this.getXScale()
        + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder) * this.getXScale();
  }

  public final float getScaledX() {
    return this.getX()
        + (this.getWidth() - (this.getWidth() * this.style.xScale.get())) / 2.0F;
  }

  public final float getX() {
    var position = this.style.position.get();
    var x = this.parent == null ? 0.0F : this.parent.getScaledContentX();
    if (position != PositionType.ABSOLUTE) {
      var layoutX = this.layout.getX();
      x += layoutX + (layoutX * this.getXScale()) - (layoutX * this.style.xScale.get());
    }

    var left = this.style.left.get();
    var right = this.style.right.get();
    var parentWidth = this.parent == null ? this.screen.width : this.parent.getScaledWidth();
    if (left != Length.AUTO) {
      x += left.valueForLength(parentWidth);
    } else if (right != Length.AUTO) {
      if (position == PositionType.ABSOLUTE) {
        var parentRight = this.parent == null ? this.screen.width
            : this.parent.getScaledContentX() + this.parent.getScaledContentWidth();
        x = parentRight - right.valueForLength(parentWidth) - this.getScaledWidth();
      } else {
        x -= right.valueForLength(parentWidth);
      }
    }

    return x
        + this.style.xTranslation.get()
        + this.unscaleOffset(this.getWidth(), this.unscaleWidth);
  }

  public final float getScaledContentY() {
    return this.getContentY()
        + (this.getContentHeight() - (this.getContentHeight() * this.style.yScale.get()))
            / 2.0F
        + (this.layout.getTopPadding()
            - (this.layout.getTopPadding() * this.style.yScale.get()))
            / 2.0F
        + (this.layout.getBottomPadding()
            - (this.layout.getBottomPadding() * this.style.yScale.get()))
            / 2.0F;
  }

  public float getContentY() {
    return this.getY()
        + this.layout.getTopPadding() * this.getYScale()
        + this.unscale(this.layout.getTopBorder(), this.unscaleBorder) * this.getYScale()
        - (this.isScrollbarEnabled()
            ? this.getScrollOffset(this.minecraft.getFrameTime()) * this.getYScale()
            : 0.0F);
  }

  public final float getScaledY() {
    return this.getY()
        + (this.getHeight() - (this.getHeight() * this.style.yScale.get())) / 2.0F;
  }

  public final float getY() {
    var position = this.style.position.get();
    var y = this.parent == null ? 0.0F : this.parent.getScaledContentY();
    if (position != PositionType.ABSOLUTE) {
      var layoutY = this.layout.getY();
      y += layoutY + (layoutY * this.getYScale()) - (layoutY * this.style.yScale.get());
    }

    var top = this.style.top.get();
    var bottom = this.style.bottom.get();
    var parentHeight = this.parent == null ? this.screen.height : this.parent.getScaledHeight();
    if (top != Length.AUTO) {
      y += top.valueForLength(parentHeight);
    } else if (bottom != Length.AUTO) {
      if (position == PositionType.ABSOLUTE) {
        var parentRight = this.parent == null ? this.screen.height
            : this.parent.getScaledContentY() + this.parent.getScaledContentHeight();
        y = parentRight - bottom.valueForLength(parentHeight) - this.getScaledHeight();
      } else {
        y -= bottom.valueForLength(parentHeight);
      }
    }

    return y
        + this.style.yTranslation.get()
        + (this.unscaleHeight
            ? this.getHeight() * this.graphicsContext.scale() - this.getHeight()
            : 0.0F);
  }

  public final float getScaledContentWidth() {
    return this.getContentWidth() * this.getXScale();
  }

  public float getContentWidth() {
    return this.getWidth()
        - (this.isScrollbarEnabled() ? SCROLLBAR_WIDTH : 0.0F)
        - this.layout.getRightPadding()
        - this.layout.getLeftPadding()
        - this.unscale(this.layout.getRightBorder(), this.unscaleBorder)
        - this.unscale(this.layout.getLeftBorder(), this.unscaleBorder);
  }

  public final float getScaledWidth() {
    return this.getWidth() * this.getXScale();
  }

  public final float getWidth() {
    var width = this.unscale(this.layout.getWidth(), this.unscaleWidth);

    var right = this.style.right.get();
    if (right != Length.AUTO &&
        this.style.position.get() == PositionType.ABSOLUTE && width == 0) {
      var parentWidth = this.parent == null ? this.screen.width : this.parent.getWidth();
      var parentRight = this.parent == null ? parentWidth : this.parent.getX() + parentWidth;
      width = parentRight - right.valueForLength(parentWidth) - this.getX();
    }

    if (this.style.boxSizing.get() == BoxSizing.CONTENT_BOX) {
      width += this.layout.getRightPadding()
          + this.layout.getLeftPadding()
          + this.unscale(this.layout.getRightBorder(), this.unscaleBorder)
          + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder);
    }

    return width;
  }

  public final float getScaledContentHeight() {
    return this.getContentHeight() * this.getYScale();
  }

  public final float getContentHeight() {
    return this.getHeight()
        - this.layout.getBottomPadding()
        - this.layout.getTopPadding()
        - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder)
        - this.unscale(this.layout.getTopBorder(), this.unscaleBorder);
  }

  public float getScaledHeight() {
    return this.getHeight() * this.getYScale();
  }

  public final float getHeight() {
    var height = this.unscale(this.layout.getHeight(), this.unscaleHeight);

    var bottom = this.style.bottom.get();
    if (bottom != Length.AUTO &&
        this.style.position.get() == PositionType.ABSOLUTE && height == 0) {
      var parentHeight = this.parent == null ? this.screen.height : this.parent.getHeight();
      var parentBottom = this.parent == null ? parentHeight : this.parent.getY() + parentHeight;
      height = parentBottom - bottom.valueForLength(parentHeight) - this.getY();
    }

    if (this.style.boxSizing.get() == BoxSizing.CONTENT_BOX) {
      height += this.layout.getBottomPadding()
          + this.layout.getTopPadding()
          + this.unscale(this.layout.getBottomBorder(), this.unscaleBorder)
          + this.unscale(this.layout.getTopBorder(), this.unscaleBorder);
    }
    return height;
  }

  public final float getXScale() {
    return (this.parent == null ? 1.0F : this.parent.getXScale()) * this.style.xScale.get();
  }

  public final float getYScale() {
    return (this.parent == null ? 1.0F : this.parent.getYScale()) * this.style.yScale.get();
  }

  public final float getAlpha() {
    return (this.parent == null ? 1.0F : this.parent.getAlpha()) * this.style.opacity.get().value();
  }

  public final void setEnabled(boolean enabled) {
    if (enabled) {
      this.getStyleManager().addState(States.ENABLED);
      this.getStyleManager().removeState(States.DISABLED);
    } else {
      this.getStyleManager().removeState(States.ENABLED);
      this.getStyleManager().addState(States.DISABLED);
      if (this.getStyleManager().hasState(States.FOCUS)) {
        this.getStyleManager().removeState(States.FOCUS);
      }
    }
    this.getStyleManager().notifyListeners();
  }

  public final boolean isHovered() {
    return this.getStyleManager().hasState(States.HOVER);
  }

  public final boolean isFocused() {
    return this.getStyleManager().hasState(States.FOCUS);
  }

  public final ViewScreen getScreen() {
    if (this.screen == null) {
      if (this.parent == null) {
        throw new UnsupportedOperationException("Root view has no screen.");
      }
      return this.parent.getScreen();
    }
    return this.screen;
  }

  public final boolean hasParent() {
    return this.parent != null;
  }

  public final ParentView getParent() {
    return this.parent;
  }

  public final boolean hasLayout() {
    return this.layout != null;
  }

  public final Layout getLayout() {
    return this.layout;
  }

  public final boolean isClosed() {
    return this.closed;
  }

  public final boolean isAdded() {
    return this.added;
  }

  protected final float unscaleOffset(float value, boolean condition) {
    return condition
        ? value * this.graphicsContext.scale() - value
        : 0.0F;
  }

  protected final float unscale(float value, boolean condition) {
    return condition ? value / this.graphicsContext.scale() : value;
  }

  public void traverseUpward(Consumer<View> handler) {
    this.traverseUpward(handler, true);
  }

  public void traverseUpward(Consumer<View> handler, boolean includeSelf) {
    var view = includeSelf ? this : this.parent;
    while (view != null) {
      handler.accept(view);
      view = view.parent;
    }
  }

  public Optional<View> traverseUpwardUntil(Predicate<View> filter) {
    return this.traverseUpwardUntil(filter, true);
  }

  public Optional<View> traverseUpwardUntil(Predicate<View> filter, boolean includeSelf) {
    var view = includeSelf ? this : this.parent;
    while (view != null) {
      if (filter.test(view)) {
        return Optional.of(view);
      }
      view = view.parent;
    }
    return Optional.empty();
  }

  @Override
  public final int compareTo(View o) {
    return Float.compare(
        this.index + this.style.zIndex.get(),
        o.index + o.style.zIndex.get());
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public Set<String> getStyleClasses() {
    return this.styleClasses;
  }

  @Override
  public final String getType() {
    if (this.getClass().isAnonymousClass()) {
      return this.getClass().getSuperclass().getSimpleName();
    }
    return this.getClass().getSimpleName();
  }

  @Override
  public StyleManager getStyleManager() {
    return this.style.getStyleManager();
  }

  @Override
  public boolean isVisible() {
    return this.added && this.style.visibility.get() == Visibility.VISIBLE;
  }

  @Override
  public StyleList getStyleList() {
    return this.getScreen().getStyleList();
  }

  @Override
  public int getIndex() {
    return this.index;
  }

  public void refreshStyle() {
    this.style.getStyleManager().refresh();
  }

  public ViewStyle getStyle() {
    return this.style;
  }

  public static final class Properties {

    @Nullable
    private String id;
    private final ImmutableSet.Builder<String> styleClasses = ImmutableSet.builder();
    private boolean doubleClick;
    private boolean unscaleWidth;
    private boolean unscaleHeight;
    private boolean unscaleBorder = true;
    private boolean unscaleOutline = true;
    private boolean focusable;

    public Properties id(String id) {
      this.id = id;
      return this;
    }

    public Properties styleClasses(String... styleClasses) {
      this.styleClasses.add(styleClasses);
      return this;
    }

    public Properties styleClasses(Iterable<String> styleClasses) {
      this.styleClasses.addAll(styleClasses);
      return this;
    }

    public Properties doubleClick(boolean doubleClick) {
      this.doubleClick = doubleClick;
      return this;
    }

    public Properties unscaleWidth(boolean unscaleWidth) {
      this.unscaleWidth = unscaleWidth;
      return this;
    }

    public Properties unscaleHeight(boolean unscaleHeight) {
      this.unscaleHeight = unscaleHeight;
      return this;
    }

    public Properties unscaleBorder(boolean unscaleBorder) {
      this.unscaleBorder = unscaleBorder;
      return this;
    }

    public Properties unscaleOutline(boolean unscaleOutline) {
      this.unscaleOutline = unscaleOutline;
      return this;
    }

    public Properties focusable(boolean focusable) {
      this.focusable = focusable;
      return this;
    }
  }
}
