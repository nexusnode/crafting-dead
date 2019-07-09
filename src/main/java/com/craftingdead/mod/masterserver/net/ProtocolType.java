package com.craftingdead.mod.masterserver.net;

import com.craftingdead.mod.masterserver.net.protocol.login.LoginProtocol;
import com.craftingdead.network.protocol.IProtocol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProtocolType {

  LOGIN(new LoginProtocol());

  /**
   * Gets a {@link IProtocol} corresponding to this protocol type.
   *
   * @return the {@link IProtocol}
   */
  private final IProtocol<?> protocol;
}
