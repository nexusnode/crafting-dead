package sm0keysa1m0n.bliss;

import java.util.function.Supplier;
import sm0keysa1m0n.bliss.layout.LayoutParent;
import sm0keysa1m0n.bliss.layout.yoga.YogaLayoutParent;

public enum Display {

  NONE(() -> LayoutParent.NILL), FLEX(YogaLayoutParent::new);

  private final Supplier<LayoutParent> layoutParentFactory;

  private Display(Supplier<LayoutParent> layoutParentFactory) {
    this.layoutParentFactory = layoutParentFactory;
  }

  public LayoutParent createLayoutParent() {
    return this.layoutParentFactory.get();
  }
}
