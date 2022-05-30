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

package com.craftingdead.immerse.client.gui.view.property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;
import com.craftingdead.immerse.client.gui.view.style.StyleSource;
import com.craftingdead.immerse.client.gui.view.style.parser.value.ValueParser;
import com.craftingdead.immerse.client.gui.view.style.parser.value.ValueParserRegistry;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;
import com.craftingdead.immerse.client.gui.view.style.state.StateListener;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class StyleableProperty<T> extends BaseProperty<T>
    implements StateListener, PropertyDispatcher<T> {

  private final StyleNode owner;

  private final Int2ObjectMap<StyleSource> sources = new Int2ObjectOpenHashMap<>();

  private final Map<String, T> styleCache = new HashMap<>();

  private final ValueParser<T> parser;

  private final Int2ObjectMap<T> stateValues = new Int2ObjectOpenHashMap<>();

  private final Map<StyleNode, MutableInt> trackedStates = new HashMap<>();

  private Transition transition = Transition.INSTANT;

  @Nullable
  private Runnable transitionStopListener;

  @SafeVarargs
  public StyleableProperty(StyleNode owner, String name, Class<T> type, T defaultValue,
      Consumer<T>... listeners) {
    super(name, type, defaultValue, listeners);
    this.owner = owner;
    this.parser = ValueParserRegistry.getInstance().getParser(type);
    this.stateValues.put(0, defaultValue);
  }

  @Override
  public final void clearTrackedNodes() {
    this.trackedStates.keySet().forEach(node -> node.getStateManager().removeListener(this));
    this.trackedStates.clear();
  }

  /**
   * Varargs version of {@link #defineState(Object, Stream)}.
   */
  public final void defineState(T value, StyleNodeState... states) {
    this.defineState(value, Stream.of(states));
  }

  /**
   * User-facing method to define a state.
   * 
   * @param value - the value to associate the state with
   * @param states - the state
   */
  public void defineState(T value, Stream<StyleNodeState> states) {
    this.defineState(StyleSource.CODE, () -> value, states);
  }

  /**
   * Internal method used to associate a value with a given state.
   * 
   * @param state - the state
   * @param value - the value to associate the state with
   */
  protected final void setState(int state, T value) {
    if (state == 0) {
      this.set(value);
    }
    this.stateValues.put(state, value);
  }

  /**
   * Internal method used to reset a specific state.
   * 
   * @param state - the state to reset
   */
  protected final void resetState(int state) {
    if (state == 0) {
      this.setState(state, this.getDefaultValue());
    } else {
      this.stateValues.remove(state);
    }
  }

  public final Transition getTransition() {
    return this.transition;
  }

  @Override
  public final void setTransition(Transition transition) {
    this.transition = transition;
  }

  @Override
  public boolean transition(StyleNodeState state) {
    var trackedState = this.trackedStates.get(state.node());
    if (trackedState == null) {
      throw new IllegalStateException("Invalid node: " + state.node());
    }

    trackedState.setValue(state.getCombinedState());

    int combinedState = 0;
    for (var otherState : this.trackedStates.entrySet()) {
      combinedState += otherState.getValue().intValue();
    }

    T newValue = this.stateValues.get(combinedState);
    if (newValue == null) {
      return false;
    }

    if (this.transitionStopListener != null) {
      this.transitionStopListener.run();
    }

    if (!Objects.equals(newValue, this.getDirect())) {
      if (this.owner.isVisible()) {
        this.transitionStopListener = this.transition.transition(this, newValue);
      } else {
        this.set(newValue);
      }
    }

    return true;
  }

  @Override
  public boolean defineState(StyleSource source, String style, Collection<StyleNodeState> states) {
    return this.defineState(source,
        () -> this.styleCache.computeIfAbsent(style, this.parser::parse), states.stream());
  }

  private boolean defineState(StyleSource source, Supplier<T> style,
      Stream<StyleNodeState> states) {
    var state = states
        .peek(nodeState -> this.trackNode(nodeState.node()))
        .mapToInt(StyleNodeState::getCombinedState)
        .sum();
    var current = this.sources.get(state);
    if (current == null || source.compareTo(current) > -1) {
      this.sources.put(state, source);
      this.setState(state, style.get());
      return true;
    }
    return false;
  }

  private void trackNode(StyleNode node) {
    var v = this.trackedStates.putIfAbsent(node, new MutableInt());
    if (v != node) {
      node.getStateManager().addListener(this);
    }
  }

  @Override
  public void reset(Predicate<StyleSource> filter) {
    this.sources.int2ObjectEntrySet().removeIf(entry -> {
      if (filter.test(entry.getValue())) {
        this.resetState(entry.getIntKey());
        return true;
      }
      return false;
    });
  }

  @Override
  public int validate(String style) {
    return this.parser.validate(style);
  }
}
