package sm0keysa1m0n.bliss.calc;

import com.google.common.base.Preconditions;
import sm0keysa1m0n.bliss.Length;

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
