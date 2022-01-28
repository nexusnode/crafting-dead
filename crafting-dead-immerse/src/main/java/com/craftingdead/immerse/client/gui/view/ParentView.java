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
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.util.ThreadSafe;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

public class ParentView<SELF extends ParentView<SELF, L, C>, L extends Layout, C extends Layout>
    extends View<SELF, L> implements INestedGuiEventHandler {

  private final List<View<?, C>> children = new ArrayList<>();
  // Bottom to top order
  @SuppressWarnings("unchecked")
  private View<?, C>[] sortedChildren = new View[0];

  private final Deque<Pair<View<?, C>, Runnable>> pendingRemoval = new ArrayDeque<>();

  private final LayoutParent<C> layoutParent;

  @Nullable
  private IGuiEventListener focusedListener;
  private boolean dragging;

  public ParentView(L layout, LayoutParent<C> layoutParent) {
    super(layout);
    this.layoutParent = layoutParent;
  }

  public final List<? extends View<?, C>> getChildViews() {
    return this.children;
  }

  public final LayoutParent<C> getLayoutParent() {
    return this.layoutParent;
  }

  @Override
  public SELF setVisible(boolean visible) {
    super.setVisible(visible);
    this.children.forEach(child -> child.setVisible(visible));
    return this.self();
  }

  @ThreadSafe
  @SuppressWarnings("unchecked")
  public final SELF addChild(View<?, C> view) {
    if (!this.minecraft.isSameThread()) {
      return this.minecraft.submit(() -> this.addChild(view)).join();
    }

    if (view.parent != null) {
      return this.self();
    }

    this.updatePendingRemoval();

    view.index = this.children.size();
    this.children.add(view);
    this.sortedChildren = this.children.toArray(new View[0]);
    this.sortChildren();

    this.layoutParent.addChild(view.getLayout(), view.index);
    view.parent = this;
    view.setVisible(this.visible);
    view.layout();
    view.added();

    return this.self();
  }

  /**
   * Forces a child to be removed.
   * 
   * @param view - child to remove
   * @throws IllegalStateException - if the child is not present
   * @return ourself
   */
  @ThreadSafe
  public final SELF removeChild(View<?, C> view) {
    if (!this.minecraft.isSameThread()) {
      return this.minecraft.submit(() -> this.removeChild(view)).join();
    }
    this.assertChildPresent(view);
    this.removed(view);
    this.children.remove(view);
    this.indexAndSortChildren();
    return this.self();
  }

  /**
   * Forces all children to be removed.
   * 
   * @return ourself
   */
  @ThreadSafe
  @SuppressWarnings("unchecked")
  public final SELF clearChildren() {
    if (!this.minecraft.isSameThread()) {
      return this.minecraft.submit(this::clearChildren).join();
    }
    this.children.forEach(this::removed);
    this.children.clear();
    this.sortedChildren = new View[0];
    return this.self();
  }

  /**
   * No callback version of {@link #queueChildForRemoval(View, Runnable)}.
   * 
   * @param view - child to remove
   */
  @ThreadSafe
  public final SELF queueChildForRemoval(View<?, C> view) {
    return this.queueChildForRemoval(view, null);
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
  public final SELF queueChildForRemoval(View<?, C> view, Runnable callback) {
    if (!this.minecraft.isSameThread()) {
      return this.minecraft.submit(() -> this.queueChildForRemoval(view, callback)).join();
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
    return this.self();
  }

  /**
   * Queues all children for removal.
   * 
   * @see #queueChildForRemoval(View, Runnable)
   * 
   * @return ourself
   */
  @ThreadSafe
  public final SELF queueAllForRemoval() {
    if (!this.minecraft.isSameThread()) {
      return this.minecraft.submit(this::queueAllForRemoval).join();
    }
    this.children.forEach(this::queueChildForRemoval);
    return this.self();
  }

  /**
   * Performs appropriate logic to remove child but <b>does not actually remove the child from
   * {@link #children}</b>.
   * 
   * @param view - child to remove
   */
  private void removed(View<?, C> view) {
    view.removed();
    view.parent = null;
    this.layoutParent.removeChild(view.getLayout());
    view.pendingRemoval = false;
  }

  private final void assertChildPresent(View<?, C> view) {
    if (this.children.size() <= view.index || this.children.get(view.index) != view) {
      throw new IllegalArgumentException("View not added");
    }
  }

  @SuppressWarnings("unchecked")
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
        View<?, C> view = pair.getLeft();
        this.removed(view);
        this.children.remove(view);
        Runnable callback = pair.getRight();
        if (callback != null) {
          callback.run();
        }
      });
      this.pendingRemoval.clear();

      this.indexAndSortChildren();
      this.layout();
    }
  }

  @Override
  public float computeFullHeight() {
    float minY = 0.0F;
    float maxY = 0.0F;
    for (View<?, ?> child : this.children) {
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
  protected void renderContent(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    for (View<?, ?> view : this.sortedChildren) {
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
  public List<? extends IGuiEventListener> children() {
    return Arrays.asList(this.sortedChildren);
  }

  @Override
  public Optional<IGuiEventListener> getChildAt(double mouseX, double mouseY) {
    for (int i = this.sortedChildren.length; i-- > 0;) {
      IGuiEventListener eventListener = this.sortedChildren[i];
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
    return INestedGuiEventHandler.super.mouseClicked(mouseX, mouseY, button)
        || super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return INestedGuiEventHandler.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return INestedGuiEventHandler.super.mouseScrolled(mouseX, mouseY, scrollDelta)
        || super.mouseScrolled(mouseX, mouseY, scrollDelta);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return INestedGuiEventHandler.super.keyPressed(keyCode, scanCode, modifiers)
        || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return INestedGuiEventHandler.super.keyReleased(keyCode, scanCode, modifiers)
        || super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return INestedGuiEventHandler.super.charTyped(codePoint, modifiers)
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
  public IGuiEventListener getFocused() {
    return this.focusedListener;
  }

  @Override
  public boolean changeFocus(boolean focus) {
    return super.changeFocus(focus) || INestedGuiEventHandler.super.changeFocus(focus);
  }

  @Override
  public void setFocused(@Nullable IGuiEventListener focusedListener) {
    this.focusedListener = focusedListener;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    return this.getFocused() != null && this.getFocused().mouseReleased(mouseX, mouseY, button);
  }
}
