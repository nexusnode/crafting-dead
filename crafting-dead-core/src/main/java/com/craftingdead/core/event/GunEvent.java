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

package com.craftingdead.core.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class GunEvent extends Event {

  private final Gun gun;
  private final ItemStack itemStack;

  public GunEvent(Gun gun, ItemStack itemStack) {
    this.gun = gun;
    this.itemStack = itemStack;
  }

  public Gun getGun() {
    return gun;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public static class Action extends GunEvent {

    private final LivingExtension<?, ?> living;

    public Action(Gun gun, ItemStack itemStack, LivingExtension<?, ?> living) {
      super(gun, itemStack);

      this.living = living;
    }

    public LivingExtension<?, ?> getLiving() {
      return living;
    }
  }

  @Cancelable
  public static class TriggerPressed extends Action {

    public TriggerPressed(Gun gun, ItemStack itemStack, LivingExtension<?, ?> living) {
      super(gun, itemStack, living);
    }
  }

  public static class Initialize extends GunEvent {

    private final Set<Attachment> attachments = new HashSet<>();
    private AmmoProvider ammoProvider;

    public Initialize(Gun gun, ItemStack itemStack, AmmoProvider ammoProvider) {
      super(gun, itemStack);
      this.ammoProvider = ammoProvider;
    }

    public void setAmmoProvider(AmmoProvider ammoProvider) {
      this.ammoProvider = ammoProvider;
    }

    public AmmoProvider getAmmoProvider() {
      return this.ammoProvider;
    }

    public void addAttachment(Attachment attachment) {
      this.attachments.add(attachment);
    }

    public Set<Attachment> getAttachments() {
      return Collections.unmodifiableSet(this.attachments);
    }
  }

  public static class Shoot extends Action {

    public Shoot(Gun gun, ItemStack itemStack, LivingExtension<?, ?> living) {
      super(gun, itemStack, living);
    }
  }

  @Cancelable
  public static class HitBlock extends Action {

    private final BlockHitResult result;
    private final BlockState blockState;
    private final Level level;

    public HitBlock(Gun gun, ItemStack itemStack, BlockHitResult result, BlockState blockState,
        LivingExtension<?, ?> living, Level level) {
      super(gun, itemStack, living);
      this.result = result;
      this.blockState = blockState;
      this.level = level;
    }

    public BlockState getBlockState() {
      return this.blockState;
    }

    public BlockHitResult getRayTraceResult() {
      return this.result;
    }

    public Level getLevel() {
      return this.level;
    }
  }

  @Cancelable
  public static class HitEntity extends Action {

    private final Entity target;
    private final Vec3 hitPos;
    private float damage;
    private boolean headshot;

    public HitEntity(
        Gun gun,
        ItemStack itemStack,
        LivingExtension<?, ?> living,
        Entity target,
        float damage,
        Vec3 hitPos,
        boolean headshot) {
      super(gun, itemStack, living);

      this.target = target;
      this.hitPos = hitPos;
      this.damage = damage;
      this.headshot = headshot;
    }

    public Entity getTarget() {
      return target;
    }

    public Vec3 getHitPos() {
      return hitPos;
    }

    public boolean isHeadshot() {
      return headshot;
    }

    public void setHeadshot(boolean headshot) {
      this.headshot = headshot;
    }

    public float getDamage() {
      return damage;
    }

    public void setDamage(float damage) {
      this.damage = damage;
    }
  }

  public static class ReloadFinish extends GunEvent {

    private ItemStack oldMagazineStack;
    private ItemStack newMagazineStack;

    public ReloadFinish(Gun gun, ItemStack itemStack, ItemStack oldMagazineStack,
        ItemStack newMagazineStack) {
      super(gun, itemStack);
      this.oldMagazineStack = oldMagazineStack;
      this.newMagazineStack = newMagazineStack;
    }

    public ItemStack getOldMagazineStack() {
      return this.oldMagazineStack;
    }

    public void setOldMagazineStack(ItemStack oldMagazineStack) {
      this.oldMagazineStack = oldMagazineStack;
    }

    public ItemStack getNewMagazineStack() {
      return this.newMagazineStack;
    }

    public void setNewMagazineStack(ItemStack newMagazineStack) {
      this.newMagazineStack = newMagazineStack;
    }
  }
}
