/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.util.state;

import java.util.Iterator;
import java.util.function.Consumer;
import com.google.common.collect.Lists;

public class StateMachine<T extends State<CTX>, CTX> {

  private final CTX context;
  private final Iterator<T> states;
  private final Consumer<T> transitionHandler;

  private StateInstance<CTX> currentState;
  private boolean finished;

  public StateMachine(CTX context, T[] states, Consumer<T> transitionHandler) {
    this(context, Lists.newArrayList(states), transitionHandler);
  }

  public StateMachine(CTX context, Iterable<T> states,
      Consumer<T> transitionHandler) {
    this.context = context;
    this.states = states.iterator();
    this.transitionHandler = transitionHandler;
    this.nextState();
  }

  public boolean nextState() {
    if (!this.states.hasNext()) {
      return this.finished = true;
    }
    T nextState = this.states.next();
    this.currentState = nextState.newInstance(this.context);
    this.transitionHandler.accept(nextState);
    return false;
  }

  public boolean tick() {
    if (this.currentState.tick()) {
      this.nextState();
    }
    return this.finished;
  }

  public StateInstance<CTX> getCurrentState() {
    return this.currentState;
  }
}
