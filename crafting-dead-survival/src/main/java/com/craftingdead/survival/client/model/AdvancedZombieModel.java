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

package com.craftingdead.survival.client.model;

import java.util.List;
import com.craftingdead.survival.world.entity.monster.AdvancedZombie;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;

public class AdvancedZombieModel<T extends AdvancedZombie> extends ZombieModel<T> {

  public final ModelPart leftSleeve;
  public final ModelPart rightSleeve;
  public final ModelPart leftPants;
  public final ModelPart rightPants;
  public final ModelPart jacket;

  public AdvancedZombieModel(ModelPart zombieRoot, ModelPart playerRoot) {
    super(zombieRoot);
    this.leftSleeve = playerRoot.getChild("left_sleeve");
    this.rightSleeve = playerRoot.getChild("right_sleeve");
    this.leftPants = playerRoot.getChild("left_pants");
    this.rightPants = playerRoot.getChild("right_pants");
    this.jacket = playerRoot.getChild("jacket");
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return Iterables.concat(super.bodyParts(),
        List.of(this.leftSleeve, this.rightSleeve, this.leftPants, this.rightPants, this.jacket));
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {
    super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    this.leftSleeve.copyFrom(this.leftArm);
    this.rightSleeve.copyFrom(this.rightArm);
    this.leftPants.copyFrom(this.leftLeg);
    this.rightPants.copyFrom(this.rightLeg);
    this.jacket.copyFrom(this.body);
  }

  @Override
  public void setAllVisible(boolean visible) {
    super.setAllVisible(visible);
    this.leftSleeve.visible = visible;
    this.rightSleeve.visible = visible;
    this.leftPants.visible = visible;
    this.rightPants.visible = visible;
    this.jacket.visible = visible;
  }
}
