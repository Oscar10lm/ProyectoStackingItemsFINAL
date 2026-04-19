package shapes;

import java.awt.geom.Ellipse2D;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000)
 */
public class Circle extends Shape {

    public static final double PI = 3.1416;

    private int diameter;

    public Circle() {
        super(20, 15, "blue");
        diameter = 30;
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }

    @Override
    protected java.awt.Shape getShape() {
        return new Ellipse2D.Double(getXPosition(), getYPosition(), diameter, diameter);
    }
}