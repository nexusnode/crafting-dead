package com.craftingdead.mod.util;

import java.util.function.Predicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.server.ServerWorld;

public class ParticleUtil {

  /**
   * Sends a particle to all the players near the XYZ location that are valid under a predicate. The
   * view range of the particle is defined by the vanilla code, so you don't need to care about it.
   *
   * @param playerPredicate - Predicate that checks if a player can see the particle.
   */
  public static <T extends IParticleData> void spawnParticleServerside(ServerWorld world, T particle,
      double x, double y, double z, int amount, double offsetX, double offsetY, double offsetZ, double speed,
      Predicate<ServerPlayerEntity> playerPredicate) {
    world.getPlayers().stream().filter(playerPredicate).forEach(player -> {
      world.spawnParticle(player, particle, false, x, y, z, amount, offsetX, offsetY, offsetZ, speed);
    });
 }
}
