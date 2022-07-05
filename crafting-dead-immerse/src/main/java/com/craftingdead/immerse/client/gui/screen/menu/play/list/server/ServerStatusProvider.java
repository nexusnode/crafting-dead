package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import net.minecraft.network.protocol.status.ServerStatus;
import reactor.core.publisher.Mono;

public interface ServerStatusProvider {

  Mono<Result> checkStatus(ServerEntry entry);

  record Result(long responseTimeMs, ServerStatus serverStatus) {}
}
