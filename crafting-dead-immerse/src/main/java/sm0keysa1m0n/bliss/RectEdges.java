package sm0keysa1m0n.bliss;

import java.util.Iterator;
import com.google.common.collect.Iterators;

public record RectEdges<T> (T top, T right, T bottom, T left) implements Iterable<T> {

  public T at(BoxSide side) {
    return switch (side) {
      case TOP -> top;
      case RIGHT -> right;
      case BOTTOM -> bottom;
      case LEFT -> left;
    };
  }

  @Override
  public Iterator<T> iterator() {
    return Iterators.forArray(this.top, this.right, this.bottom, this.left);
  }
}
