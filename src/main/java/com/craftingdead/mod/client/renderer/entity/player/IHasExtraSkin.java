package com.craftingdead.mod.client.renderer.entity.player;

import com.craftingdead.mod.capability.player.IPlayer;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Anything that carries an extra skin that could be applied above a player's skin.
 */
public interface IHasExtraSkin {
  /**
   * Please read {@link IHasExtraSkin}.<br>
   *
   * This method gets the texture location to be used in a {@link ResourceLocation} and rendered in a
   * player's body. The texture must have a size that is compatible with a player skin.
   *
   * @param player The {@link IPlayer} instance, if needed. Can be used to return different textures
   *        depending on something from {@link IPlayer}.
   * @param lowerCaseSkinType The lower case skin type. In 1.15.2, it may be "default" or "slim".
   * @return The location of the texture in CraftingDead assets. For example:
   *         "textures/models/clothing/combat_bdu_clothing.png".
   */
  public String getExtraSkinLocation(IPlayer<? extends PlayerEntity> player,
      String lowerCaseSkinType);
}
