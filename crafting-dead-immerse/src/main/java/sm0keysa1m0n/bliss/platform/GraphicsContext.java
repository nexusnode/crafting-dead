package sm0keysa1m0n.bliss.platform;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Surface;

public interface GraphicsContext {

  int width();

  int height();

  float scale();

  Surface surface();

  Canvas canvas();

  void begin();

  void end();

  void setCursor(Cursor cursor);
}
