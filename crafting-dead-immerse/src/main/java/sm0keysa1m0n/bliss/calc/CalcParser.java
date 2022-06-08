package sm0keysa1m0n.bliss.calc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import sm0keysa1m0n.bliss.Length;

public class CalcParser {

  public static CalcExpressionNode parse(String expression) {
    // Sub-calcs are the same as just using parenthesis.
    expression.replace("calc", "");

    var terms = infixToRpn(expression);

    var stack = new ArrayDeque<CalcExpressionNode>();
    for (String term : terms) {
      if (term.equals("+")) {
        stack.push(new CalcExpressionNode.OperationNode(
            CalcExpressionNode.Operator.ADD, stack.pop(), stack.pop()));
      } else if (term.equals("-")) {
        var a = stack.pop();
        var b = stack.pop();
        stack.push(new CalcExpressionNode.OperationNode(
            CalcExpressionNode.Operator.SUBTRACT, b, a));
      } else if (term.equals("*")) {
        stack.push(new CalcExpressionNode.OperationNode(
            CalcExpressionNode.Operator.MULTIPLY, stack.pop(), stack.pop()));
      } else if (term.equals("/")) {
        var a = stack.pop();
        var b = stack.pop();
        stack.push(new CalcExpressionNode.OperationNode(
            CalcExpressionNode.Operator.DIVIDE, b, a));
      } else {
        if (term.endsWith("px")) {
          stack.push(new CalcExpressionNode.LengthNode(
              Length.fixed(Float.parseFloat(term.substring(0, term.length() - 2)))));
        } else if (term.endsWith("%")) {
          stack.push(new CalcExpressionNode.LengthNode(
              Length.percentage(Float.parseFloat(term.substring(0, term.length() - 1)))));
        } else {
          stack.push(new CalcExpressionNode.NumberNode(Float.parseFloat(term)));
        }
      }
    }

    return stack.pop();
  }

  // Method is used to get the precedence of operators
  private static boolean isValue(char c) {
    return Character.isLetterOrDigit(c) || c == '%' || c == '.';
  }

  // Operator having higher precedence
  // value will be returned
  private static int getPrecedence(char ch) {
    return switch (ch) {
      case '+', '-' -> 1;
      case '*', '/' -> 2;
      case '^' -> 3;
      default -> -1;
    };
  }

  // Operator has Left --> Right associativity
  private static boolean hasLeftAssociativity(char ch) {
    if (ch == '+' || ch == '-' || ch == '/' || ch == '*') {
      return true;
    } else {
      return false;
    }
  }

  // Shunting yard algorithm
  private static List<String> infixToRpn(String expression) {
    Deque<Character> stack = new ArrayDeque<>();

    List<String> output = new ArrayList<>();
    StringBuilder tokenBuilder = new StringBuilder();

    for (int i = 0; i < expression.length(); ++i) {
      var ch = expression.charAt(i);

      if (Character.isWhitespace(ch)) {
        continue;
      }

      if (isValue(ch)) {
        // If the scanned Token is an
        // operand, add it to output
        tokenBuilder.append(ch);
      } else if (ch == '(') {
        // If the scanned Token is an '('
        // push it to the stack
        stack.push(ch);
      } else if (ch == ')') {
        if (!tokenBuilder.isEmpty()) {
          output.add(tokenBuilder.toString());
          tokenBuilder = new StringBuilder();
        }
        // If the scanned Token is an ')' pop and append
        // it to output from the stack until an '(' is
        // encountered
        while (!stack.isEmpty()
            && stack.peek() != '(') {
          output.add(stack.pop().toString());
        }

        stack.pop();
      } else {
        if (!tokenBuilder.isEmpty()) {
          output.add(tokenBuilder.toString());
          tokenBuilder = new StringBuilder();
        }
        // If an operator is encountered then taken the
        // further action based on the precedence of the
        // operator
        while (!stack.isEmpty()
            && getPrecedence(ch) <= getPrecedence(stack.peek())
            && hasLeftAssociativity(ch)) {
          output.add(stack.pop().toString());
        }
        stack.push(ch);
      }
    }

    if (!tokenBuilder.isEmpty()) {
      output.add(tokenBuilder.toString());
    }

    // pop all the remaining operators from
    // the stack and append them to output
    while (!stack.isEmpty()) {
      if (stack.peek() == '(') {
        throw new IllegalStateException("Invalid expression: " + expression);
      }
      output.add(stack.pop().toString());
    }

    return output;
  }
}
