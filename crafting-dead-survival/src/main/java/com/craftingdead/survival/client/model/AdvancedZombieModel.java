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
    this.leftSleeve.copyFrom(this.leftLeg);
    this.rightSleeve.copyFrom(this.rightLeg);
    this.leftPants.copyFrom(this.leftArm);
    this.rightPants.copyFrom(this.rightArm);
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
