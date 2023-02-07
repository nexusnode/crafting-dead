package sm0keysa1m0n.bliss.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.MutableServerListView;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec2;
import sm0keysa1m0n.bliss.Bliss;
import sm0keysa1m0n.bliss.Display;
import sm0keysa1m0n.bliss.ThreadSafe;
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
  View focusedView = null;

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

  public final List<? extends View> getChildren() {
    return this.children;
  }

  public final LayoutParent getLayoutParent() {
    return this.layoutParent;
  }

  @ThreadSafe
  public final void replace(View view) {
    if (!Bliss.instance().platform().joinMainThread(() -> this.replace(view))) {
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
    if (!Bliss.instance().platform().joinMainThread(() -> this.forceAddChild(view))) {
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

    if (!Bliss.instance().platform().joinMainThread(() -> this.addChild(view))) {
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

    if (!Bliss.instance().platform().isMainThread()) {
      return Bliss.instance().platform().submitToMainThread(() -> this.removeChild(view)).join();
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
    if (!Bliss.instance().platform().joinMainThread(this::clearChildren)) {
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
      this.focusedView = null;
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
    if (!Bliss.instance().platform().joinMainThread(this::layout)) {
      return;
    }

    if (!this.isAdded()) {
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

  public Optional<View> traverseSortedDepthFirst(Predicate<View> filter, Consumer<View> handler) {
    for (int i = this.sortedChildren.length; i-- > 0;) {
      var view = this.sortedChildren[i];
      if (view instanceof ParentView parent) {
        var result = parent.traverseSortedDepthFirst(filter, handler);
        if (result.isPresent()) {
          handler.accept(this);
          return result;
        }
      }

      if (filter.test(view)) {
        handler.accept(view);
        handler.accept(this);
        return Optional.of(view);
      }
    }

    if (filter.test(this)) {
      handler.accept(this);
      return Optional.of(this);
    }

    return Optional.empty();
  }

  @Override
  public Optional<View> changeFocus(boolean forward) {
    return super.changeFocus(forward).or(() -> this.nextFocusedChild(forward));
  }

  protected Optional<View> nextFocusedChild(boolean forward) {
    if (this.focusedView != null) {
      var result = this.focusedView.changeFocus(forward);
      if (result.isPresent()) {
        return result;
      }
    }

    int fromIndex;
    if (this.focusedView != null) {
      fromIndex = this.focusedView.index + (forward ? 1 : 0);
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
      var result = nextView.changeFocus(forward);
      if (result.isPresent()) {
        this.focusedView = nextView;
        return result;
      }
    }

    this.focusedView = null;

    return Optional.empty();
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
