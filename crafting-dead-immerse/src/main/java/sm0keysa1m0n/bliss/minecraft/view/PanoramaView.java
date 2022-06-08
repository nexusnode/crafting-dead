package sm0keysa1m0n.bliss.minecraft.view;

import java.util.Objects;
import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import sm0keysa1m0n.bliss.view.View;

public class PanoramaView extends View {

  private final PanoramaRenderer panorama;

  public PanoramaView(Properties properties, CubeMap cubeMap) {
    super(properties);
    Objects.requireNonNull(cubeMap, "cubeMap cannot be null");
    this.panorama = new PanoramaRenderer(cubeMap);
  }

  @SuppressWarnings("removal")
  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    super.renderContent(poseStack, mouseX, mouseY, partialTick);
    this.graphicsContext.exitManaged();
    {
      if (this.minecraft.level == null) {
        this.panorama.render(partialTick, this.getAlpha());
      } else {
        RenderUtil.fillGradient(poseStack, 0, 0, this.getScaledContentWidth(),
            this.getScaledContentHeight(), 0xA0101010, 0xB0101010);
      }
    }
    this.graphicsContext.enterManaged();
  }
}
