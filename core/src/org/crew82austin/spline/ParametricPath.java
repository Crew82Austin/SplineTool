package org.crew82austin.spline;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface ParametricPath {

  /**
   * Used within the LibGDX framework to draw the path.
   * @param batch
   */
  public void draw(ShapeRenderer renderer);

  /**
   * Draws the control handles for the selected Path.
   * @param batch
   */
  public void drawControls(ShapeRenderer renderer);

  /**
   * Returns the {@link BezierSpline} in the path closest to the point
   * specified.
   * @param x
   * @param y
   * @return
   */
  public BezierSpline getSpline(float x, float y);

  /**
   * Returns the distance from the provided point to the nearest point on
   * the path.
   * @param x
   * @param y
   * @return
   */
  public float distance(float x, float y);

  /**
   * Returns the distance squared from the provided point to the nearest
   * point on the path.  It's faster.
   * @param x
   * @param y
   * @return
   */
  public float distance2(float x, float y);
}
