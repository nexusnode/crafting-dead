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

final class HandlerAdapter extends Handler {

  private final EnterHandler enterHandler;
  private final LeaveHandler exitHandler;

  private HandlerAdapter(Session session, EnterHandler enterHandler, LeaveHandler leaveHandler) {
    super(session);
    this.enterHandler = enterHandler;
    this.exitHandler = leaveHandler;
  }

  @Override
  public boolean onCrossBoundary(LocalPlayer player, Location from, Location to,
      ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited,
      MoveType moveType) {

    var extension = PlayerExtension.getOrThrow(CraftingDeadWorldGuard.toEntity(player));
    this.enterHandler.handleEnter(extension, entered);
    this.exitHandler.handleExit(extension, exited);

    return true;
  }

  static Factory<HandlerAdapter> createFactory(EnterHandler enterHandler,
      LeaveHandler leaveHandler) {
    return new Factory<>() {

      @Override
      public HandlerAdapter create(Session session) {
        return new HandlerAdapter(session, enterHandler, leaveHandler);
      }
    };
  }


  @FunctionalInterface
  interface EnterHandler {

    void handleEnter(PlayerExtension<?> extension, Set<ProtectedRegion> regions);
  }

  @FunctionalInterface
  interface LeaveHandler {

    void handleExit(PlayerExtension<?> extension, Set<ProtectedRegion> regions);
  }
}
