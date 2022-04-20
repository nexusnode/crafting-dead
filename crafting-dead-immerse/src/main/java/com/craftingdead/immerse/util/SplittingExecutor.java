/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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
