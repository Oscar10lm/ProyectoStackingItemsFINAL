package shapes;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)()
 */
public class Rectangle extends Shape {

    public static int EDGES = 4;

    private int height;
    private int width;

    /**
     * Create a new rectangle at default position with default color.
     */
    public Rectangle() {
        super(0, 0, "magenta");
        height = 25;
        width = 25;
    }

    public Rectangle(int perimeter) {
        super(70, 15, "magenta");
        int side = perimeter / 4;
        height = side;
        width = side;
    }

    /**
     * Reset the size of the rectangle to its default values.
     */
    public void resetSize() {
        height = 30;
        width = 40;
        draw();
    }

    public int perimeter() {
        int suma = width + height;
        return 2 * suma;
    }

    public void zoom(char z) {
        if (z == '+') {
            width = width * 2;
            height = height * 2;
        } else if (z == '-') {
            width = width / 2;
            height = height / 2;
        }
        draw();
    }

    public void walk(int times) {
        int steps = Math.abs(times);

        for (int i = 0; i < steps; i++) {
            if (times > 0) {
                moveRight();
            } else {
                moveLeft();
            }
            moveDown();
        }
    }

    /**
     * Change the size to the new size.
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidth the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        if (newHeight < 0 || newWidth < 0) {
            System.out.println("Error, valor negativo no permitido");
        } else {
            erase();
            height = newHeight;
            width = newWidth;
            draw();
        }
    }

    @Override
    protected java.awt.Shape getShape() {
        return new java.awt.Rectangle(getXPosition(), getYPosition(), width, height);
    }
}
