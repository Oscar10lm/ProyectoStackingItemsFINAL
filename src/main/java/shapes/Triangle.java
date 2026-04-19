package shapes;

import java.awt.Polygon;

/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0  (15 July 2000)
 */
public class Triangle extends Shape {

    public static int VERTICES = 3;

    private int height;
    private int width;

    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle() {
        super(140, 15, "green");
        height = 30;
        width = 40;
    }

    /**
     * Change the size to the new size.
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidth the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    @Override
    protected java.awt.Shape getShape() {
        int[] xpoints = {
            getXPosition(),
            getXPosition() + (width / 2),
            getXPosition() - (width / 2)
        };
        int[] ypoints = {
            getYPosition(),
            getYPosition() + height,
            getYPosition() + height
        };
        return new Polygon(xpoints, ypoints, 3);
    }
}