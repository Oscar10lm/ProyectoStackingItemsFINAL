package shapes;


/**
 * Clase base abstracta para las figuras del simulador.
 * Centraliza posición, color, visibilidad y movimiento.
 */
public abstract class Shape {
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean visible;

    protected Shape(int xPosition, int yPosition, String color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
        this.visible = false;
    }

    public void makeVisible() {
        visible = true;
        draw();
    }

    public void makeInvisible() {
        erase();
        visible = false;
    }

    public void moveRight() {
        moveHorizontal(20);
    }

    public void moveLeft() {
        moveHorizontal(-20);
    }

    public void moveUp() {
        moveVertical(-20);
    }

    public void moveDown() {
        moveVertical(20);
    }

    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    public void slowMoveHorizontal(int distance) {
        int delta = distance < 0 ? -1 : 1;
        int steps = Math.abs(distance);

        for (int i = 0; i < steps; i++) {
            xPosition += delta;
            draw();
        }
    }

    public void slowMoveVertical(int distance) {
        int delta = distance < 0 ? -1 : 1;
        int steps = Math.abs(distance);

        for (int i = 0; i < steps; i++) {
            yPosition += delta;
            draw();
        }
    }

    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    protected int getXPosition() {
        return xPosition;
    }

    protected int getYPosition() {
        return yPosition;
    }

    protected String getColor() {
        return color;
    }

    protected boolean isVisible() {
        return visible;
    }

    protected void setPosition(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    protected void draw() {
        if (visible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, getShape());
            canvas.wait(10);
        }
    }

    protected void erase() {
        if (visible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    protected abstract java.awt.Shape getShape();
}