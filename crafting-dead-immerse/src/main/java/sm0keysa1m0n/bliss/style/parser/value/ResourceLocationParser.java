package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class ResourceLocationParser {

  @Nullable
  public static ResourceLocation parse(StyleReader reader) throws ParserException {
    var start = reader.getCursor();

    while (reader.canRead() && ResourceLocation.isAllowedInResourceLocation(reader.peek())) {
      reader.skip();
    }

    if (start == reader.getCursor()) {
      return null;
    }

    var value = reader.getString().substring(start, reader.getCursor());
    try {
      return new ResourceLocation(value);
    } catch (ResourceLocationException e) {
      throw new ParserException("Invalid resource location: " + value);
    }
  }
}
