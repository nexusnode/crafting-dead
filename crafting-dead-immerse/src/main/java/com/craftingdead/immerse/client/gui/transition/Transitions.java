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

package com.craftingdead.immerse.client.gui.transition;

//import com.craftingdead.immerse.client.gui.transition.TransitionManager.TransitionType;
//import com.mojang.blaze3d.systems.RenderSystem;
//
//public enum Transitions implements ITransition {
//
//  GROW {
//
//    private static final float SCALE = 0.075F;
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void transform(double width, double height, float progress) {
//      float scaleOffset = (1.0F - progress) * SCALE;
//      RenderSystem.translated(width * -scaleOffset, height * -scaleOffset, 0.0F);
//      RenderSystem.scalef(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
//    }
//
//    @Override
//    public int getTransitionTime() {
//      return 250;
//    }
//
//    @Override
//    public TransitionType getTransitionType() {
//      return TransitionType.SINE;
//    }
//  }
//}
