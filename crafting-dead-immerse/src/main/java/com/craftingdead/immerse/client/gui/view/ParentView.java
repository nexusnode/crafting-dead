/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;
import com.craftingdead.immerse.client.gui.view.style.StyleParent;
import com.craftingdead.immerse.util.ThreadSafe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;

public class ParentView extends View implements ContainerEventHandler, StyleParent {

  private final List<View> children = new ArrayList<>();
  // Bottom to top order
  private View[] sortedChildren = new View[0];

  private LayoutParent layoutParent = LayoutParent.NILL;

  @Nullable
  private GuiEventListener focusedListener;
  private boolean dragging;

  public ParentView(Properties properties) {
    super(properties);

    this.getStyle().display.addListener(this::setDisplay);
    this.setDisplay(this.getStyle().display.get());
  }

  private void setDisplay(Display display) {
    if (this.layoutParent != LayoutParent.NILL) {
      this.children.forEach(this::clearLayout);
      this.layoutParent.close();
    }

    if (display != Display.NONE || this.layoutParent != LayoutParent.NILL) {
      this.layoutParent = display.createLayoutParent();
      this.layoutParent.setAll(this.getStyle());
      this.children.forEach(this::setupLayout);
      if (this.isAdded()) {
        this.layout();
      }
    }
  }

  public final List<? extends View> getChildViews() {
    return this.children;
  }

  public final LayoutParent getLayoutParent() {
    return this.layoutParent;
  }

  @ThreadSafe
  public final void replace(View view) {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(() -> this.replace(view)).join();
      return;
    }

    if (view.hasParent()) {
      return;
    }

