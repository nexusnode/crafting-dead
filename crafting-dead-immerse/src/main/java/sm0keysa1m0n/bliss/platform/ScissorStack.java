package sm0keysa1m0n.bliss.platform;

import java.util.ArrayDeque;
import java.util.Deque;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.humbleui.types.IRect;

/**
 * Used to track the current clipped region for whenever we need to use GL directly.
 * 
 * @author Sm0keySa1m0n
 */
public class ScissorStack {

  private static final Deque<IRect> regionStack = new ArrayDeque<>();

  public static void push(int x, int y, int width, int height) {
    push(new IRect(x, y, width, height));
  }

  public static void push(IRect rect) {
    var parentRect = peek();
    regionStack.push(parentRect == null ? rect : rect.intersect(parentRect));
  }

  public static void pop() {
    if (!regionStack.isEmpty()) {
      regionStack.pop();
    }
  }

  public static void apply() {
    var rect = peek();
    if (rect == null) {
      return;
    }
    RenderSystem.enableScissor(rect.getLeft(), rect.getTop(),
        Math.max(rect.getRight() - rect.getLeft(), 0),
        Math.max(rect.getBottom() - rect.getTop(), 0));
  }

  @Nullable
  public static IRect peek() {
    return regionStack.peek();
  }

  public static boolean isEmpty() {
    return regionStack.isEmpty();
  }
}
