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
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

public class ParentView<SELF extends ParentView<SELF, L, C>, L extends Layout, C extends Layout>
    extends View<SELF, L> implements INestedGuiEventHandler {

  private final List<View<?, C>> children = new ArrayList<>();
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

  @SuppressWarnings("unchecked")
  public final SELF addChild(View<?, C> view) {
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
    view.layout();
    view.added();

    return this.self();
  }

  /**
   * Forces a child to be removed. If the child is not present an {@link IllegalArgumentException}
   * will be thrown.
   * 
   * @param view - child to remove
   * @return ourself
   */
  public final SELF removeChild(View<?, C> view) {
    this.assertChildPresent(view);
    this.prepareForRemoval(view);
    this.children.remove(view);
    this.indexAndSortChildren();
    return this.self();
  }

  /**
   * Forces all children to be removed.
   * 
   * @return ourself
   */
  @SuppressWarnings("unchecked")
  public final SELF clearChildren() {
    this.children.forEach(this::prepareForRemoval);
    this.children.clear();
    this.sortedChildren = new View[0];
    return this.self();
  }

  /**
   * No callback version of {@link #queueChildForRemoval(View, Runnable)}.
   * 
   * @param view - child to remove
   */
  public final void queueChildForRemoval(View<?, C> view) {
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
  public final void queueChildForRemoval(View<?, C> view, Runnable callback) {
    this.assertChildPresent(view);
    view.queueRemoval(() -> {
      view.pendingRemoval = true;
      this.pendingRemoval.add(Pair.of(view, callback));
    });
  }

  /**
   * Queues all children for removal.
   * 
   * @see #queueChildForRemoval(View, Runnable)
   * 
   * @return ourself
   */
  public final SELF queueAllForRemoval() {
    this.children.forEach(this::queueChildForRemoval);
    return this.self();
  }

  /**
   * Performs appropriate logic to remove child but <b>does not actually remove the child from
   * {@link #children}</b>.
   * 
   * @param view - child to remove
   */
  private void prepareForRemoval(View<?, C> view) {
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
        this.prepareForRemoval(view);
        this.children.remove(view);
        Runnable callback = pair.getRight();
        if (callback != null) {
          callback.run();
        }
      });
      this.pendingRemoval.clear();

      this.indexAndSortChildren();
    }
  }

  @Override
  public float computeFullHeight() {
    final float height = (float) (this.children
        .stream()
        .mapToDouble(c -> c.getY() + c.getHeight()) // TODO + c.getBottomMargin()
        .max()
        .orElse(0.0F)
        - super.getScaledContentY());

    return Math.max(height, 0);
  }

  @Override
  public void layout() {
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
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
    return Lists.reverse(Arrays.asList(this.sortedChildren));
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.children().forEach(listener -> listener.mouseMoved(mouseX, mouseY));
    super.mouseMoved(mouseX, mouseY);
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    for (IGuiEventListener eventListener : this.children()) {
      if (eventListener.isMouseOver(mouseX, mouseY)) {
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
