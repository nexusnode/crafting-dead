package sm0keysa1m0n.bliss.view;

import java.util.function.Consumer;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
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
  public boolean changeFocus(boolean forward) {
    return super.changeFocus(forward);
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

    public static final Color DEFAULT_UNDERSCORE_COLOR = Color.WHITE;
    public static final float DEFAULT_UNDERSCORE_HEIGHT = 2.5F;
    public static final boolean DEFAULT_DISABLED = false;

    private final Runnable selectedListener;

    public TabView(Runnable selectedListener) {
      super(new Properties().focusable(true));
      this.selectedListener = selectedListener;
    }

    @Override
    public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      if (TabsView.this.selectedTab == this) {
        RenderUtil.fill(matrixStack, this.getScaledContentX(),
            this.getScaledContentY() + this.getScaledContentHeight() - DEFAULT_UNDERSCORE_HEIGHT,
            this.getScaledContentX() + this.getScaledContentWidth(),
            this.getScaledContentY() + this.getScaledContentHeight(),
            DEFAULT_UNDERSCORE_COLOR.valueHex());
      } else if (this.isHovered()) {
        RenderUtil.fill(matrixStack, this.getScaledContentX(),
            this.getScaledContentY() + this.getScaledContentHeight()
                - DEFAULT_UNDERSCORE_HEIGHT / 1.5F,
            this.getScaledContentX() + this.getScaledContentWidth(),
            this.getScaledContentY() + this.getScaledContentHeight(),
            DEFAULT_UNDERSCORE_COLOR.valueHex());
      }
    }
  }
}
