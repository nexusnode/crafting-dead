/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
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

  private final Deque<Pair<View, Runnable>> pendingRemoval = new ArrayDeque<>();

  private LayoutParent layoutParent;

  @Nullable
  private GuiEventListener focusedListener;
  private boolean dragging;

  public ParentView(Properties<?> properties) {
    super(properties);
    this.registerValueProperty(
        StyleableProperty.create("display", Display.class, Display.FLEX, this::setDisplay));
  }

  private void setDisplay(Display display) {
    if (this.layoutParent != null) {
      this.layoutParent.gatherDispatchers(this.getStyle()::removeDispatcher);
      this.layoutParent.gatherListeners(this.stateManager::addListener);
      this.children.forEach(this::clearLayout);
      this.layoutParent.close();
    }
    this.layoutParent = display.createLayoutParent();
    this.layoutParent.gatherDispatchers(this.getStyle()::registerDispatcher);
    this.layoutParent.gatherListeners(this.stateManager::removeListener);
    this.children.forEach(this::setupLayout);
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

    if (view.isAdded()) {
      return;
    }

    this.queueAllForRemoval();
    this.addChild(view);
    if (this.isAdded()) {
      this.layout();
    }
  }

  @ThreadSafe
  public final void addChild(View view) {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(() -> this.addChild(view)).join();
      return;
    }

    if (view.parent != null) {
      return;
    }

    this.updatePendingRemoval();

    view.index = this.children.size();
    this.children.add(view);
    this.sortedChildren = this.children.toArray(new View[0]);
    this.sortChildren();
    view.getStyle().setParent(this);
    view.parent = this;

    if (this.layoutParent != null) {
      this.setupLayout(view);
    }

    if (this.isAdded()) {
      view.added();
    }
  }

  private void setupLayout(View view) {
    view.setLayout(this.layoutParent.addChild(view.index));
  }

  private void clearLayout(View view) {
    this.layoutParent.removeChild(view.getLayout());
    view.setLayout(null);
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
   * Forces a child to be removed.
   * 
   * @param view - child to remove
   * @throws IllegalStateException - if the child is not present
   * @return ourself
   */
  @ThreadSafe
  public final void removeChild(View view) {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(() -> this.removeChild(view)).join();
      return;
    }
    this.assertChildPresent(view);
    this.removed(view);
    this.children.remove(view);
    this.indexAndSortChildren();
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
   * No callback version of {@link #queueChildForRemoval(View, Runnable)}.
   * 
   * @param view - child to remove
   */
  @ThreadSafe
  public final void queueChildForRemoval(View view) {
    this.queueChildForRemoval(view, null);
  }

  /**
   * Queues a child to be removed, this will notify the child and the child will be able to choose
   * when it's actually removed via a callback in {@link View#queueRemoval(Runnable)}. If the child
   * is not present an {@link IllegalArgumentException} will be thrown.
   * 
   * @param view - child to remove
   * @param callback - executed upon removal
   */
  @ThreadSafe
  public final void queueChildForRemoval(View view, Runnable callback) {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(() -> this.queueChildForRemoval(view, callback)).join();
      return;
    }
    this.assertChildPresent(view);
    view.queueRemoval(new Runnable() {

      private boolean removed;

      @Override
      public void run() {
        if (!this.removed) {
          this.removed = true;
          view.pendingRemoval = true;
          ParentView.this.pendingRemoval.add(Pair.of(view, callback));
        } else {
          throw new UnsupportedOperationException("Cannot call remove twice.");
        }
      }
    });
  }

  /**
   * Queues all children for removal.
   * 
   * @see #queueChildForRemoval(View, Runnable)
   * 
   * @return ourself
   */
  @ThreadSafe
  public final void queueAllForRemoval() {
    if (!this.minecraft.isSameThread()) {
      this.minecraft.submit(this::queueAllForRemoval).join();
      return;
    }
    this.children.forEach(this::queueChildForRemoval);
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
    view.getStyle().setParent(null);
    view.parent = null;
    view.getStyle().setStyleSupplier(null);
    view.pendingRemoval = false;
  }

  private final void assertChildPresent(View view) {
    if (this.children.size() <= view.index || this.children.get(view.index) != view) {
      throw new IllegalArgumentException("View not added");
    }
  }

  private void indexAndSortChildren() {
    for (int i = 0; i < this.children.size(); i++) {
      this.children.get(i).index = i;
    }

    this.sortedChildren = this.children.toArray(new View[0]);
    this.sortChildren();
  }

  private void updatePendingRemoval() {
    if (!this.pendingRemoval.isEmpty()) {
      this.pendingRemoval.forEach(pair -> {
        var view = pair.getLeft();
        this.removed(view);
        this.children.remove(view);
        var callback = pair.getRight();
        if (callback != null) {
          callback.run();
        }
      });
      this.pendingRemoval.clear();

      this.indexAndSortChildren();
      if (this.isAdded()) {
        this.layout();
      }
    }
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

    // Do this twice because the content width depends on if a scrollbar exists which depends on the
    // content height etc.

    this.layoutParent.layout(this.getContentWidth(), this.getContentHeight());
    this.children.forEach(View::layout);
    super.layout();

    this.layoutParent.layout(this.getContentWidth(), this.getContentHeight());
    this.children.forEach(View::layout);
    super.layout();
  }

  @Override
  public void tick() {
    this.updatePendingRemoval();
    super.tick();
    this.children.forEach(View::tick);
  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    for (var view : this.sortedChildren) {
      if (!view.pendingRemoval) {
        view.render(matrixStack, mouseX, mouseY, partialTicks);
      }
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
      GuiEventListener eventListener = this.sortedChildren[i];
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
    return ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button)
        || super.mouseClicked(mouseX, mouseY, button);
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
  public void setFocused(@Nullable GuiEventListener focusedListener) {
    this.focusedListener = focusedListener;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    return this.getFocused() != null && this.getFocused().mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public List<StyleHolder> getChildStyles() {
    return this.children.stream().map(View::getStyle).toList();
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
