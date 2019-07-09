package com.craftingdead.mod.masterserver.net;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.event.MasterServerEvent;
import com.craftingdead.mod.masterserver.net.protocol.login.message.LoginResultMessage;
import com.craftingdead.network.pipeline.Session;
import com.craftingdead.network.protocol.IMessage;
import com.craftingdead.network.protocol.IProtocol;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;

public class CustomSession extends Session {

  private static final Logger logger = LogManager.getLogger();

  private final Queue<IMessage> messageQueue = new ConcurrentLinkedQueue<>();

  @Getter
  private boolean authenticated;

  private final Supplier<IMessage> loginMessageSupplier;

  public CustomSession(IProtocol<?> protocol, Supplier<IMessage> loginMessageSupplier) {
    super(protocol);
    this.loginMessageSupplier = loginMessageSupplier;
  }

  @Override
  public void sendMessage(IMessage msg) {
    if (this.authenticated) {
      super.sendMessage(msg);
    } else {
      this.messageQueue.add(msg);
    }
  }

  private void dispatchPendingMessages() {
    while (!this.messageQueue.isEmpty()) {
      IMessage msg = this.messageQueue.poll();
      this.sendMessage(msg);
    }
  }

  public void onLoginResult(LoginResultMessage msg) {
    if (msg.isSuccess()) {
      logger.info("Successfully logged in to master server");
      this.authenticated = true;
      this.dispatchPendingMessages();
    } else {
      logger.info("Failed to log in to master server, disconnecting");
      this.closeChannel();
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    logger.info("Connected to master server, sending login message");
    super.sendMessage(this.loginMessageSupplier.get());
    MinecraftForge.EVENT_BUS.post(new MasterServerEvent.ConnectedEvent(this));
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctc) {
    logger.info("Disconnected from master server");
    MinecraftForge.EVENT_BUS.post(new MasterServerEvent.DisconnectedEvent(this));
  }
}
