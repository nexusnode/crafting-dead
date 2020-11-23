/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.craftingdead.immerse.client.util.DownloadUtil;
import com.craftingdead.immerse.client.util.LoggingErrorHandler;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.common.base.Strings;
import com.mojang.datafixers.util.Either;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenType;
import io.noties.tumbleweed.equations.Sine;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public class ContainerComponent extends ParentComponent<ContainerComponent> {

  private static final Logger logger = LogManager.getLogger();

  private static final TweenType<ContainerComponent> SCROLL_OFFSET =
      new SimpleTweenType<>(
          (SimpleTweenType.FloatGetter<ContainerComponent>) c -> (float) c.scrollOffset,
          (SimpleTweenType.FloatSetter<ContainerComponent>) (c, v) -> c.scrollOffset = v);

  private static final int SCROLLBAR_WIDTH = 4;

  private static final float SCROLL_CHUNK = 7.5F;

  private static final float SCROLL_MOMENTUM_DAMPING = 0.75F;

  private float scrollOffset;

  private float totalHeight;

  private boolean draggingScroller;

  private boolean calculateWidthWithScrollbar;

  private float lastScrollMomentum;

  private float scrollMomentum;

  @Override
  public float getContentWidth() {
    return super.getContentWidth() - (this.calculateWidthWithScrollbar ? SCROLLBAR_WIDTH : 0.0F);
  }

  @Override
  public float getContentY() {
    return super.getContentY() + (this.totalHeight > this.getHeight() ? -this.scrollOffset : 0.0F);
  }

  @Override
  public void layout() {
    super.layout();
    this.totalHeight =
        (float) (this.children()
            .stream()
            .mapToDouble(c -> c.getY() + c.getHeight())
            .max()
            .orElse(0.0F)
            - this.children()
                .stream()
                .mapToDouble(Component::getY)
                .min()
                .orElse(0.0F));
    if (this.isScrollbarEnabled() != this.calculateWidthWithScrollbar) {
      this.calculateWidthWithScrollbar = this.isScrollbarEnabled();
      super.layout();
    }
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);
  }

  private final void scrollTo(float y, float duration) {
    Tween.to(this, SCROLL_OFFSET, duration)
        .target(this.clampScrollOffset(y))
        .ease(Sine.OUT)
        .start(this.getTweenManager());
  }

  private final float clampScrollOffset(float scrollOffset) {
    return MathHelper.clamp(scrollOffset, 0.0F, this.totalHeight - this.getHeight());
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    if (this.isScrollbarEnabled() && !this.draggingScroller) {
      this.scrollMomentum -= scrollDelta * SCROLL_CHUNK;
    }
    return super.mouseScrolled(mouseX, mouseY, scrollDelta);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    super.mouseClicked(mouseX, mouseY, button);
    if (this.isScrollbarEnabled() && mouseX >= this.getX() + this.getWidth() - SCROLLBAR_WIDTH
        && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY()
        && mouseY <= this.getY() + this.getHeight()) {
      if (mouseX >= this.getScrollbarX() && mouseX <= this.getScrollbarX() + SCROLLBAR_WIDTH
          && mouseY >= this.getScrollbarY()
          && mouseY <= this.getScrollbarY() + this.getScrollbarHeight()) {
        this.draggingScroller = true;
      } else {
        this.scrollTo(this.scrollOffset
            + (mouseY > this.getScrollbarY() ? this.getHeight() : -this.getHeight()), 200);
      }
    }
    return true;
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    if (this.draggingScroller && mouseY >= this.getY()
        && mouseY <= this.getY() + this.getHeight()) {
      this.scrollOffset = this.clampScrollOffset((float) (this.scrollOffset
          + deltaY * this.totalHeight / (this.getHeight())));
    }
    return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    super.mouseReleased(mouseX, mouseY, button);
    this.draggingScroller = false;
    return true;
  }

  @Override
  public void tick() {
    super.tick();

    this.lastScrollMomentum = this.scrollMomentum;

    if (Math.abs(this.scrollMomentum) <= 0.1F) {
      this.scrollOffset += this.scrollMomentum;
      this.scrollMomentum = 0.0F;
    }
    this.scrollMomentum *= (1 - SCROLL_MOMENTUM_DAMPING);

  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);

    float scrollDelta = MathHelper.lerp(partialTicks, this.lastScrollMomentum, this.scrollMomentum);
    float newScrollOffset = this.scrollOffset + scrollDelta;
    if (newScrollOffset < 0.0F || newScrollOffset > this.totalHeight - this.getHeight()) {
      this.scrollMomentum = this.lastScrollMomentum = 0.0F;
    }
    this.scrollOffset =
        Math.min(Math.max(newScrollOffset, 0.0F), this.totalHeight - this.getHeight());

    if (this.isScrollbarEnabled()) {
      RenderUtil.roundedFill(this.getScrollbarX(), this.getY(),
          this.getScrollbarX() + SCROLLBAR_WIDTH,
          this.getY() + this.getHeight(), 0x40000000, SCROLLBAR_WIDTH / 2.0F);
      RenderUtil.roundedFill(this.getScrollbarX(), this.getScrollbarY(),
          this.getScrollbarX() + SCROLLBAR_WIDTH, this.getScrollbarY() + this.getScrollbarHeight(),
          0x4CFFFFFF, SCROLLBAR_WIDTH / 2.0F);
    }
  }

  protected boolean isScrollbarEnabled() {
    return this.getOverflow() == Overflow.SCROLL && this.totalHeight > this.getHeight();
  }

  @Override
  public void renderChildren(int mouseX, int mouseY, float partialTicks) {
    final double scale = this.minecraft.getMainWindow().getGuiScaleFactor();
    final boolean scissor =
        this.getOverflow() == Overflow.HIDDEN || this.getOverflow() == Overflow.SCROLL;
    if (scissor) {
      GL11.glEnable(GL11.GL_SCISSOR_TEST);
      GL11.glScissor((int) (this.getScaledX() * scale),
          (int) (this.mainWindow.getFramebufferHeight()
              - ((this.getScaledY() + this.getScaledHeight()) * scale)),
          (int) (this.getScaledWidth() * scale), (int) (this.getScaledHeight() * scale));
    }
    super.renderChildren(mouseX, mouseY, partialTicks);
    if (scissor) {
      GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
  }

  private final double getScrollbarX() {
    return this.getX() + this.getWidth() - SCROLLBAR_WIDTH;
  }

  private final double getScrollbarY() {
    return this.getY() + (this.scrollOffset / this.totalHeight) * this.getHeight();
  }

  private final float getScrollbarHeight() {
    return MathHelper.clamp(this.getHeight() * (this.getHeight() / this.totalHeight), 10.0F,
        this.getHeight());
  }

  /**
   * Add all the {@link Component}s specified in the passed {@link File}, spacing them evenly with
   * {@code flex: 1;}
   * 
   * @param file - the {@link File} to read {@link Component}s from
   * @return ourself
   */
  public ContainerComponent addAll(File file) {
    return this.addAll(file, c -> c.setFlex(1.0F));
  }


  /**
   * Add all the {@link Component}s specified in the passed {@link File}.
   * 
   * @param file - the {@link File} to read {@link Component}s from
   * @param configurer - a {@link Consumer} used to configure {@link Component}s before they're
   *        added
   * @return ourself
   */
  public ContainerComponent addAll(File file, Consumer<Component<?>> configurer) {
    DocumentBuilder builder;
    Document document;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setIgnoringElementContentWhitespace(true);
      builder = factory.newDocumentBuilder();
      builder.setErrorHandler(LoggingErrorHandler.INSTANCE);
      document = builder.parse(file);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      logger.warn("Failed to pass xml {} {}", file.getAbsolutePath(), e);
      return this;
    }

    NodeList nodes = document.getDocumentElement().getChildNodes();
    int nextIndex = this.children().size();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      switch (node.getNodeName()) {
        case "text":
          String text = node.getTextContent();
          boolean shadow = true;
          float scale = 1.0F;
          Node scaleNode = node.getAttributes().getNamedItem("scale");
          if (scaleNode != null && scaleNode.getNodeValue() != null) {
            try {
              scale = Float.valueOf(scaleNode.getNodeValue());
            } catch (NumberFormatException | NullPointerException e) {
              logger.warn("Float expected for property 'scale' in {}", file.getAbsolutePath());
            }
          }

          Node shadowNode = node.getAttributes().getNamedItem("shadow");
          if (shadowNode != null && shadowNode.getNodeValue() != null) {
            try {
              shadow = Boolean.valueOf(shadowNode.getNodeValue());
            } catch (NumberFormatException | NullPointerException e) {
              logger.warn("Boolean expected for property 'shadow' in {}", file.getAbsolutePath());
            }
          }

          if (text != null) {
            Component<?> component = new TextBlockComponent(this.minecraft.fontRenderer,
                new StringTextComponent(text), shadow).setScale(scale);
            component.setWidthPercent(100.0F);
            this.addChild(component);
            nextIndex++;
          }
          break;
        case "image":
          final Either<Integer, Float> width;
          final Either<Integer, Float> height;
          String url = null;
          FitType fitType = FitType.FILL;

          Node urlNode = node.getAttributes().getNamedItem("url");
          if (urlNode != null && urlNode.getNodeValue() != null) {
            url = urlNode.getNodeValue();
          } else {
            logger.warn("No URL declared for image in {}", file.getAbsolutePath());
            break;
          }

          Node widthNode = node.getAttributes().getNamedItem("width");
          if (widthNode != null && widthNode.getNodeValue() != null) {
            String widthString = widthNode.getNodeValue();
            width = extractSize(widthString);
            if (width == null) {
              logger.warn("Invalid width '{}' for image in {}", widthString,
                  file.getAbsolutePath());
            }
          } else {
            width = null;
          }

          Node heightNode = node.getAttributes().getNamedItem("height");
          if (heightNode != null && heightNode.getNodeValue() != null) {
            String heightString = heightNode.getNodeValue();
            height = extractSize(heightString);
            if (height == null) {
              logger.warn("Invalid height '{}' for image in {}", heightString,
                  file.getAbsolutePath());
            }
          } else {
            height = null;
          }

          if (width == null && height == null) {
            logger.warn("Both width and height cannot be empty for image in {}",
                file.getAbsolutePath());
            break;
          }

          Node fitNode = node.getAttributes().getNamedItem("fit");
          if (fitNode != null && fitNode.getNodeValue() != null) {
            try {
              fitType = FitType.valueOf(fitNode.getNodeValue());
            } catch (IllegalArgumentException e) {
              logger.warn("Invalid fit type {} in {}", heightNode.getNodeValue(),
                  file.getAbsolutePath());
            }
          }
          final ImageComponent component = new ImageComponent().setFitType(fitType);
          if (width != null) {
            width.ifLeft(component::setWidth).ifRight(component::setWidthPercent);
          }
          if (height != null) {
            height.ifLeft(component::setHeight).ifRight(component::setHeightPercent);
          }
          this.addChild(nextIndex, component);
          DownloadUtil.downloadImageAsTexture(url)
              .thenAcceptAsync(result -> result.ifPresent(image -> {
                component.setImage(image);
                this.layout();
              }), this.minecraft);
          break;
        default:
          break;
      }
    }
    return this;
  }

  private static Either<Integer, Float> extractSize(String size) {
    if (Strings.isNullOrEmpty(size)) {
      return null;
    }
    if (size.contains("%")) {
      String pctString = size.split("%")[0];
      try {
        return Either.right(Float.valueOf(pctString));
      } catch (NumberFormatException e) {
        return null;
      }
    } else if (size.contains("px")) {
      String pxString = size.split("px")[0];
      try {
        return Either.left(Integer.valueOf(pxString));
      } catch (NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }
  }
}
