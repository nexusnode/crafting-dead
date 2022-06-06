package sm0keysa1m0n.bliss.style.parser.value;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationParser implements ValueParser<ResourceLocation> {

  public static final ResourceLocationParser INSTANCE = new ResourceLocationParser();

  private ResourceLocationParser() {}

  @Override
  public int validate(String style) {
    return style.substring(0, style.indexOf(')') + 1).length();
  }

  @Override
  public ResourceLocation parse(String style) {
    var pathWithNamespace = style.split("\\(");
    var namespace = pathWithNamespace[0].trim();
    var path = pathWithNamespace[1].replace(")", "").replace("\"", "");
    return new ResourceLocation(namespace, path);
  }
}
