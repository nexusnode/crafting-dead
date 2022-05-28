package com.craftingdead.immerse.client.gui;

import java.util.Set;
import io.github.humbleui.skija.Path;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class Test {

  public void test() {

  }

  private void paintBorderSides(RRect outerBorder, RRect innerBorder, RectEdges<BorderEdge> edges,
      Set<BorderEdge> edgeSet) {
    var renderRadii = isRounded(outerBorder);

    var path = new Path();
    if (renderRadii) {
      path.addRRect(outerBorder);
    }


  }

  // The inner border adjustment for bleed avoidance mode BackgroundBleedBackgroundOverBorder
  // is only applied to sideRect, which is okay since BackgroundBleedBackgroundOverBorder
  // is only to be used for solid borders and the shape of the border painted by
  // drawBoxSideFromPath only depends on sideRect when painting solid borders.
  private void paintOneSide(BoxSide side, BoxSide adjacentSide1, BoxSide adjacentSide2,
      RectEdges<BorderEdge> edges, Set<BorderEdge> edgeSet, Rect sideRect) {
    var edge = edges.at(side);
    if (!edge.shouldRender() || !edgeSet.contains(edge)) {
      return;
    }
    
    float firstRadius;
    float secondRadius;
    
    
    switch(side) {
      case TOP -> {
        sideRect = sideRect.withHeight(edge.edgeWidth());
        firstRadius = innerBorder.
      }
    }
  }

  private static boolean isRounded(RRect rect) {
    for (var radius : rect._radii) {
      if (radius > 0) {
        return true;
      }
    }
    return false;
  }
}
