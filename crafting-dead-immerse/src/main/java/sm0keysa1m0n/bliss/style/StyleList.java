package sm0keysa1m0n.bliss.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.Typeface;
import io.github.humbleui.skija.paragraph.TypefaceFontProvider;
import sm0keysa1m0n.bliss.style.selector.Selector;
import sm0keysa1m0n.bliss.style.selector.StyleNodeState;

public class StyleList {

  private final List<StyleRule> rules = new ArrayList<>();
  private final Map<String, Typeface> fonts = new HashMap<>();

  public StyleList() {}

  public StyleList(StyleList original) {
    this.merge(original);
  }

  public List<StyleRule> getRules() {
    return this.rules;
  }

  public StyleList merge(StyleList source) {
    source.rules.forEach(rule -> this.addRule(rule.selector(), rule.properties()));
    this.fonts.putAll(source.fonts);
    return this;
  }

  public FontMgr createFontManager() {
    var fontManager = new TypefaceFontProvider();
    this.fonts.forEach((name, typeface) -> fontManager.registerTypeface(typeface, name));
    return fontManager;
  }

  public void addFont(String name, Typeface typeface) {
    this.fonts.put(name, typeface);
  }

  public void addRule(Selector selector, Set<StyleProperty> properties) {
    this.rules.add(new StyleRule(selector, properties));
  }

  public Map<StyleRule, Set<StyleNodeState>> getRulesMatching(StyleNode node) {
    var rules = new LinkedHashMap<StyleRule, Set<StyleNodeState>>();
    this.rules.forEach(rule -> rule.selector().match(node)
        .ifPresent(nodeState -> rules.put(rule, nodeState)));
    return rules;
  }
}
