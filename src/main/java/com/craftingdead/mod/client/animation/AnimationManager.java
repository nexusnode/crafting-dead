package com.craftingdead.mod.client.animation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.craftingdead.mod.capability.animation.IAnimation;
import net.minecraft.item.ItemStack;

public class AnimationManager {

  private final Map<ItemStack, IAnimation> currentAnimations = new HashMap<>();
  private final Map<ItemStack, IAnimation> nextAnimations = new HashMap<>();

  public void tick() {
    Iterator<Entry<ItemStack, IAnimation>> nextIterator =
        this.nextAnimations.entrySet().iterator();
    nextIterator.forEachRemaining((entry) -> {
      ItemStack itemStack = entry.getKey();
      IAnimation animation = entry.getValue();
      this.currentAnimations.put(itemStack, animation);
      nextIterator.remove();
    });
    Iterator<Entry<ItemStack, IAnimation>> currentIterator =
        this.currentAnimations.entrySet().iterator();
    currentIterator.forEachRemaining((entry) -> {
      IAnimation animation = entry.getValue();
      if (animation.tick()) {
        currentIterator.remove();
      }
    });
  }

  public void setNextGunAnimation(ItemStack itemStack, IAnimation animation) {
    this.nextAnimations.put(itemStack, animation);
  }

  public void cancelCurrentAnimation(ItemStack itemStack) {
    this.currentAnimations.remove(itemStack);
  }

  public void clear(ItemStack itemStack) {
    this.cancelCurrentAnimation(itemStack);
    this.nextAnimations.remove(itemStack);
  }

  public IAnimation getCurrentAnimation(ItemStack itemStack) {
    return this.currentAnimations.get(itemStack);
  }
}
