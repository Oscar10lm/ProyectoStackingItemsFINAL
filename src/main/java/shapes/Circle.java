package shapes;

import java.awt.geom.Ellipse2D;

/**
 * Modela un círculo que puede mostrarse y manipularse en un lienzo.
 * @author Michael Kolling and David J. Barnes (Modified)
 * @version 1.0 (15 July 2000)()
 */
public class Circle extends Shape {

    public static final double PI = 3.1416;

    private int diameter;

    public Circle() {
        super(20, 15, "blue");
        diameter = 30;
    }

    /**
     * Ajusta el diámetro del círculo a un nuevo valor en píxeles.
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