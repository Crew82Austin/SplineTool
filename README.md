# SplineTool
Tool for modeling curves with Bezier splines

Controls:

ESC - deselect everything
e - when editing, Extend the current path by adding another spline

Click on a path to select it.  You can then drag the control points around
to change the shape of the path.

Click on the segment between two control points to select that particular
spline.  You will then have access to the weight handles.  In addition,
you can use the scroll wheel to increase or decrease the number of
subdivisions used when drawing that segment.


## To-Do
Need to be able to divide a segment in two.

Need to be able to delete a control point, thereby merging two adjacent
segments.

Need to be able to create a "loop" out of a path.  This would add one more
segment from the last control point to the first control point.

Need to be able to drag an entire path to reposition it.

Need to be able to print the coordinates of all the control points and
handles.

