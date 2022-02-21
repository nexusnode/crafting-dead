package com.craftingdead.immerse.util;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class SplittingExecutor implements Executor {

  private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

  public void tick() {
    var task = this.tasks.poll();
    if (task == null) {
      return;
    }
    task.run();
  }

  @Override
  public void execute(Runnable command) {
    this.tasks.add(command);
  }

  public <T> CompletionStage<T> submit(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, this);
  }
}
