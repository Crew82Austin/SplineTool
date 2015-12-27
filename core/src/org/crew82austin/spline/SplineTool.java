package org.crew82austin.spline;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class SplineTool extends ApplicationAdapter implements InputProcessor {

  public enum MovementMode {
    NONE,
    CONTROL,
    START,
    END
  }

  protected Color                   background = Color.WHITE;
  protected SpriteBatch             batch;
  protected ShapeRenderer           renderer;
  protected Texture                 img;
  protected Set<CompositeSpline>    splines;
  protected CompositeSpline         selectedPath = null;
  protected BezierSpline            selectedSpline = null;
  protected ControlPoint            selectedControl = null;
  protected MovementMode            movementMode = MovementMode.NONE;
  protected boolean                 showImage = false;

  @Override
  public void create() {
    batch = new SpriteBatch();
    renderer = new ShapeRenderer();
    splines = new HashSet<>();
    img = new Texture("badlogic.jpg");
    splines.add(new CompositeSpline(20, 20, 80, 80));
    Gdx.input.setInputProcessor(this);
  }

  @Override
  public void render() {
    // Clear the background
    Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    if (showImage) {
      batch.begin();
      batch.draw(img, 0, 0);
      batch.end();
    }
    if (!splines.isEmpty()) {
      for (ParametricPath path : splines) {
        path.draw(renderer);
      }
    }
    if (selectedPath != null) {
      if (selectedSpline != null) {
        selectedSpline.drawHandles(renderer);
      }
      selectedPath.drawControls(renderer);
    }
  }

  @Override
  public boolean keyDown(int keycode) {
    System.out.println("keyDown(keycode="+keycode+")");
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    System.out.println("keyUp(keycode="+keycode+")");
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    boolean handled = false;
    System.out.println("keyTyped(character="+character+")");
    switch (character) {
    case 0x1B:
      clearSelection();
      handled = true;
      break;
    case 'e':
      if (selectedPath != null) {
        selectedPath.extend();
      }
      break;
    default:
      // Do nothing...
    }
    return handled;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    float x = screenX;
    float y = Gdx.graphics.getHeight() - screenY;
    System.out.println("touchDown(screenX="+screenX+", screenY="+screenY+", pointer="+pointer+", button="+button+")");
    // TODO Auto-generated method stub
    if (selectedPath == null) {
      // This will switch from viewing to editing
      for (CompositeSpline path : splines) {
        if (path.distance2(x, y) <= 16.0f) {
          selectedPath = path;
          return true;
        }
      }
    } else {
      // If no control points are selected, then see if the user clicked on
      // a control point.
      if (selectedSpline == null) {
        BezierSpline spline = selectedPath.getSpline(x, y);
        if (spline != null) {
          // If the user selected a control point, we'll remember that.
          if (spline.start.testControl(x, y, 2.0f)) {
            selectedControl = spline.start;
            movementMode = MovementMode.CONTROL;
          } else if (spline.end.testControl(x, y, 2.0f)) {
            selectedControl = spline.end;
            movementMode = MovementMode.CONTROL;
          } else {
            // Otherwise, select the spline itself.
            selectedSpline = spline;
            movementMode = MovementMode.NONE;
          }
        }
      } else {
        // A spline is selected.  Now, see if the user is trying to
        // manipulate a handle.
        if (selectedSpline.start.testStart(x, y, 2.0f)) {
          selectedControl = selectedSpline.start;
          movementMode = MovementMode.START;
        } else if (selectedSpline.end.testEnd(x, y, 2.0f)) {
          selectedControl = selectedSpline.end;
          movementMode = MovementMode.END;
        } else if (selectedSpline.start.testEnd(x, y, 2.0f)) {
          selectedControl = selectedSpline.start;
          movementMode = MovementMode.START;
        } else if (selectedSpline.end.testStart(x, y, 2.0f)) {
          selectedControl = selectedSpline.end;
          movementMode = MovementMode.END;
        } else if (selectedSpline.start.testControl(x, y, 2.0f)) {
          selectedControl = selectedSpline.start;
          movementMode = MovementMode.CONTROL;
        } else if (selectedSpline.end.testControl(x, y, 2.0f)) {
          selectedControl = selectedSpline.end;
          movementMode = MovementMode.CONTROL;
        } else {
          BezierSpline spline = selectedPath.getSpline(x, y);
          if (spline != selectedSpline) {
            selectedSpline = spline;
          } else {
            selectedSpline = null;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    System.out.println("touchUp(screenX="+screenX+", screenY="+screenY+", pointer="+pointer+", button="+button+")");
    selectedControl = null;
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    System.out.println("touchDragged(screenX="+screenX+", screenY="+screenY+", pointer="+pointer+")");
    if (selectedControl != null) {
      switch (movementMode) {
      case NONE:
        break;
      case CONTROL:
        selectedControl.setControl(screenX, Gdx.graphics.getHeight() - screenY);
        break;
      case START:
        selectedControl.setStart(screenX, Gdx.graphics.getHeight() - screenY);
        break;
      case END:
        selectedControl.setEnd(screenX, Gdx.graphics.getHeight() - screenY);
        break;
      }
    }
    return true;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    // TODO Auto-generated method stub
    // System.out.println("mouseMoved(screenX="+screenX+", screenY="+screenY+")");
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    if (selectedSpline != null) {
      selectedSpline.incDivisions(amount);
    }
    return false;
  }

  public void clearSelection() {
    selectedControl = null;
    selectedSpline = null;
    selectedPath = null;
    movementMode = MovementMode.NONE;
  }
}
