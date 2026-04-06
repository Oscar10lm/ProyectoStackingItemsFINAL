package tower;

import shapes.Rectangle;

/**
 * Representa una tapa asociable a una taza.
 * @author Juan Gaitán and Oscar LasLidso
 */
public class Lid extends StackingItem {

    /**
     * Construye una tapa con identificador, tamaño y color.
     */
    public Lid(int id, int size, String color) {
        super(id, size, color);
    }

    /**
     * Construye la geometría de la tapa.
     */
    @Override
    protected void buildParts() {
        for (int i = 0; i < getSize(); i++) {
            Rectangle r = buildBlock(getX() + (i * BLOCK_SIZE), getY());
            parts.add(r);
        }
    }
}