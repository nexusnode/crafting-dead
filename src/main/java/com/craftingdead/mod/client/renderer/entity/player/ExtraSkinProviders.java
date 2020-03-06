package com.craftingdead.mod.client.renderer.entity.player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.client.util.RenderUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * A container of {@link IExtraSkinProvider}.
 */
public class ExtraSkinProviders {

  private final List<IExtraSkinProvider> extraSkinProviders = Lists.newArrayList();

  /**
   * Registers an {@link IExtraSkinProvider}.<br>
   * The order of registering matters during render phase.
   */
  public void registerExtraSkinProvider(IExtraSkinProvider provider) {
    this.extraSkinProviders.add(provider);
  }

  /**
   * Gets an unmodifiable view of the current extra skin providers.
   */
  public List<IExtraSkinProvider> getExtraSkinProviders() {
    return Collections.unmodifiableList(this.extraSkinProviders);
  }

  /**
   * Gets all the extra skins that can currently be applied to a single player accordingly to each
   * registered {@link IExtraSkinProvider}.<br>
   *
   * @param player The {@link IPlayer} instance.
   * @param skinType The skin type of the player. In 1.15.2, it may be "default" or "slim".
   * @return A {@link List} of skins from every non-empty output from each {@link IExtraSkinProvider}.
   */
  public List<ResourceLocation> getAvailableExtraSkins(IPlayer<? extends PlayerEntity> player,
      String skinType) {
    return this.extraSkinProviders.stream()

        // Maps every provider to its output.
        .map(provider -> provider.provideResource(player, skinType))

        // Removes every empty output. Empty means there is no skin to be rendered.
        .filter(Optional::isPresent)

        // Gets the value from the Optional wrapper
        .map(Optional::get)

        .collect(Collectors.toList());
  }

  @FunctionalInterface
  public static interface IExtraSkinProvider {
    /**
     * Tells how to provide an {@link IHasExtraSkin} from an {@link IPlayer} instance.
     *
     * @param player The {@link IPlayer} instance.
     * @return Non-empty {@link Optional} containing the {@link IHasExtraSkin} object if there is a
     *         skin to be shown. Empty {@link Optional} otherwise.
     */
    public Optional<IHasExtraSkin> provide(IPlayer<? extends PlayerEntity> player);

    /**
     * Provides a {@link ResourceLocation} using <code>provide()</code> method output.<br>
     * It uses a cache to prevent resource leaking.
     *
     * @return Non-empty {@link Optional} containing the {@link ResourceLocation} object if there is a
     *         skin to be shown. Empty {@link Optional} otherwise.
     */
    public default Optional<ResourceLocation> provideResource(IPlayer<? extends PlayerEntity> player,
        String lowerCaseSkinType) {
      Optional<IHasExtraSkin> extraSkinOptional = this.provide(player);

      return extraSkinOptional.map(extraSkin -> {
        ResourceLocation skinResource =
            RenderUtil.getCachedResource(extraSkin.getExtraSkinLocation(player, lowerCaseSkinType));

        return skinResource;
      });
    }
  }
}
