package shapes;

import java.awt.Polygon;

/**
 * Modela un triángulo que puede mostrarse y manipularse en un lienzo.
 * @author Michael Kolling and David J. Barnes (Modified)
 * @version 1.0 (15 July 2000)()
 */
public class Triangle extends Shape {

    public static int VERTICES = 3;

    private int height;
    private int width;

    /**
     * Crea un triángulo con dimensiones y posición predeterminadas.
     */
    public Triangle() {
        super(140, 15, "green");
        height = 30;
        width = 40;
    }

    /**
     * Ajusta la base y la altura del triángulo a nuevos valores en píxeles.
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