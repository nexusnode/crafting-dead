package sm0keysa1m0n.bliss.style;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import io.github.humbleui.skija.FontMgr;

public interface StyleNode {

  default void styleRefreshed(FontMgr fontManager) {};

  String getType();

  @Nullable
  String getId();

  Set<String> getStyleClasses();

  StyleManager getStyleManager();

  boolean isVisible();

  @Nullable
  StyleNode getParent();

  StyleList getStyleList();

  int getIndex();

  default List<? extends StyleNode> getChildStyles() {
    return Collections.emptyList();
  }
}
