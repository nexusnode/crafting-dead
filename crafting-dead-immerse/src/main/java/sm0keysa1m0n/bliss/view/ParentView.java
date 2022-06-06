package sm0keysa1m0n.bliss.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.util.ThreadSafe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec2;
import sm0keysa1m0n.bliss.Display;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.layout.LayoutParent;
import sm0keysa1m0n.bliss.layout.MeasureMode;
import sm0keysa1m0n.bliss.style.StyleNode;

public class ParentView extends View {

  private final List<View> children = new ArrayList<>();
  // Bottom to top order
  private View[] sortedChildren = new View[0];

  private LayoutParent layoutParent = LayoutParent.NILL;

  @Nullable
  private View focusedView;
  private boolean dragging;

  public ParentView(Properties properties) {
    super(properties);

    this.getStyle().display.addListener(this::setDisplay);
    this.setDisplay(this.getStyle().display.get());
  }

  @Override
  protected void setLayout(Layout layout) {
    super.setLayout(layout);
    layout.setMeasureFunction(this::measure);
  }

  private Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height) {
    this.layoutParent.layout(
        widthMode == MeasureMode.UNDEFINED || widthMode == MeasureMode.AT_MOST
            ? Float.NaN
            : width,
        heightMode == MeasureMode.UNDEFINED || heightMode == MeasureMode.AT_MOST
            ? Float.NaN
            : height);

    var measuredWidth = this.layoutParent.getContentWidth();
    var measuredHeight = this.layoutParent.getContentHeight();
    return new Vec2(
        widthMode == MeasureMode.AT_MOST ? Math.min(measuredWidth, width) : measuredWidth,
        heightMode == MeasureMode.AT_MOST ? Math.min(measuredHeight, height) : measuredHeight);
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
    view.parent = null;
    if (this.focusedView == view) {
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
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    super.renderContent(poseStack, mouseX, mouseY, partialTick);
    for (var view : this.sortedChildren) {
      view.render(poseStack, mouseX, mouseY, partialTick);
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

  public View hover(double mouseX, double mouseY, Consumer<View> hoveredConsumer) {
    for (int i = this.sortedChildren.length; i-- > 0;) {
      var view = this.sortedChildren[i];
      if (view instanceof ParentView parent) {
        var hovered = parent.hover(mouseX, mouseY, hoveredConsumer);
        if (hovered != null) {
          hoveredConsumer.accept(view);
          return hovered;
        }
      }

      if (view.isMouseOver(mouseX, mouseY)) {
        hoveredConsumer.accept(view);
        return view;
      }
    }
    return null;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.getChildViews().forEach(listener -> listener.mouseMoved(mouseX, mouseY));
    super.mouseMoved(mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (var view : this.sortedChildren) {
      if (view.mouseClicked(mouseX, mouseY, button)) {
        this.setFocused(view);
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
          this.setDragging(true);
        }
        return true;
      }
    }
    this.setFocused(null);
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return this.focusedView != null
        && this.isDragging()
        && button == GLFW.GLFW_MOUSE_BUTTON_LEFT
        && this.focusedView.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return this.focusedView != null && this.focusedView.keyPressed(keyCode, scanCode, modifiers)
        || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return this.focusedView != null && this.focusedView.keyReleased(keyCode, scanCode, modifiers)
        || super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return this.focusedView != null && this.focusedView.charTyped(codePoint, modifiers)
        || super.charTyped(codePoint, modifiers);
  }

  private boolean isDragging() {
    return this.dragging;
  }

  private void setDragging(boolean dragging) {
    this.dragging = dragging;
  }

  @Override
  public boolean changeFocus(boolean forward) {
    if (super.changeFocus(forward)) {
      return true;
    }

    if (this.focusedView != null && this.focusedView.changeFocus(forward)) {
      return true;
    }

    int focusedIndex = this.children.indexOf(this.focusedView);
    int fromIndex;
    if (this.focusedView != null && focusedIndex >= 0) {
      fromIndex = focusedIndex + (forward ? 1 : 0);
    } else if (forward) {
      fromIndex = 0;
    } else {
      fromIndex = this.children.size();
    }

    var iterator = this.children.listIterator(fromIndex);
    BooleanSupplier hasNext = forward ? iterator::hasNext : iterator::hasPrevious;
    Supplier<View> next = forward ? iterator::next : iterator::previous;

    while (hasNext.getAsBoolean()) {
      var nextView = next.get();
      if (nextView.changeFocus(forward)) {
        this.setFocused(nextView);
        return true;
      }
    }

    this.setFocused(null);
    return false;
  }

  @Override
  public void removeFocus() {
    super.removeFocus();
    this.setFocused(null);
  }

  private void setFocused(@Nullable View view) {
    if (this.focusedView == view) {
      return;
    }

    if (view != null) {
      if (view.parent != this) {
        return;
      }
    }

    if (this.focusedView != null && this.focusedView.isAdded()) {
      this.focusedView.removeFocus();
    }

    this.focusedView = view;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    return this.focusedView != null && this.focusedView.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public List<? extends StyleNode> getChildStyles() {
    return this.children;
  }

  @Override
  public void refreshStyle() {
    super.refreshStyle();
    this.children.forEach(View::refreshStyle);
  }
}
