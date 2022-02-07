package com.craftingdead.immerse.client.gui.view.style.adapter;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationTranslator
    implements StyleDecoder<ResourceLocation>, StyleEncoder<ResourceLocation>,
    StyleValidator<ResourceLocation> {

  @Override
  public ResourceLocation decode(String style) {
    var pathWithNamespace = style.split("\\(");
    var namespace = pathWithNamespace[0].trim();
    var path = pathWithNamespace[1].replace(")", "").replace("\"", "");
    return new ResourceLocation(namespace, path);
  }

  @Override
  public String encode(ResourceLocation value, boolean prettyPrint) {
    return value.getPath();
  }

  @Override
  public int validate(String style) {
    return style.substring(0, style.indexOf(')') + 1).length();
  }
}
