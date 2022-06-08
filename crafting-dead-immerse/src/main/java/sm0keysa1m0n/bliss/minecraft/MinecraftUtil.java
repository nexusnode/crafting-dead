package sm0keysa1m0n.bliss.minecraft;

import java.io.IOException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.svg.SVGDOM;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.view.ImageAccess;
import sm0keysa1m0n.bliss.view.SimpleImageAccess;
import sm0keysa1m0n.bliss.view.SvgImageAccess;

public class MinecraftUtil {

  private static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);

  @SuppressWarnings("resource")
  @Nullable
  public static ImageAccess createImageAccess(ResourceLocation imageLocation) {
    try (var inputStream =
        Minecraft.getInstance().getResourceManager().getResource(imageLocation).getInputStream()) {
      var bytes = inputStream.readAllBytes();
      if (imageLocation.getPath().endsWith(".svg")) {
        return new SvgImageAccess(new SVGDOM(Data.makeFromBytes(bytes)));
      } else {
        return new SimpleImageAccess(Image.makeFromEncoded(bytes));
      }
    } catch (IOException e) {
      logger.warn("Failed to load image: {}", imageLocation);
      return null;
    }
  }
}
