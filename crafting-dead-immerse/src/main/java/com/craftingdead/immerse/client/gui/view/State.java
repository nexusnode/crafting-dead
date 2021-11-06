package com.craftingdead.immerse.client.gui.view;

public class State {

  private final String name;

  private State(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  @Override
  public int hashCode() {
    return this.getName().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof State && ((State) obj).getName().equals(this.getName());
  }

  public static State create(String name) {
    return new State(name);
  }
}
