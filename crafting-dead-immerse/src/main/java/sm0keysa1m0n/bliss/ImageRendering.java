package sm0keysa1m0n.bliss;

import io.github.humbleui.skija.FilterMipmap;
import io.github.humbleui.skija.FilterMode;
import io.github.humbleui.skija.MipmapMode;
import io.github.humbleui.skija.SamplingMode;

public enum ImageRendering {

  SMOOTH(new FilterMipmap(FilterMode.LINEAR, MipmapMode.LINEAR)),
  HIGH_QUALITY(SMOOTH.getSamplingMode()),
  AUTO(HIGH_QUALITY.getSamplingMode()),
  CRISP_EDGES(new FilterMipmap(FilterMode.NEAREST, MipmapMode.NEAREST)),
  PIXELATED(SamplingMode.DEFAULT);

  private final SamplingMode samplingMode;

  private ImageRendering(SamplingMode samplingMode) {
    this.samplingMode = samplingMode;
  }

  public SamplingMode getSamplingMode() {
    return this.samplingMode;
  }
}
