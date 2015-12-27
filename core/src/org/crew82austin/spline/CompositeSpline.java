package org.crew82austin.spline;

import java.util.Vector;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CompositeSpline implements ParametricPath {

  protected Vector<BezierSpline>    segments;

  public CompositeSpline(float x1, float y1, float x2, float y2) {
    segments = new Vector<>();
    segments.add(new BezierSpline(x1, y1, x2, y2));
  }

  @Override
  public void draw(ShapeRenderer renderer) {
    for (ParametricPath path : segments) {
      path.draw(renderer);
    }
  }

  @Override
  public void drawControls(ShapeRenderer renderer) {
    // TODO: Could be made more efficient by only drawing each control
    //       once...
    for (ParametricPath path : segments) {
      path.drawControls(renderer);
    }
  }

  @Override
  public BezierSpline getSpline(float x, float y) {
    float minDist2 = 100.0f;
    BezierSpline minSpline = null;
    for (ParametricPath path : segments) {
      float testDist2 = path.distance2(x, y);
      if (testDist2 < minDist2) {
        minDist2 = testDist2;
        minSpline = path.getSpline(x, y);
      }
    }
    return minSpline;
  }

  @Override
  public float distance(float x, float y) {
    float minDist2 = 100.0f;
    for (ParametricPath path : segments) {
      float testDist2 = path.distance2(x, y);
      if (testDist2 < minDist2) {
        minDist2 = testDist2;
      }
    }
    return (float) Math.sqrt(minDist2);
  }

  @Override
  public float distance2(float x, float y) {
    float minDist2 = 100.0f;
    for (ParametricPath path : segments) {
      float testDist2 = path.distance2(x, y);
      if (testDist2 < minDist2) {
        minDist2 = testDist2;
      }
    }
    return minDist2;
  }

  public void extend() {
    if (!segments.isEmpty()) {
      ControlPoint start = segments.lastElement().end;
      ControlPoint end = new ControlPoint(start, 20, 20);
      segments.add(new BezierSpline(start, end));
    }
  }

}
