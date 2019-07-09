package com.craftingdead.mod.client.animation;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

public class AnimationManager {

  private final Map<ItemStack, GunAnimation> currentAnimations = Maps.newHashMap();
  private final Map<ItemStack, GunAnimation> nextAnimations = Maps.newHashMap();

  public void tick() {
    Iterator<Entry<ItemStack, GunAnimation>> nextIterator = nextAnimations.entrySet().iterator();
    nextIterator.forEachRemaining((entry) -> {
      ItemStack itemStack = entry.getKey();
      GunAnimation animation = entry.getValue();
      currentAnimations.put(itemStack, animation);
      nextIterator.remove();
    });
    Iterator<Entry<ItemStack, GunAnimation>> currentIterator =
        currentAnimations.entrySet().iterator();
    currentIterator.forEachRemaining((entry) -> {
      ItemStack itemStack = entry.getKey();
      GunAnimation animation = entry.getValue();
      animation.tick();
      if (animation.isFinished()) {
        animation.onAnimationStopped(itemStack);
        currentIterator.remove();
      }
    });
  }

  public void setNextGunAnimation(ItemStack itemStack, GunAnimation animation) {
    nextAnimations.put(itemStack, animation);
  }

  public void cancelCurrentAnimation(ItemStack itemStack) {
    GunAnimation animation = currentAnimations.get(itemStack);
    if (animation != null) {
      animation.onAnimationStopped(itemStack);
    }
    currentAnimations.remove(itemStack);
  }

  public void clear(ItemStack itemStack) {
    cancelCurrentAnimation(itemStack);
    nextAnimations.remove(itemStack);
  }

  public GunAnimation getCurrentAnimation(ItemStack itemStack) {
    return currentAnimations.get(itemStack);
  }
}
