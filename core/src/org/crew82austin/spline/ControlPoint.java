package org.crew82austin.spline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ControlPoint {

  public enum ControlMode {
    SMOOTH,
    COLINEAR,
    SHARP
  }

  static protected Color    controlColor  = Color.BLUE;
  static protected Color    smoothColor   = Color.GREEN;
  static protected Color    colinearColor = Color.ORANGE;
  static protected Color    sharpColor    = Color.RED;
  static protected Color    refColor      = Color.BLACK;

  protected ControlMode     mode = ControlMode.SMOOTH;
  protected Vector2         control;
  protected Vector2         start;
  protected Vector2         end;
  protected Vector2         startDelta;
  protected Vector2         endDelta;
  protected boolean         bStart = false;
  protected boolean         bEnd   = false;

  public ControlPoint(float x1, float y1, float x2, float y2, boolean bStart) {
    mode = ControlMode.SMOOTH;
    control = new Vector2(x1, y1);
    if (bStart) {
      start = new Vector2(x2, y2);
      startDelta = new Vector2(start);
      startDelta.sub(control);
      end = new Vector2(control);
      end.sub(startDelta);
      endDelta = new Vector2(end);
      endDelta.sub(control);
      this.bStart = true;
    } else {
      end = new Vector2(x2, y2);
      endDelta = new Vector2(end);
      endDelta.sub(control);
      start = new Vector2(control);
      start.sub(endDelta);
      startDelta = new Vector2(start);
      startDelta.sub(control);
      this.bEnd = true;
    }
  }

  public ControlPoint(ControlPoint ref, float offsetX, float offsetY) {
    mode = ControlMode.SMOOTH;
    control = new Vector2(ref.control);
    control.add(offsetX, offsetY);
    end = new Vector2(ref.end);
    end.add(offsetX, offsetY);
    endDelta = new Vector2(end);
    endDelta.sub(control);
    start = new Vector2(ref.start);
    start.add(offsetX, offsetY);
    startDelta = new Vector2(start);
    startDelta.sub(control);
    this.bEnd = true;
  }

  public void drawControl(ShapeRenderer renderer) {
    renderer.begin(ShapeType.Filled);
    renderer.setColor(controlColor);
    renderer.circle(control.x, control.y, 2.0f, 6);
    renderer.end();
  }

  public void drawHandles(ShapeRenderer renderer) {
    renderer.begin(ShapeType.Line);
    renderer.setColor(refColor);
    if (bStart) {
      renderer.line(start, control);
    }
    if (bEnd) {
      renderer.line(control, end);
    }
    switch (mode) {
    case SMOOTH:
      renderer.setColor(smoothColor);
      break;
    case COLINEAR:
      renderer.setColor(colinearColor);
      break;
    case SHARP:
      renderer.setColor(sharpColor);
      break;
    }
    if (bStart) {
      renderer.rect(start.x - 1, start.y - 1, 3, 3);
    }
    if (bEnd) {
      renderer.rect(end.x - 1,   end.y - 1,   3, 3);
    }
    renderer.end();
  }

  public Vector2 getControl() {
    return control;
  }

  public Vector2 getEnd() {
    return end;
  }

  public Vector2 getStart() {
    return start;
  }

  public void setControl(float newX, float newY) {
    control.set(newX, newY);
    start.set(control);
    start.add(startDelta);
    end.set(control);
    end.add(endDelta);
  }

  public void setStart(float startX, float startY) {
    startDelta.set(startX, startY);
    startDelta.sub(control);
    switch (mode) {
    case SMOOTH:
      // SMOOTH means control weights are equal but opposite
      endDelta.set(startDelta);
      endDelta.scl(-1.0f);
      break;
    case COLINEAR:
      float len = startDelta.len();
      // COLINEAR will update the direction but not the magnitude
      if (len > 0.0f) {
        endDelta.dot(startDelta);
        endDelta.scl(-1.0f / len);
      }
      break;
    case SHARP:
      // Nothing to do...
      break;
    }
    start.set(control);
    start.add(startDelta);
    end.set(control);
    end.add(endDelta);
  }

  public void setEnd(float endX, float endY) {
    endDelta.set(endX, endY);
    endDelta.sub(control);
    switch (mode) {
    case SMOOTH:
      // SMOOTH means control weights are equal but opposite
      startDelta.set(endDelta);
      startDelta.scl(-1.0f);
      break;
    case COLINEAR:
      // COLINEAR will update the direction but not the magnitude
      float len = endDelta.len();
      if (len > 0.0f) {
        startDelta.dot(endDelta);
        startDelta.scl(-1.0f / len);
      }
      break;
    case SHARP:
      // Nothing to do...
      break;
    }
    start.set(control);
    start.add(startDelta);
    end.set(control);
    end.add(endDelta);
  }

  public void toggleMode() {
    switch (mode) {
    case SMOOTH:
      mode = ControlMode.COLINEAR;
      break;
    case COLINEAR:
      mode = ControlMode.SHARP;
      break;
    case SHARP:
      mode = ControlMode.SMOOTH;
      break;
    }
  }

  public ControlMode getMode() {
    return mode;
  }

  public boolean testControl(Vector2 test, float epsilon) {
    return control.epsilonEquals(test, epsilon);
  }

  public boolean testControl(float testX, float testY, float epsilon) {
    return control.epsilonEquals(testX, testY, epsilon);
  }

  public boolean testStart(Vector2 test, float epsilon) {
    return bStart ? start.epsilonEquals(test, epsilon) : false;
  }

  public boolean testStart(float testX, float testY, float epsilon) {
    return bStart ? start.epsilonEquals(testX, testY, epsilon) : false;
  }

  public boolean testEnd(Vector2 test, float epsilon) {
    return bEnd ? end.epsilonEquals(test, epsilon) : false;
  }

  public boolean testEnd(float testX, float testY, float epsilon) {
    return bEnd ? end.epsilonEquals(testX, testY, epsilon) : false;
  }

  public boolean isStart() {
    return bStart;
  }

  public boolean isEnd() {
    return bEnd;
  }

  public void setStart(boolean bStart) {
    this.bStart = bStart;
  }

  public void setEnd(boolean bEnd) {
    this.bEnd = bEnd;
  }

}
