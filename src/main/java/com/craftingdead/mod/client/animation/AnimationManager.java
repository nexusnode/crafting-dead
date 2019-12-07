package com.craftingdead.mod.client.animation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;

public class AnimationManager {

  private final Map<ItemStack, IGunAnimation> currentAnimations = new HashMap<>();
  private final Map<ItemStack, IGunAnimation> nextAnimations = new HashMap<>();

  public void tick() {
    Iterator<Entry<ItemStack, IGunAnimation>> nextIterator =
        this.nextAnimations.entrySet().iterator();
    nextIterator.forEachRemaining((entry) -> {
      ItemStack itemStack = entry.getKey();
      IGunAnimation animation = entry.getValue();
      this.currentAnimations.put(itemStack, animation);
      nextIterator.remove();
    });
    Iterator<Entry<ItemStack, IGunAnimation>> currentIterator =
        this.currentAnimations.entrySet().iterator();
    currentIterator.forEachRemaining((entry) -> {
      IGunAnimation animation = entry.getValue();
      if (animation.tick()) {
        currentIterator.remove();
      }
    });
  }

  public void setNextGunAnimation(ItemStack itemStack, IGunAnimation animation) {
    this.nextAnimations.put(itemStack, animation);
  }

  public void cancelCurrentAnimation(ItemStack itemStack) {
    this.currentAnimations.remove(itemStack);
  }

  public void clear(ItemStack itemStack) {
    this.cancelCurrentAnimation(itemStack);
    this.nextAnimations.remove(itemStack);
  }

  public IGunAnimation getCurrentAnimation(ItemStack itemStack) {
    return this.currentAnimations.get(itemStack);
  }
}
