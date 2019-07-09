package com.craftingdead.mod.event;

import com.craftingdead.network.pipeline.Session;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.eventbus.api.Event;

@Getter
@RequiredArgsConstructor
public class MasterServerEvent extends Event {

  private final Session session;

  public static class ConnectedEvent extends MasterServerEvent {

    public ConnectedEvent(Session session) {
      super(session);
    }
  }

  @Getter
  public static class LoginResultEvent extends MasterServerEvent {

    private final boolean success;

    public LoginResultEvent(Session session, boolean success) {
      super(session);
      this.success = success;
    }
  }

  public static class DisconnectedEvent extends MasterServerEvent {

    public DisconnectedEvent(Session session) {
      super(session);
    }
  }
}
