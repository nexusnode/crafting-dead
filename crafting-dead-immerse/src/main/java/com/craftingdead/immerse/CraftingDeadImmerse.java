package com.craftingdead.immerse;

import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.server.ServerDist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;

@Mod(CraftingDeadImmerse.ID)
public class CraftingDeadImmerse {

  public static final String ID = "craftingdeadimmerse";

  public static final String VERSION;

  public static final String DISPLAY_NAME;

  static {
    VERSION = JarVersionLookupHandler
        .getImplementationVersion(CraftingDeadImmerse.class)
        .orElse("[version]");
    DISPLAY_NAME = JarVersionLookupHandler
        .getImplementationTitle(CraftingDeadImmerse.class)
        .orElse("[display_name]");
  }

  /**
   * Singleton.
   */
  private static CraftingDeadImmerse instance;

  /**
   * Mod distribution.
   */
  private final IModDist modDist;

  public CraftingDeadImmerse() {
    instance = this;
    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public static CraftingDeadImmerse getInstance() {
    return instance;
  }
}
