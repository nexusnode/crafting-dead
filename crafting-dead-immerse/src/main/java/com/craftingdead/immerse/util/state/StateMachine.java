/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
