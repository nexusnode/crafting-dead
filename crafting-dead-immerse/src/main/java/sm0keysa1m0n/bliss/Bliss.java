package sm0keysa1m0n.bliss;

import sm0keysa1m0n.bliss.platform.GraphicsContext;
import sm0keysa1m0n.bliss.platform.Platform;

public class Bliss {

  private static Bliss instance;

  private final GraphicsContext graphicsContext;
  private final Platform platform;

  private Bliss(GraphicsContext graphicsContext, Platform platform) {
    this.graphicsContext = graphicsContext;
    this.platform = platform;
  }

  public GraphicsContext getGraphicsContext() {
    return this.graphicsContext;
  }

  public Platform platform() {
    return this.platform;
  }

  public static Bliss instance() {
    return instance;
  }

  public static Bliss initialize(GraphicsContext graphicsContext, Platform platform) {
    if (instance != null) {
      throw new IllegalStateException("Bliss is already initialized.");
    }
    return instance = new Bliss(graphicsContext, platform);
  }
}
