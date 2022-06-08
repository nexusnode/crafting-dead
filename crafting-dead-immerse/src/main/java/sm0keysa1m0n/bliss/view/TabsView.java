package sm0keysa1m0n.bliss.view;

import java.util.function.Consumer;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.types.Rect;
import net.minecraft.network.chat.FormattedText;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.view.event.ActionEvent;

public class TabsView extends ParentView {

  private TabView selectedTab = null;

  public TabsView(Properties properties) {
    super(properties);
  }

  public TabsView addTab(Runnable selectedListener, FormattedText text) {
    return this.addTab(selectedListener, text, __ -> {});
  }

  public TabsView addTab(Runnable selectedListener, FormattedText text,
      Consumer<TabView> customizer) {
    var view = new TabView(selectedListener);
    view.addChild(new TextView(new Properties()).setText(text));
    view.addListener(ActionEvent.class, event -> this.selectTab(view));
    customizer.accept(view);
    this.addChild(view);
    return this;
  }

  @Override
  protected void added() {
    super.added();
    if (this.getChildren().size() > 0) {
      this.selectTab((TabView) this.getChildren().get(0));
    }
  }

  private void selectTab(TabView newTab) {
    var previousTab = this.selectedTab;
    this.selectedTab = newTab;
    if (previousTab != newTab) {
      newTab.selectedListener.run();
    }
  }

  public class TabView extends ParentView {

    private static final float UNDERSCORE_HEIGHT = 2.5F;
    private static final float HOVERED_UNDERSCORE_HEIGHT = UNDERSCORE_HEIGHT / 1.5F;

    private static final Color underscoreColor = Color.WHITE;

    private final Runnable selectedListener;

    public TabView(Runnable selectedListener) {
      super(new Properties().focusable(true));
      this.selectedListener = selectedListener;
    }

    @Override
    public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
      super.renderContent(poseStack, mouseX, mouseY, partialTick);

      var canvas = this.graphicsContext.canvas();
      var scale = this.graphicsContext.scale();
      if (TabsView.this.selectedTab == this) {
        try (var paint = new Paint()) {
          paint.setColor(underscoreColor.multiplied(this.getAlpha()));
          paint.setMode(PaintMode.FILL);
          canvas.drawRect(Rect.makeLTRB(this.getScaledContentX() * scale,
              (this.getScaledContentY() + this.getScaledContentHeight()
                  - UNDERSCORE_HEIGHT) * scale,
              (this.getScaledContentX() + this.getScaledContentWidth()) * scale,
              (this.getScaledContentY() + this.getScaledContentHeight()) * scale), paint);
        }
      } else if (this.isHovered()) {
        try (var paint = new Paint()) {
          paint.setColor(underscoreColor.multiplied(this.getAlpha()));
          paint.setMode(PaintMode.FILL);
          canvas.drawRect(Rect.makeLTRB(this.getScaledContentX() * scale,
              (this.getScaledContentY() + this.getScaledContentHeight()
                  - HOVERED_UNDERSCORE_HEIGHT) * scale,
              (this.getScaledContentX() + this.getScaledContentWidth()) * scale,
              (this.getScaledContentY() + this.getScaledContentHeight()) * scale), paint);
        }
      }
    }
  }
}
