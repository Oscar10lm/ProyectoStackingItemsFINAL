package tower;

 

import java.util.ArrayList;
import java.util.List;
import shapes.Rectangle;

/**
 * Clase base abstracta que define el comportamiento común de los elementos apilables.
 * @author gaitan - lasso
 */
public abstract class StackingItem {
    protected static final int BLOCK_SIZE = 25;

    private final int id;
    private final int size;
    private final String color;
    private boolean visible;

    protected int xPosition;
    protected int yPosition;
    protected final ArrayList<Rectangle> parts;

    protected StackingItem(int id, int size, String color) {
        this.id = id;
        this.size = size;
        this.color = color;
        this.visible = false;
        this.xPosition = 0;
        this.yPosition = 0;
        this.parts = new ArrayList<>();
    }

    protected abstract void buildParts();

    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    protected void afterMove() {
        // Hook para subclases.
    }

    protected final void rebuild() {
        parts.clear();
        buildParts();
    }

    public void moveTo(int targetX, int targetY) {
        boolean wasVisible = visible;
        if (wasVisible) {
            makeInvisible();
        }

        this.xPosition = targetX;
        this.yPosition = targetY;
        rebuild();
        afterMove();

        if (wasVisible) {
            makeVisible();
        }
    }

    public void makeVisible() {
        visible = true;
        for (Rectangle part : parts) {
            part.makeVisible();
        }
    }

    public void makeInvisible() {
        visible = false;
        for (Rectangle part : parts) {
            part.makeInvisible();
        }
    }

    protected Rectangle buildBlock(int blockX, int blockY) {
        Rectangle block = new Rectangle();
        block.changeColor(color);
        block.moveHorizontal(blockX);
        block.moveVertical(blockY);
        return block;
    }

    public List<Rectangle> getParts() {
        return parts;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Ejecuta lógica personalizada antes de que el elemento se fije en la torre.
     */
    public abstract void applyPreStackEffect(Tower tower) throws TowerException;

    /**
     * Ejecuta lógica personalizada una vez que el elemento ha sido apilado.
     */
    public abstract void applyPostStackEffect(Tower tower) throws TowerException;

    /**
     * Determina si las condiciones actuales de la torre permiten retirar este elemento.
     */
    public abstract boolean canBeRemoved(Tower tower);
}