    this.clearChildren();
    this.addChild(view);
    if (this.isAdded()) {
      this.layout();
    }
  }

  @ThreadSafe
  public final void forceAddChild(View view) {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(() -> this.forceAddChild(view)).join();
      return;
    }

    if (view.hasParent()) {
      view.getParent().removeChild(view);
    }

    this.addChild(view);
  }

  @ThreadSafe
  public final void addChild(View view) {
    if (view.hasParent()) {
      return;
    }

    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(() -> this.addChild(view)).join();
      return;
    }

    view.index = this.children.size();
    this.children.add(view);
    this.sortedChildren = this.children.toArray(new View[0]);
    this.sortChildren();
    view.getStyleHolder().setParent(this);
    view.parent = this;

    this.setupLayout(view);

    if (this.isAdded()) {
      view.added();
    }
  }

  private void setupLayout(View view) {
    view.setLayout(this.layoutParent.addChild(view.index));
  }

  private void clearLayout(View view) {
    this.layoutParent.removeChild(view.getLayout());
    view.setLayout(Layout.NILL);
  }

  @Override
  protected void added() {
    super.added();
    this.children.forEach(View::added);
  }

  @Override
  protected void removed() {
    super.removed();
    this.children.forEach(View::removed);
  }

  /**
   * Remove a child view.
   * 
   * @param view - child to remove
   * @return <code>true</code> if the child was present
   */
  @ThreadSafe
  public final boolean removeChild(View view) {
    if (view.parent != this) {
      return false;
    }
    if (!this.minecraft.isSameThread()) {
      return this.minecraft.submit(() -> this.removeChild(view)).join();
    }
    this.removed(view);
    if (!this.children.remove(view)) {
      throw new IllegalStateException("Expecting child view to be present");
    }
    this.indexAndSortChildren();
    return true;
  }

  /**
   * Forces all children to be removed.
   * 
   * @return ourself
   */
  @ThreadSafe
  public final void clearChildren() {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(this::clearChildren).join();
      return;
    }
    this.children.forEach(this::removed);
    this.children.clear();
    this.sortedChildren = new View[0];
  }

  /**
   * Performs appropriate logic to remove child but <b>does not actually remove the child from
   * {@link #children}</b>.
   * 
   * @param view - child to remove
   */
  private void removed(View view) {
    view.removed();
    if (this.layoutParent != null) {
      this.clearLayout(view);
    }
    view.getStyleHolder().setParent(null);
    view.parent = null;
    view.getStyleHolder().setStyleSupplier(null);
    if (this.focusedListener == view) {
      this.setFocused(null);
    }
  }

  private void indexAndSortChildren() {
    for (int i = 0; i < this.children.size(); i++) {
      this.children.get(i).index = i;
    }

    this.sortedChildren = this.children.toArray(new View[0]);
    this.sortChildren();
  }

  @Override
  public float computeFullHeight() {
    float minY = 0.0F;
    float maxY = 0.0F;
    for (var child : this.children) {
      float y = child.getY() - this.getScaledContentY();
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y + child.getHeight());
    }

    return Math.max(maxY - minY, 0);
  }

  @ThreadSafe
  @Override
  public void layout() {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(this::layout).join();
      return;
    }

    var wasScrollbarEnabled = this.isScrollbarEnabled();
    this.layoutParent.layout(this.getContentWidth(), this.getContentHeight());
    this.children.forEach(View::layout);
    super.layout();

    // Re-layout to account for scrollbar width.
    if (wasScrollbarEnabled != this.isScrollbarEnabled()) {
      this.layoutParent.layout(this.getContentWidth(), this.getContentHeight());
      this.children.forEach(View::layout);
      super.layout();
    }
  }

  @Override
  public void tick() {
    super.tick();
    this.children.forEach(View::tick);
  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    for (var view : this.sortedChildren) {
      view.render(matrixStack, mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public void close() {
    this.children.forEach(View::close);
    this.layoutParent.close();
    super.close();
  }

  public void sortChildren() {
    Arrays.sort(this.sortedChildren);
  }

  @Override
  public List<? extends GuiEventListener> children() {
    return Arrays.asList(this.sortedChildren);
  }

  @Override
  public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
    for (int i = this.sortedChildren.length; i-- > 0;) {
      var eventListener = this.sortedChildren[i];
      if (eventListener.isMouseOver(mouseX, mouseY)) {
        return Optional.of(eventListener);
      }
    }
    return Optional.empty();
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.children().forEach(listener -> listener.mouseMoved(mouseX, mouseY));
    super.mouseMoved(mouseX, mouseY);
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    for (int i = this.sortedChildren.length; i-- > 0;) {
      if (this.sortedChildren[i].isMouseOver(mouseX, mouseY)) {
        return true;
      }
    }
    return super.isMouseOver(mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }
    this.setFocused(null);
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return ContainerEventHandler.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return ContainerEventHandler.super.mouseScrolled(mouseX, mouseY, scrollDelta)
        || super.mouseScrolled(mouseX, mouseY, scrollDelta);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return ContainerEventHandler.super.keyPressed(keyCode, scanCode, modifiers)
        || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return ContainerEventHandler.super.keyReleased(keyCode, scanCode, modifiers)
        || super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return ContainerEventHandler.super.charTyped(codePoint, modifiers)
        || super.charTyped(codePoint, modifiers);
  }

  @Override
  public boolean isDragging() {
    return this.dragging;
  }

  @Override
  public void setDragging(boolean dragging) {
    this.dragging = dragging;
  }

  @Nullable
  @Override
  public GuiEventListener getFocused() {
    return this.focusedListener;
  }

  @Override
  public boolean changeFocus(boolean focus) {
    return super.changeFocus(focus) || ContainerEventHandler.super.changeFocus(focus);
  }

  @Override
  public void removeFocus() {
    super.removeFocus();
    this.setFocused(null);
  }

  @Override
  public void setFocused(@Nullable GuiEventListener focusedListener) {
    if (this.focusedListener == focusedListener) {
      return;
    }

    if (focusedListener instanceof View view && view.parent != this) {
      return;
    }

    if (this.focusedListener instanceof View view && view.isAdded()) {
      view.removeFocus();
    }

    this.focusedListener = focusedListener;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    return this.getFocused() != null && this.getFocused().mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public List<StyleHolder> getChildStyles() {
    return this.children.stream().map(View::getStyleHolder).toList();
  }

  @Override
  public int getChildCount() {
    return this.children.size();
  }

  @Override
  public void refreshStyle() {
    super.refreshStyle();
    this.children.forEach(View::refreshStyle);
  }
}
