package com.craftingdead.worldguard;

import java.util.Set;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;

public class ClearEquipmentOnExitFlag extends Handler {

  public static final Factory FACTORY = new Factory();

  public static class Factory extends Handler.Factory<ClearEquipmentOnExitFlag> {
    @Override
    public ClearEquipmentOnExitFlag create(Session session) {
      return new ClearEquipmentOnExitFlag(session);
    }
  }

  protected ClearEquipmentOnExitFlag(Session session) {
    super(session);
  }

  @Override
  public boolean onCrossBoundary(LocalPlayer player, Location from, Location to,
      ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited,
      MoveType moveType) {
    PlayerExtension.getOrThrow(CraftingDeadWorldGuard.toEntity(player))
        .clearEquipment();
    return true;
  }
}
