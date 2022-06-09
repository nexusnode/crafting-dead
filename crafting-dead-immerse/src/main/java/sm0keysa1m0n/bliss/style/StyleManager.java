package sm0keysa1m0n.bliss.style;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.TransitionParser;

public class StyleManager {

  private static final Logger logger = LoggerFactory.getLogger(StyleManager.class);

  private final Map<String, PropertyDispatcher<?>> properties = new HashMap<>();
  private final List<PropertyDispatcher<?>> listeners = new ArrayList<>();

  private final StyleNode owner;

  private int state;

  public StyleManager(StyleNode owner) {
    this.owner = owner;
  }

  public StyleNode getOwner() {
    return this.owner;
  }

  public int getState() {
    return this.state;
  }

  public void addState(int state) {
    this.state |= state;
  }

  public void removeState(int state) {
    this.state &= ~state;
  }

  public boolean hasState(int state) {
    return (this.state & state) == state;
  }

  public boolean toggleState(int state) {
    if (this.hasState(state)) {
      this.removeState(state);
      return false;
    }
    this.addState(state);
    return true;
  }

  public void addListener(PropertyDispatcher<?> listener) {
    this.listeners.add(listener);
  }

  public void removeListener(PropertyDispatcher<?> listener) {
    this.listeners.remove(listener);
  }

  public void notifyListeners() {
    for (var listener : this.listeners) {
      listener.refreshState();
    }
  }

  public void parseInline(String css) throws ParserException {
    for (var propertyStr : css.split(";")) {
      var split = propertyStr.split(":", 2);
      var propertyName = split[0].trim();
      var property = this.properties.get(propertyName);
      if (property != null) {
        property.defineState(split[1].trim(), 1000, Set.of());
      }
    }
  }

  public Collection<PropertyDispatcher<?>> getProperties() {
    return this.properties.values();
  }

  public <T> PropertyDispatcher<T> registerProperty(PropertyDispatcher<T> property) {
    this.properties.put(property.getName(), property);
    return property;
  }

  public void refresh() {
    var styleList = this.owner.getStyleList();
    if (styleList == null) {
      return;
    }

    var rules = styleList.getRulesMatching(this.owner);
    var transitions = new ArrayList<StyleTransition>();

    this.properties.values().forEach(PropertyDispatcher::reset);

    for (var ruleEntry : rules.entrySet()) {
      var rule = ruleEntry.getKey();
      var nodeStates = ruleEntry.getValue();
      TransitionParser transitionParser = null;
      for (var property : rule.properties()) {
        var dispatcher = this.properties.get(property.name());
        if (dispatcher != null) {
          try {
            dispatcher.defineState(property.value(), rule.selector().getSpecificity(), nodeStates);
          } catch (ParserException e) {
            logger.warn("Failed to parse property: {}", property, e);
          }
          continue;
        }

        if (TransitionParser.isTransitionProperty(property.name())) {
          if (transitionParser == null) {
            transitionParser = new TransitionParser();
          }

          try {
            transitionParser.tryParse(property.name(), property.value());
          } catch (ParserException e) {
            logger.warn("Failed to parse transition property: {}", property, e);
            continue;
          }
        }
      }

      if (transitionParser != null) {
        try {
          transitionParser.build().ifPresent(transitions::add);
        } catch (ParserException e) {
          logger.warn("Failed to parse transition: ", e);
        }
      }
    }

    for (var transition : transitions) {
      transition.apply(this.properties);
    }

    this.properties.values().forEach(PropertyDispatcher::refreshState);

    this.owner.styleRefreshed(styleList.createFontManager());
  }
}
