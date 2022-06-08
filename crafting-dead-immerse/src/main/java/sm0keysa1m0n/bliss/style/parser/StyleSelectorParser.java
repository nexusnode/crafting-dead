package sm0keysa1m0n.bliss.style.parser;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.style.selector.CompoundSelector;
import sm0keysa1m0n.bliss.style.selector.Selector;
import sm0keysa1m0n.bliss.style.selector.SingleSelector;
import sm0keysa1m0n.bliss.style.selector.combinator.Combinator;
import sm0keysa1m0n.bliss.style.selector.combinator.DescendantCombinator;
import sm0keysa1m0n.bliss.style.selector.combinator.SiblingCombinator;

class StyleSelectorParser {

  private StyleSelectorParser() {}

  static Selector[] readSelectors(String currentLine, NumberedLineIterator iterator) {
    var selectorBuilder = new StringBuilder(currentLine.trim());
    while (!currentLine.endsWith("{")) {
      currentLine = iterator.nextLine().trim();
      selectorBuilder.append(currentLine);
    }
    var test = readSelectorList(selectorBuilder.toString().replace("{", "").trim());
    return test;
  }

  static Selector[] readSelectorList(String selector) {
    if (selector.contains(",")) {
      var selectors = new Selector[StringUtils.countMatches(selector, ",") + 1];
      int i = 0;
      for (var subSelector : selector.split(",")) {
        selectors[i] = readSingleSelector(cleanSelector(subSelector.trim()));
        i++;
      }

      return selectors;
    }

    return new Selector[] {readSingleSelector(cleanSelector(selector))};
  }

  static Selector readSingleSelector(String selectorStr) {
    var bracketLevel = 0;
    var chars = selectorStr.toCharArray();

    var selectorStack = new CompoundSelector();
    var combinator = Combinator.AND;
    StringBuilder currentElement = null;
    for (int i = 0; i < chars.length; i++) {
      var ch = chars[i];

      if (ch == '(') {
        bracketLevel++;
        if (currentElement != null) {
          currentElement.append(ch);
        }
        continue;
      } else if (ch == ')') {
        bracketLevel--;
        if (currentElement != null) {
          currentElement.append(ch);
        }
        continue;
      } else if (bracketLevel != 0) {
        if (bracketLevel < 0) {
          throw new IllegalStateException("Uneven amount of brackets in: " + selectorStr);
        }
        if (currentElement != null) {
          currentElement.append(ch);
        }
        continue;
      }

      var nextCombinator = tryParseCombinator(ch);
      if (nextCombinator != null) {
        selectorStack.push(parseElement(currentElement.toString()), combinator);
        currentElement = null;
        combinator = nextCombinator;
        continue;
      }

      if (ch == ':' || ch == '.' || ch == '*' || ch == '#') {
        if (currentElement != null) {
          selectorStack.push(parseElement(currentElement.toString()), combinator);
          combinator = Combinator.AND;
        }
        currentElement = new StringBuilder();
        currentElement.append(ch);
        continue;
      }

      // Type selectors
      if (currentElement == null) {
        currentElement = new StringBuilder();
      }

      currentElement.append(ch);
    }

    if (currentElement != null) {
      selectorStack.push(parseElement(currentElement.toString()), combinator);
    }

    return selectorStack;
  }

  @Nullable
  static Combinator tryParseCombinator(char ch) {
    return switch (ch) {
      case '>' -> new DescendantCombinator(true);
      case ' ' -> new DescendantCombinator(false);
      case '+' -> new SiblingCombinator(true);
      case '~' -> new SiblingCombinator(false);
      default -> null;
    };
  }

  static SingleSelector parseElement(String part) {
    if (part.startsWith(":")) {
      var pseudoClass = part.substring(1);
      var structural =
          SingleSelector.parseStructural(pseudoClass, StyleSelectorParser::readSelectorList);
      if (structural != null) {
        return structural;
      } else {
        return SingleSelector.ofState(pseudoClass);
      }
    } else if (part.startsWith("*")) {
      return SingleSelector.WILDCARD;
    } else if (part.startsWith("#")) {
      return SingleSelector.ofId(part.substring(1));
    } else if (part.startsWith(".")) {
      return SingleSelector.ofClass(part.substring(1));
    } else {
      return SingleSelector.ofType(part);
    }
  }

  static String cleanSelector(String selector) {
    var cleanSelector = new StringBuilder(selector);

    int i = 0;
    while (i < cleanSelector.length()) {
      if (i != 0 && cleanSelector.charAt(i) == ' ') {
        int count = 1;
        while (cleanSelector.charAt(i + count) == ' ') {
          count++;
        }

        if (!isToken(cleanSelector.charAt(i + count))
            && !isToken(cleanSelector.charAt(i - 1))) {
          cleanSelector.replace(i, i + count, " ");
        } else {
          cleanSelector.delete(i, i + count);
        }
      }
      i++;
    }
    return cleanSelector.toString();
  }

  static boolean isToken(char c) {
    return c == '>'
        || c == '+'
        || c == '~';
  }
}
