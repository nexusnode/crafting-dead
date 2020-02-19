package com.craftingdead.mod.client.renderer.texture;

import java.util.Map;
import java.util.stream.Stream;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.item.PaintItem;
import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintSpriteUploader extends SpriteUploader {

  public static final ResourceLocation ATLAS_LOCATION =
      new ResourceLocation(CraftingDead.ID, "textures/atlas/paints.png");

  public PaintSpriteUploader(TextureManager textureManager) {
    super(textureManager, ATLAS_LOCATION, "paint");
  }

  @Override
  protected Stream<ResourceLocation> getSprites() {
    return ForgeRegistries.ITEMS
        .getEntries()
        .stream()
        .filter(entry -> entry.getValue() instanceof PaintItem)
        .map(Map.Entry::getKey);
  }


  public TextureAtlasSprite getSprite(PaintItem paint) {
    return this.getSprite(paint.getRegistryName());
  }
}
