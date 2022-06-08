package sm0keysa1m0n.bliss.minecraft.platform;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import sm0keysa1m0n.bliss.platform.Platform;

public class MinecraftPlatform implements Platform {

  private final Minecraft minecraft;

  public MinecraftPlatform(Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  @Override
  public CompletableFuture<Void> submitToMainThread(Runnable task) {
    return this.minecraft.submit(task);
  }

  @Override
  public <T> CompletableFuture<T> submitToMainThread(Supplier<T> task) {
    return this.minecraft.submit(task);
  }

  @Override
  public boolean isMainThread() {
    return this.minecraft.isSameThread();
  }

  @Override
  public Executor mainExecutor() {
    return this.minecraft;
  }

  @Override
  public Executor backgroundExecutor() {
    return Util.backgroundExecutor();
  }

  @Override
  public String getClipboard() {
    return this.minecraft.keyboardHandler.getClipboard();
  }

  @Override
  public void setClipboard(String value) {
    this.minecraft.keyboardHandler.setClipboard(value);
  }
}
