package org.crew82austin.spline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

public class BezierSpline implements ParametricPath {

  static protected final float  EPSILON = 2.0f;
  static protected final float  MIN_DIVISIONS = 3.0f;

  protected Color           color = Color.BLACK;
  protected Bezier<Vector2> bezier;
  protected ControlPoint    start;
  protected ControlPoint    end;
  protected float           divisions = 10.0f;

  public BezierSpline(float x1, float y1, float x2, float y2) {
    start = new ControlPoint(x1, y1, x1 + (x2 - x1) * 0.2f, y1 + (y2 - y1) * 0.2f, true );
    end   = new ControlPoint(x2, y2, x2 - (x2 - x1) * 0.2f, y2 - (y2 - y1) * 0.2f, false);
    bezier = new Bezier<>(start.getControl(), start.getStart(), end.getEnd(), end.getControl());
  }

  public BezierSpline(ControlPoint start, ControlPoint end) {
    this.start = start;
    this.start.setStart(true);
    this.end   = end;
    this.end.setEnd(true);
    this.bezier = new Bezier<>(start.getControl(), start.getStart(), end.getEnd(), end.getControl());
  }

  @Override
  public void draw(ShapeRenderer renderer) {
    renderer.begin(ShapeType.Line);
    renderer.setColor(color);
    Vector2 prev = new Vector2();
    Vector2 cur = new Vector2();
    boolean first = true;
    float step = 1.0f / divisions;
    for (float t = 0.0f; t < 1.0f; t += step) {
      if (first) {
        bezier.valueAt(prev, t);
        first = false;
      } else {
        bezier.valueAt(cur, t);
        renderer.line(prev, cur);
        prev.set(cur);
      }
    }
    bezier.valueAt(cur, 1.0f);
    renderer.line(prev, cur);
    renderer.end();

  }

  @Override
  public void drawControls(ShapeRenderer renderer) {
    start.drawControl(renderer);
    end.drawControl(renderer);
  }

  public void drawHandles(ShapeRenderer renderer) {
    start.drawHandles(renderer);
    end.drawHandles(renderer);
  }

  @Override
  public BezierSpline getSpline(float x, float y) {
    return (distance2(x, y) <= 16.0f) ? this : null;
  }

  @Override
  public float distance(float x, float y) {
    Vector2 test = new Vector2(x, y);
    float t = bezier.approximate(test);
    bezier.valueAt(test, t);
    test.sub(x, y);
    return test.len();
  }

  @Override
  public float distance2(float x, float y) {
    Vector2 test = new Vector2(x, y);
    float t = bezier.approximate(test);
    bezier.valueAt(test, t);
    System.out.println("distance2("+x+", "+y+") at t="+t+" is ("+test.x+", "+test.y+")");
    test.sub(x, y);
    return test.len2();
  }

  public void incDivisions(float inc) {
    divisions += inc;
    if (divisions < MIN_DIVISIONS) {
      divisions = MIN_DIVISIONS;
    }
  }

}
