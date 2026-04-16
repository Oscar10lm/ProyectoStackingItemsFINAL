package tower;

 

import java.util.ArrayList;
import shapes.Rectangle;

/**
 * Representa una taza formada por bloques rectangulares.
 * @author Juan Gaitán and Oscar Lasso
 */
public class Cup extends StackingItem {

    private final ArrayList<Lid> lids;

    /**
     * Construye una taza con identificador, tamaño y color.
     */
    public Cup(int id, int size, String color) {
        super(id, size, color);
        lids = new ArrayList<>();
    }

    public Cup(int id, String color) {
        this(id, (2 * id) - 1, color);
    }

    @Override
    protected void buildParts() {
        int heightBlocks = getHeight();
        int widthBlocks = getSize();

        int baseYIndex = heightBlocks - 1;
        for (int c = 0; c < widthBlocks; c++) {
            Rectangle r = buildBlock(getX() + (c * BLOCK_SIZE), getY() + (baseYIndex * BLOCK_SIZE));
            parts.add(r);
        }

        for (int r = 0; r < heightBlocks - 1; r++) {
            Rectangle left = buildBlock(getX(), getY() + (r * BLOCK_SIZE));
            parts.add(left);

            if (getSize() > 1) {
                Rectangle right = buildBlock(getX() + ((widthBlocks - 1) * BLOCK_SIZE), getY() + (r * BLOCK_SIZE));
                parts.add(right);
            }
        }
    }

    @Override
    protected void afterMove() {
        int nestedLids = 0;
        int coverLids = 0;

        for (Lid lid : lids) {
            int lidX = getX() + ((getSize() - lid.getSize()) * BLOCK_SIZE) / 2;
            int lidY;
            if (lid.getSize() < getSize()) {
                int innerFloorY = getY() + getRealPixelHeight() - (2 * BLOCK_SIZE);
                lidY = innerFloorY - (nestedLids * BLOCK_SIZE);
                nestedLids++;
            } else {
                lidY = getY() - BLOCK_SIZE - (coverLids * BLOCK_SIZE);
                coverLids++;
            }
            lid.moveTo(lidX, lidY);
        }
    }

    @Override
    public void makeVisible() {
        super.makeVisible();
        for (Lid lid : lids) {
            lid.makeVisible();
        }
    }

    @Override
    public void makeInvisible() {
        super.makeInvisible();
        for (Lid lid : lids) {
            lid.makeInvisible();
        }
    }

    /**
     * Agrega una tapa a la taza.
     */
    public void addLid(Lid lid) {
        lids.add(lid);
    }

    /**
     * Elimina la tapa superior.
     */
    public void removeTopLid() {
        if (!lids.isEmpty()) {
            Lid lid = lids.remove(lids.size() - 1);
            lid.makeInvisible();
        }
    }

    /**
     * Elimina todas las tapas asociadas.
     */
    public void removeAllLids() {
        for (Lid lid : lids) {
            lid.makeInvisible();
        }
        lids.clear();
    }

    public boolean hasLids() {
        return !lids.isEmpty();
    }

    public ArrayList<Lid> getLids() {
        return lids;
    }

    public int getHeight() {
        return (2 * getSize() - 1);
    }

    public int getRealPixelHeight() {
        return getHeight() * BLOCK_SIZE;
    }

    public int getPixelWidth() {
        return getSize() * BLOCK_SIZE;
    }

    /**
     * Indica si esta taza elimina tapas bloqueantes al entrar.
     */
    public boolean shouldClearBlockingLids() {
        return false;
    }

    /**
     * Indica si esta taza desplaza objetos de menor tamano.
     */
    public boolean shouldDisplaceSmallerItems() {
        return false;
    }

    /**
     * Indica si esta taza no puede retirarse de la torre.
     */
    public boolean isLockedAtBase() {
        return false;
    }

    /**
     * Marca la taza como bloqueada en la base.
     * En la taza normal no hace nada.
     */
    public void lockAtBase() {
        // Hook polimorfico.
    }
}
