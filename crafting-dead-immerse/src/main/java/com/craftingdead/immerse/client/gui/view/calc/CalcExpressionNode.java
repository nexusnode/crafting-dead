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

package com.craftingdead.immerse.client.gui.view.calc;

import com.craftingdead.immerse.client.gui.view.Length;
import com.google.common.base.Preconditions;

public interface CalcExpressionNode {

  float evaluate(float maxValue);

  Type type();

  record NumberNode(float value) implements CalcExpressionNode {

    @Override
    public float evaluate(float maxValue) {
      return this.value;
    }

    @Override
    public Type type() {
      return Type.NUMBER;
    }
  }

  record LengthNode(Length length) implements CalcExpressionNode {

    @Override
    public float evaluate(float maxValue) {
      return this.length.valueForLength(maxValue);
    }

    @Override
    public Type type() {
      return Type.LENGTH;
    }
  }

  record InversionNode(CalcExpressionNode child) implements CalcExpressionNode {

    @Override
    public float evaluate(float maxValue) {
      return 1.0F / this.child.evaluate(maxValue);
    }

    @Override
    public Type type() {
      return Type.INVERSION;
    }
  }

  record NegationNode(CalcExpressionNode child) implements CalcExpressionNode {

    @Override
    public float evaluate(float maxValue) {
      return -this.child.evaluate(maxValue);
    }

    @Override
    public Type type() {
      return Type.NEGATION;
    }
  }

  record OperationNode(Operator operator, CalcExpressionNode... children)
      implements CalcExpressionNode {

    @Override
    public float evaluate(float maxValue) {
      return switch (this.operator) {
        case ADD -> {
          var sum = 0.0F;
          for (var child : this.children) {
            sum += child.evaluate(maxValue);
          }
          yield sum;
        }
        case SUBTRACT -> {
          Preconditions.checkState(this.children.length == 2);
          var left = this.children[0].evaluate(maxValue);
          var right = this.children[1].evaluate(maxValue);
          yield left - right;
        }
        case MULTIPLY -> {
          var product = 1.0F;
          for (var child : this.children) {
            product *= child.evaluate(maxValue);
          }
          yield product;
        }
        case DIVIDE -> {
          Preconditions.checkState(this.children.length == 2);
          var left = this.children[0].evaluate(maxValue);
          var right = this.children[1].evaluate(maxValue);
          yield left / right;
        }
      };
    }

    @Override
    public Type type() {
      return Type.OPERATION;
    }
  }

  enum Operator {

    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE;
  }

  enum Type {

    NUMBER,
    LENGTH,
    OPERATION,
    NEGATION,
    INVERSION;
  }
}
