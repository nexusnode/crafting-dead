package sm0keysa1m0n.bliss.platform;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
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

  // Note: OpenGL uses left-bottom coordinate, manual conversion from left-top coordinate required
  public static void push(int x, int y, int width, int height) {
    push(IRect.makeXYWH(x, y, width, height));
  }

  public static void push(IRect rect) {
    var parentRect = peek();
    regionStack.push(
        Objects.requireNonNullElse(parentRect == null ? rect : rect.intersect(parentRect), rect));
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
        rect.getWidth(), rect.getHeight());
  }

  @Nullable
  public static IRect peek() {
    return regionStack.peek();
  }

  public static boolean isEmpty() {
    return regionStack.isEmpty();
  }
}
