package sm0keysa1m0n.bliss;

import java.util.ArrayDeque;
import java.util.Deque;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Rect2i;

public class ScissorStack {

  private static final Deque<Rect2i> regionStack = new ArrayDeque<>();

  public static void push(int x, int y, int width, int height) {
    push(new Rect2i(x, y, width, height));
  }

  public static void push(Rect2i rect) {
    var parentRect = peek();
    regionStack.push(rect);
    if (parentRect == null) {
      apply(rect);
      return;
    }

    int x = Math.min(rect.getX(), parentRect.getX());
    int y = Math.max(rect.getY(), parentRect.getY());
    int x2 = Math.min(rect.getX() + rect.getWidth(),
        parentRect.getX() + parentRect.getWidth());
    int y2 = Math.min(rect.getY() + rect.getHeight(),
        parentRect.getY() + parentRect.getHeight());
    RenderSystem.enableScissor(x, y, Math.max(x2 - x, 0), Math.max(y2 - y, 0));
  }

  public static void pop() {
    if (!regionStack.isEmpty()) {
      regionStack.pop();
      if (apply()) {
        return;
      }
    }
    RenderSystem.disableScissor();
  }

  private static void apply(Rect2i rect) {
    RenderSystem.enableScissor(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }

  public static boolean apply() {
    var rect = peek();
    if (rect == null) {
      return false;
    }
    apply(rect);
    return true;
  }

  @Nullable
  public static Rect2i peek() {
    return regionStack.peek();
  }

  public static boolean isEmpty() {
    return regionStack.isEmpty();
  }
}
