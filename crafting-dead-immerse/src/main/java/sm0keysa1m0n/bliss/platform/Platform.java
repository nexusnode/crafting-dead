package sm0keysa1m0n.bliss.platform;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public interface Platform {

  default boolean joinMainThread(Runnable task) {
    if (this.isMainThread()) {
      return true;
    }
    this.submitToMainThread(task).join();
    return false;
  }

  CompletableFuture<Void> submitToMainThread(Runnable task);

  <T> CompletableFuture<T> submitToMainThread(Supplier<T> task);

  boolean isMainThread();

  Executor mainExecutor();

  Executor backgroundExecutor();

  String getClipboard();

  void setClipboard(String value);
}
